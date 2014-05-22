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

@Api(name = "userinfoendpoint", namespace = @ApiNamespace(ownerDomain = "cloudid.com", ownerName = "cloudid.com", packagePath = ""))
public class UserInfoEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listUserInfo")
	public CollectionResponse<UserInfo> listUserInfo(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		EntityManager mgr = null;
		Cursor cursor = null;
		List<UserInfo> execute = null;

		try {
			mgr = getEntityManager();
			Query query = mgr.createQuery("select from UserInfo as UserInfo");
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
			}

			if (limit != null) {
				query.setFirstResult(0);
				query.setMaxResults(limit);
			}

			execute = (List<UserInfo>) query.getResultList();
			cursor = JPACursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (UserInfo obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<UserInfo> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getUserInfo")
	public UserInfo getUserInfo(@Named("id") String id) {
		EntityManager mgr = getEntityManager();
		UserInfo userinfo = null;
		try {
			userinfo = mgr.find(UserInfo.class, id);
		} finally {
			mgr.close();
		}
		return userinfo;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param userinfo the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertUserInfo")
	public UserInfo insertUserInfo(UserInfo userinfo) {
		EntityManager mgr = getEntityManager();
		try {
			if (containsUserInfo(userinfo)) {
				throw new EntityExistsException("Object already exists");
			}
			mgr.persist(userinfo);
		} finally {
			mgr.close();
		}
		return userinfo;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param userinfo the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateUserInfo")
	public UserInfo updateUserInfo(UserInfo userinfo) {
		EntityManager mgr = getEntityManager();
		try {
			if (!containsUserInfo(userinfo)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.persist(userinfo);
		} finally {
			mgr.close();
		}
		return userinfo;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeUserInfo")
	public void removeUserInfo(@Named("id") String id) {
		EntityManager mgr = getEntityManager();
		try {
			UserInfo userinfo = mgr.find(UserInfo.class, id);
			mgr.remove(userinfo);
		} finally {
			mgr.close();
		}
	}
	
	@ApiMethod(httpMethod="GET",name= "getCount")
	public void getCount(@Named("id") String id){
		EntityManager mgr = getEntityManager();
		try {
			UserInfo userinfo = mgr.find(UserInfo.class, id);
			
		} finally {
			mgr.close();
		}
		
	}
	

	private boolean containsUserInfo(UserInfo userinfo) {
		EntityManager mgr = getEntityManager();
		boolean contains = true;
		try {
			UserInfo item = mgr.find(UserInfo.class, userinfo.getID());
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
