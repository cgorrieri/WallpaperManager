package com.wallpapers_manager.cyril.wallpapers;

import java.util.ArrayList;

import com.wallpapers_manager.cyril.WMSQLiteOpenHelper;
import com.wallpapers_manager.cyril.folders.Folder;
import com.wallpapers_manager.cyril.wallpapers_playlist.WallpapersPlaylistDBAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class WallpapersDBAdapter {

	private static final int VERSION = 1;
	
	private static final String 	TABLE = "wallpapers";
	private static final String 	ID = "_id";
	public static final int 		ID_IC = 0;
	private static final String 	FOLDER_ID = "folder_id";
	public static final int 		FOLDER_ID_IC = 1;
	private static final String 	ADDRESS = "address";
	public static final int 		ADDRESS_IC = 2;
	
	private Context 			mContext;
	private SQLiteDatabase 		mDataBase;
	private WMSQLiteOpenHelper 	mBaseHelper;
	
	public WallpapersDBAdapter(Context context) {
		mBaseHelper = new WMSQLiteOpenHelper(context, TABLE+".db", null, VERSION);
		mContext = context;
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
	
	public Cursor getCursor(){
		return mDataBase.query(TABLE, new String[] {ID,FOLDER_ID,ADDRESS}, null, null, null, null, null);
	}
	
	public Cursor getCursor(int folderId){
		return mDataBase.query(TABLE, new String[] {ID,FOLDER_ID,ADDRESS}, FOLDER_ID+" = "+folderId+"", null, null, null, null);
	}
	
	public Wallpaper getWallpaper(String address){
		Cursor c = mDataBase.query(TABLE, new String[] {ID,FOLDER_ID,ADDRESS}, null, null, null, ADDRESS+" = '"+address+"'", null);
		return cursorToWallpaper(c);
	}

	public Wallpaper getWallpaper(int id){
		Cursor c = mDataBase.query(TABLE, new String[] {ID,FOLDER_ID,ADDRESS}, ID+" = "+id+"", null, null, null, null);
		return cursorToWallpaper(c);
	}

	public ArrayList<Wallpaper> getWallpapersFromFolder(Folder folder){
		Cursor c = mDataBase.query(TABLE, new String[] {ID,FOLDER_ID,ADDRESS}, FOLDER_ID+" = "+folder.getId()+"", null, null, null, null);
		return cursorToWallpapers(c);
	}
	
	public ArrayList<Wallpaper> getWallpapers(){
		return cursorToWallpapers(this.getCursor());
	}
	
	public long insertWallpaper(Wallpaper wallpaper) {
		ContentValues values = new ContentValues();
		values.put(FOLDER_ID, wallpaper.getFolderId());
		values.put(ADDRESS, wallpaper.getAddress());
		return mDataBase.insert(TABLE, null, values);
	}
	
	public int updateWallpaper(Wallpaper wallpaper) {
		ContentValues values = new ContentValues();
		values.put(FOLDER_ID, wallpaper.getFolderId());
		values.put(ADDRESS, wallpaper.getAddress());
		return mDataBase.update(TABLE, values, ID+" = "+wallpaper.getId(), null);
	}
	
	public int updateWallpaper(ContentValues values, String where, String[] whereArgs) {
		return mDataBase.update(TABLE, values, where, whereArgs);
	}
	
	public int removeWallpaper(String address) {
		return mDataBase.delete(TABLE, ADDRESS+" = "+ address, null);
	}
	
	public int removeWallpaper(int id) {
		WallpapersPlaylistDBAdapter wallpapersPlaylistDBAdapter = new WallpapersPlaylistDBAdapter(mContext);
		wallpapersPlaylistDBAdapter.open();
			wallpapersPlaylistDBAdapter.removeFromWallpaperId(id);
		wallpapersPlaylistDBAdapter.close();
		return mDataBase.delete(TABLE, ID+" = "+ id, null);
	}
	
	public int removeWallpaperFromFolder(Folder folder) {
		ArrayList<Wallpaper> wallpapersList = getWallpapersFromFolder(folder);
		int result = 0;
		for(Wallpaper wallpaper : wallpapersList) {
			result += removeWallpaper(wallpaper.getId());
		}
		return mDataBase.delete(TABLE, FOLDER_ID+" = "+ folder.getId(), null);
	}
	
	private Wallpaper cursorToWallpaper(Cursor c) {
		if(c.moveToFirst() == false) return null;
		Wallpaper w = new Wallpaper(c.getInt(ID_IC), c.getInt(FOLDER_ID_IC), c.getString(ADDRESS_IC));
		c.close();
		return w;
	}
	private ArrayList<Wallpaper> cursorToWallpapers(Cursor cursor) {
		if(cursor.getCount() == 0) return new ArrayList<Wallpaper>(0);
		
		ArrayList<Wallpaper> wallpapersList = new ArrayList<Wallpaper>(cursor.getCount());
		cursor.moveToFirst();
		do {
			wallpapersList.add(new Wallpaper(cursor));
		} while(cursor.moveToNext());
		cursor.close();
		return wallpapersList;
	}
}
