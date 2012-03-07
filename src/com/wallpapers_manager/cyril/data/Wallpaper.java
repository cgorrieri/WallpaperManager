package com.wallpapers_manager.cyril.data;

import java.io.File;

import android.database.Cursor;

import com.wallpapers_manager.cyril.WallpaperManagerConstants;
import com.wallpapers_manager.cyril.bdd.WallpapersDBAdapter;

/** Represnt a wallpaper */
public class Wallpaper {
	private int mId;
	private int mFolderId;
	private String mAddress;

	/**
	 * Create a wallpaper by a cursor
	 * 
	 * @param c
	 *            Cursor
	 */
	public Wallpaper(Cursor c) {
		this(c.getInt(0), c.getInt(1), c.getString(2));
	}

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

	public int getId() {
		return mId;
	}

	public void setId(int id) {
		mId = id;
	}

	public int getFolderId() {
		return mFolderId;
	}

	public void setFolderId(int folderId) {
		mFolderId = folderId;
	}

	public String getAddress() {
		return mAddress;
	}

	public void setAddress(String address) {
		mAddress = address;
	}

	public boolean delete(WallpapersDBAdapter wallpapersDBAdapter) {
		wallpapersDBAdapter.removeWallpaper(mId);
		File file = new File(WallpaperManagerConstants._registrationFilesDir,
				mAddress);
		/* TODO Do thumbnail for performance
		File thumbnailFile = new File(WallpaperManagerConstants._registrationFilesDir, "thumbnail_"+ mAddress);
		return file.delete() && thumbnailFile.delete();
		//*/
		return file.delete();
	}

	public String toString() {
		return "Id :" + mId + ", Folder_id :" + mFolderId + ", Address :"
				+ mAddress;
	}
}
