package com.wallpapers_manager.cyril.wallpapers;

public class Folder {	
	private int 	mId;
	private String 	mName;
	
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
