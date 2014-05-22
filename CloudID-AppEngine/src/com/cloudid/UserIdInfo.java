package com.cloudid;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity 
public class UserIdInfo {

	@Id
	private String IDHash;
    private String userName;
	private String lastLocation;
	private String ID;
	private String fName;
	private String lName;
	private String dob;
	private String expiryDate;
	private String rewardPoints;
	private String imageURL;
	private long timeStampCreated;
	private String lastAccessedBy;
	private long timeStampLastAccessed;
	private String type;
	
	public String getRewardPoints() {
		return rewardPoints;
	}
	public void setRewardPoints(String rewardPoints) {
		this.rewardPoints = rewardPoints;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	
	
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getLastAccessedBy() {
		return lastAccessedBy;
	}
	public void setLastAccessedBy(String lastAccessedBy) {
		this.lastAccessedBy = lastAccessedBy;
	}	
	public String getLastLocation() {
		return lastLocation;
	}
	public void setLastLocation(String lastLocation) {
		this.lastLocation = lastLocation;
	}
	public long getTimeStampCreated() {
		return timeStampCreated;
	}
	public void setTimeStampCreated(long timeStampCreated) {
		this.timeStampCreated = timeStampCreated;
	}
	public long getTimeStampLastAccessed() {
		return timeStampLastAccessed;
	}
	public void setTimeStampLastAccessed(long timeStampLastAccessed) {
		this.timeStampLastAccessed = timeStampLastAccessed;
	}
	
	public String getIDHash() {
		return IDHash;
	}
	public void setIDHash(String iDHash) {
		IDHash = iDHash;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getfName() {
		return fName;
	}
	public void setfName(String fName) {
		this.fName = fName;
	}
	public String getlName() {
		return lName;
	}
	public void setlName(String lName) {
		this.lName = lName;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	
}
