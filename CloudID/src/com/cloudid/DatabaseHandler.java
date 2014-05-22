package com.cloudid;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "CloudID";
 
    // Labels table name
    private static final String TABLE_LABELS = "User_ID";
 
    // Labels Table Columns names
    private static final String ID_UNIQUEID = "uid";
    private static final String ID_FNAME = "First_Name";
    private static final String ID_LNAME = "Last_Name";
    private static final String ID_DOB = "DOB";    
    private static final String ID_UFID = "UFID";
    private static final String ID_UFID_HASH = "UFID_MD5";
    private static final String ID_TIMESTAMP = "Timestamp";
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
    	// Category table create query
    	String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_LABELS + "("
        		+ ID_UNIQUEID + " INTEGER PRIMARY KEY," + ID_FNAME + " TEXT,"+ID_LNAME + " TEXT,"+ID_DOB + " TEXT,"+ID_UFID+" TEXT,"+ID_UFID_HASH+" TEXT,"+ID_TIMESTAMP+" TEXT)";
    	db.execSQL(CREATE_CATEGORIES_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LABELS);
 
        // Create tables again
        onCreate(db);
    }
    
    /**
     * Inserting new lable into lables table
     * */
    public int insertID(String ufid,String fname,String lname,String dob,String hash){
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	String selectQuery = "SELECT count(*) FROM " + TABLE_LABELS+" where "+ ID_UFID+ " ='"+ufid+"'";
    	Cursor mCount=db.rawQuery(selectQuery, null);
    	mCount.moveToFirst();
    	int count= mCount.getInt(0);
    	
    	mCount.close();
    	if(count==1){
    		return 0;
    	}
    	
    	
    	ContentValues values = new ContentValues();
    	values.put(ID_UFID, ufid);
    	values.put(ID_FNAME, fname);
    	values.put(ID_LNAME, lname);
    	values.put(ID_DOB, dob);
    	values.put(ID_UFID_HASH,hash );
    	values.put(ID_TIMESTAMP, "");
    	 
    	// Inserting Row
        db.insert(TABLE_LABELS, null, values);
        db.close(); // Closing database connection
        return 1;//success
    }
    
    /**
     * Getting all labels
     * returns list of labels
     * */
    public List<ID> getAllID(){
    	List<ID> labels = new ArrayList<ID>();
    	
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_LABELS;
     
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	ID id= new ID();
            	id.setFName(cursor.getString(1));
            	id.setLName(cursor.getString(2));
            	id.setDOB(cursor.getString(3));
            	id.setUFID(cursor.getString(4));
            	labels.add(id);
            	//labels.add("NAME: "+cursor.getString(1)+" "+cursor.getString(2)+" \nDATE OF BIRTH: "+ cursor.getString(3)+" \nUFID: "+cursor.getString(4));
            } while (cursor.moveToNext());
        }
        
        // closing connection
        cursor.close();
        db.close();
    	
    	// returning lables
    	return labels;
    }

	public void deleteID(String dlabel) {
		// TODO Auto-generated method stub
		//String deleteQuery = "DELETE FROM "  + QUEST_NAME + " WHERE " + QUEST_NAME+" = "+dlabel;
		SQLiteDatabase db = this.getReadableDatabase();
		db.delete(TABLE_LABELS,  ID_UFID+" = '"+dlabel+"'", null);
		db.close();
	}

	public String[] selectIDRow(String edit) {
		// TODO Auto-generated method stub
		String[] row = new String[3];
		SQLiteDatabase db = this.getReadableDatabase();
		 String selectQuery = "SELECT * FROM " + TABLE_LABELS+" WHERE "+ID_UFID+" ='"+edit+"'";
		 Cursor cursor = db.rawQuery(selectQuery, null);
		 cursor.moveToFirst();
	     row[0]=cursor.getString(1);
	     row[1]=cursor.getString(2);
	     row[2]=cursor.getString(4);
	     
	           db.close();
	        
		 
		return row;
		 
		 
		
		
	}
}

	/*public void updateRow(String[] final_row, String edit) {
		// TODO Auto-generated method stub
		Log.i("edit", edit);
		SQLiteDatabase db = this.getReadableDatabase();
		String update="UPDATE "+TABLE_LABELS+" SET "+QUEST_NAME+" ='"+final_row[0]+"',"+QUEST_DESCRIPTION+" ='"+final_row[1]+"',"+QUEST_REWARD+"="+final_row[2]+" WHERE "+QUEST_NAME+" ='"+edit+"'";
		ContentValues args = new ContentValues();
		args.put(QUEST_NAME,final_row[0]);
		args.put(QUEST_DESCRIPTION,final_row[1]);
		args.put(QUEST_REWARD,final_row[2]);
		//db.update(TABLE_LABELS, args,QUEST_NAME+"='"+edit+"'" , null);
		Cursor cursor = db.rawQuery(update, null);
		System.out.print(cursor.getCount());
		db.close();
	}
	
*}*/
