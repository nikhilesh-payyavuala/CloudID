package com.cloudid;

import com.google.android.gms.plus.model.people.Person;

import android.os.Parcel;
import android.os.Parcelable;

public class UserInfoParcelable implements Parcelable{
	
	
	private String userID;
	private String emailID;
	
	
	
	
	

	

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}

}
