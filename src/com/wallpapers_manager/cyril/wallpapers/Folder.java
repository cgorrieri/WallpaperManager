package com.wallpapers_manager.cyril.wallpapers;

import android.database.Cursor;

public class Folder {	
	private int 	mId;
	private String 	mName;
	
	public Folder(Cursor cursor) {
		this(cursor.getInt(0), cursor.getString(1));
	}
	
	public Folder(String name) {
		mName = name;
	}
	
	public Folder(int id, String name) {
		mId = id;
		mName = name;
	}
	
	public int getId() { return mId; }
	public void setId(int id) { mId = id; }
	
	public String getName() { return mName; }
	public void setAddress(String name) { this.mName = name; }
}
