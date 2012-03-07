package com.wallpapers_manager.cyril.data;

import android.database.Cursor;

public class WallpaperPlaylist {	
	private int 	mId;
	private int 	mWallpaperId;
	private int 	mPlaylistId;
	
	public WallpaperPlaylist(Cursor cursor) {
		this(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2));
	}
	
	public WallpaperPlaylist(int wppId, int rtlId) {
		this(-1, wppId, rtlId);
	}
	
	public WallpaperPlaylist(int id, int wallpaperId, int rotateListId) {
		mId = id;
		mWallpaperId = wallpaperId;
		mPlaylistId = rotateListId;
	}

	public void setId(int mId) { this.mId = mId; }

	public int getId() { return mId; }

	public void setWallpaperId(int wallpaperId) {
		this.mWallpaperId = wallpaperId;
	}

	public int getWallpaperId() { return mWallpaperId; }

	public void setPlaylistId(int rotateListId) {
		this.mPlaylistId = rotateListId;
	}

	public int getPlaylistId() { return mPlaylistId; }
}
