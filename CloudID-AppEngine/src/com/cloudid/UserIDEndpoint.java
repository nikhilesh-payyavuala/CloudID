package com.cloudid;

import com.cloudid.EMF;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JPACursorHelper;

import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@Api(name = "useridendpoint", namespace = @ApiNamespace(ownerDomain = "cloudid.com", ownerName = "cloudid.com", packagePath = ""))
public class UserIDEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listUserID")
	public CollectionResponse<UserID> listUserID(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		EntityManager mgr = null;
		Cursor cursor = null;
		List<UserID> execute = null;

		try {
			mgr = getEntityManager();
			Query query = mgr.createQuery("select from UserID as UserID");
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
			}

			if (limit != null) {
				query.setFirstResult(0);
				query.setMaxResults(limit);
			}

			execute = (List<UserID>) query.getResultList();
			cursor = JPACursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (UserID obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<UserID> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getUserID")
	public UserID getUserID(@Named("id") String id) {
		EntityManager mgr = getEntityManager();
		UserID userid = null;
		try {
			userid = mgr.find(UserID.class, id);
		} finally {
			mgr.close();
		}
		return userid;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param userid the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertUserID")
	public UserID insertUserID(UserID userid) {
		EntityManager mgr = getEntityManager();
		try {
			if (containsUserID(userid)) {
				throw new EntityExistsException("Object already exists");
			}
			mgr.persist(userid);
		} finally {
			mgr.close();
		}
		return userid;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param userid the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateUserID")
	public UserID updateUserID(UserID userid) {
		EntityManager mgr = getEntityManager();
		try {
			if (!containsUserID(userid)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.persist(userid);
		} finally {
			mgr.close();
		}
		return userid;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeUserID")
	public void removeUserID(@Named("id") String id) {
		EntityManager mgr = getEntityManager();
		try {
			UserID userid = mgr.find(UserID.class, id);
			mgr.remove(userid);
		} finally {
			mgr.close();
		}
	}

	private boolean containsUserID(UserID userid) {
		EntityManager mgr = getEntityManager();
		boolean contains = true;
		try {
			UserID item = mgr.find(UserID.class, userid.getIDHash());
			if (item == null) {
				contains = false;
			}
		} finally {
			mgr.close();
		}
		return contains;
	}

	private static EntityManager getEntityManager() {
		return EMF.get().createEntityManager();
	}

}
