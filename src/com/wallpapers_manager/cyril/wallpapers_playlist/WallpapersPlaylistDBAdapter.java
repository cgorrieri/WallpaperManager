package com.wallpapers_manager.cyril.wallpapers_playlist;

import java.util.ArrayList;

import com.wallpapers_manager.cyril.WMSQLiteOpenHelper;
import com.wallpapers_manager.cyril.folders.Folder;
import com.wallpapers_manager.cyril.playlists.Playlist;
import com.wallpapers_manager.cyril.wallpapers.Wallpaper;
import com.wallpapers_manager.cyril.wallpapers.WallpapersDBAdapter;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class WallpapersPlaylistDBAdapter {

	private static final int VERSION = 1;
	
	private static final String 	TABLE = "playlist_wallpaper_assoc";
	private static final String 	ID = "_id";
	public 	static final int		ID_IC = 0;
	private static final String 	WALLPAPER_ID = "wallpaper_id";
	public 	static final int		WALLPAPER_ID_IC = 1;
	private static final String 	PLAYLIST_ID = "playlist_id";
	public 	static final int		PLAYLIST_ID_IC = 2;
	
	private SQLiteDatabase 			mDataBase;
	private WMSQLiteOpenHelper 		mBaseHelper;
	private WallpapersDBAdapter 	mWallpapersDBAdapter;
	
	public WallpapersPlaylistDBAdapter(Context context) {
		mBaseHelper = new WMSQLiteOpenHelper(context, TABLE+".db", null, VERSION);
		mWallpapersDBAdapter = new WallpapersDBAdapter(context);
	}
	
	public void open() {
		mDataBase = mBaseHelper.getWritableDatabase();
	}
	
	public void close() {
		mDataBase.close();
	}
	
	public SQLiteDatabase getDataBase() {
		return mDataBase;
	}
	
	/**
	 * Get a Cursor for playlist_id
	 * @param e_playlist_id
	 * @return Cursor
	 */
	public Cursor getCursor(int playList){
		Cursor c = mDataBase.query(TABLE, new String[] {ID,WALLPAPER_ID,PLAYLIST_ID}, PLAYLIST_ID+" = "+playList+"", null, null, null, null);
		return c;
	}
	
	/**
	 * Test if a PlaylistWallpaper already exist
	 * @param wallpaperPlaylist
	 * @return true if exist, false otherwise
	 */
	public boolean rtlWppExist(WallpaperPlaylist wallpaperPlaylist) {
		Cursor c = mDataBase.query(TABLE, new String[] {ID,WALLPAPER_ID,PLAYLIST_ID}, PLAYLIST_ID+" = "+wallpaperPlaylist.getPlaylistId()+" AND "+WALLPAPER_ID+" = "+wallpaperPlaylist.getWallpaperId(), null, null, null, null);
		int count = c.getCount();
		c.close();
		return count == 1;
	}

	/**
	 * Get a ArrayList of Wallpaper from Playlist
	 * @param playlist
	 * @return ArrayList<Wallpaper>
	 */
	public ArrayList<Wallpaper> getWallpapersFromPlaylist(Playlist playlist){
		return cursorToWallpapers(getCursor(playlist.getId()));
	}
	
	/**
	 * Insert a new PlaylistWallpaper
	 * @param wallpaperPlaylist
	 * @return id of the new PlaylistWallpaper, -1 otherwise
	 */
	public long insertWallpaperPlaylist(WallpaperPlaylist wallpaperPlaylist) {
		if(wallpaperPlaylist.getPlaylistId() == -1 || wallpaperPlaylist.getWallpaperId() == -1 || rtlWppExist(wallpaperPlaylist))
			return -1;
		
		ContentValues values = new ContentValues();
		values.put(WALLPAPER_ID, wallpaperPlaylist.getWallpaperId());
		values.put(PLAYLIST_ID, wallpaperPlaylist.getPlaylistId());
		return mDataBase.insert(TABLE, null, values);
	}
	
	/**
	 * Insert the folder's content in the rotate list 
	 * @param folder
	 * @param playlist
	 */
	public void insertPlaylistWallpaperForFolder(Folder folder, Playlist playlist) {
		mWallpapersDBAdapter.open();
			ArrayList<Wallpaper> wallpapersList = mWallpapersDBAdapter.getWallpapersFromFolder(folder);
		mWallpapersDBAdapter.close();
		WallpaperPlaylist wallpaperPlaylist = new WallpaperPlaylist(-1,playlist.getId());
		for(Wallpaper wallpaper : wallpapersList){
			wallpaperPlaylist.setWallpaperId(wallpaper.getId());
			this.insertWallpaperPlaylist(wallpaperPlaylist);
		}
	}
	
	/**
	 * Remove the rotate list wallpaper
	 * @param wallpaperPlaylist
	 * @return 1 if the row is deleted, 0 otherwise
	 */
	public int remove(WallpaperPlaylist wallpaperPlaylist) {
		return mDataBase.delete(TABLE, WALLPAPER_ID+" = "+ wallpaperPlaylist.getWallpaperId()+" AND "+PLAYLIST_ID+" = "+ wallpaperPlaylist.getPlaylistId(), null);
	}
	
	public int removeFromWallpaperId(int wallpaperId) {
		return mDataBase.delete(TABLE, WALLPAPER_ID+" = "+ wallpaperId, null);
	}
	
	public int removeFromPlaylistId(int rotateListId) {
		return mDataBase.delete(TABLE, PLAYLIST_ID+" = "+ rotateListId, null);
	}
	
	/**
	 * Get a ArrayList of Wallpaper from Cursor
	 * @param c Cursor
	 * @return If cursor not empty return ArrayList<Wallpaper>, null otherwise
	 */
	private ArrayList<Wallpaper> cursorToWallpapers(Cursor c) {
		if(c.moveToFirst() == false) return null;
		
		ArrayList<Wallpaper> wallpapersList = new ArrayList<Wallpaper>(c.getCount());
		mWallpapersDBAdapter.open();
			do {
				int id = c.getInt(0);
				wallpapersList.add(mWallpapersDBAdapter.getWallpaper(id));
			} while(c.moveToNext());
		mWallpapersDBAdapter.close();
		c.close();
		return wallpapersList;
	}
}
