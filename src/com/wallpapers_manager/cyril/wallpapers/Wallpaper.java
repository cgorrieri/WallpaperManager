package com.wallpapers_manager.cyril.wallpapers;

import java.io.File;

import com.wallpapers_manager.cyril.WallpaperManagerConstants;



public class Wallpaper {	
	private int 	mId;
	private int 	mFolderId;
	private String 	mAddress;
	
	public Wallpaper(String address) {
		this(-1, 0, address);
	}
	public Wallpaper(int folderId, String address) {
		this(-1, folderId, address);
	}
	
	public Wallpaper(int id, int folderId, String address) {
		mId = id;
		mFolderId = folderId;
		mAddress = address;
	}
	
	public int getId() { return mId; }
	public void setId(int e_id) { mId = e_id; }
	
	public int getFolderId() { return mFolderId; }
	public void setFolderId(int folderId) { mFolderId = folderId; }
	
	public String getAddress() { return mAddress; }
	public void setAddress(String address) { mAddress = address; }
	
	public boolean delete(WallpapersDBAdapter wallpapersDBAdapter) 
	{
		wallpapersDBAdapter.removeWallpaper(mId);
		File file = new File(WallpaperManagerConstants._registrationFilesDir, mAddress);
		File thumbnailFile = new File(WallpaperManagerConstants._registrationFilesDir, "thumbnail_"+mAddress);
		return file.delete() && thumbnailFile.delete();
	}
	
	public String toString() {
		return "Id :"+mId+", Folder_id :"+mFolderId+", Address :"+mAddress;
	}
}
