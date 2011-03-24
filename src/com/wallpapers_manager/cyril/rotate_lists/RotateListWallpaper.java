package com.wallpapers_manager.cyril.rotate_lists;

public class RotateListWallpaper {	
	private int 	mId;
	private int 	mWallpaperId;
	private int 	mRotateListId;
	
	public RotateListWallpaper(int wppId, int rtlId) {
		this(-1, wppId, rtlId);
	}
	
	public RotateListWallpaper(int id, int wallpaperId, int rotateListId) {
		mId = id;
		mWallpaperId = wallpaperId;
		mRotateListId = rotateListId;
	}

	public void setId(int mId) { this.mId = mId; }

	public int getId() { return mId; }

	public void setWallpaperId(int wallpaperId) {
		this.mWallpaperId = wallpaperId;
	}

	public int getWallpaperId() { return mWallpaperId; }

	public void setRotateListId(int rotateListId) {
		this.mRotateListId = rotateListId;
	}

	public int getRotateListId() { return mRotateListId; }
}
