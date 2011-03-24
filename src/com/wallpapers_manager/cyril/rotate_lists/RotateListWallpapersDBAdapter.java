package com.wallpapers_manager.cyril.rotate_lists;

import java.util.ArrayList;

import com.wallpapers_manager.cyril.WMSQLiteOpenHelper;
import com.wallpapers_manager.cyril.wallpapers.Folder;
import com.wallpapers_manager.cyril.wallpapers.Wallpaper;
import com.wallpapers_manager.cyril.wallpapers.WallpapersDBAdapter;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RotateListWallpapersDBAdapter {

	private static final int VERSION = 1;
	
	private static final String 	TABLE = "rotate_list_wallpaper_assoc";
	private static final String 	ID = "_id";
	public 	static final int		ID_IC = 0;
	private static final String 	WALLPAPER_ID = "wallpaper_id";
	public 	static final int		WALLPAPER_ID_IC = 1;
	private static final String 	ROTATELIST_ID = "rotate_list_id";
	public 	static final int		ROTATELIST_ID_IC = 2;
	
	private SQLiteDatabase 			mDataBase;
	private WMSQLiteOpenHelper 		mBaseHelper;
	private WallpapersDBAdapter 	mWallpapersDBAdapter;
	
	public RotateListWallpapersDBAdapter(Context context) {
		mBaseHelper = new WMSQLiteOpenHelper(context, TABLE+".db", null, VERSION);
		mWallpapersDBAdapter = new WallpapersDBAdapter(context);
	}
	
	public void open() {
		mDataBase = mBaseHelper.getWritableDatabase();
		mWallpapersDBAdapter.open();
	}
	
	public void close() {
		mWallpapersDBAdapter.close();
		mDataBase.close();
	}
	
	public SQLiteDatabase getDataBase() {
		return mDataBase;
	}
	
	/**
	 * Get a Cursor for rotate_list_id
	 * @param e_rotate_list_id
	 * @return Cursor
	 */
	public Cursor getCursor(int rotateListId){
		Cursor c = mDataBase.query(TABLE, new String[] {ID,WALLPAPER_ID,ROTATELIST_ID}, ROTATELIST_ID+" = "+rotateListId+"", null, null, null, null);
		return c;
	}
	
	/**
	 * Test if a RotateListWallpaper already exist
	 * @param rotateListWallpaper
	 * @return true if exist, false otherwise
	 */
	public boolean rtlWppExist(RotateListWallpaper rotateListWallpaper) {
		Cursor c = mDataBase.query(TABLE, new String[] {ID,WALLPAPER_ID,ROTATELIST_ID}, ROTATELIST_ID+" = "+rotateListWallpaper.getRotateListId()+" AND "+WALLPAPER_ID+" = "+rotateListWallpaper.getWallpaperId(), null, null, null, null);
		int count = c.getCount();
		c.close();
		return count == 1;
	}

	/**
	 * Get a ArrayList of Wallpaper from RotateList
	 * @param rotateList
	 * @return ArrayList<Wallpaper>
	 */
	public ArrayList<Wallpaper> getWallpapersFromRotateList(RotateList rotateList){
		Cursor c = mDataBase.query(TABLE, new String[] {ID,WALLPAPER_ID,ROTATELIST_ID}, ROTATELIST_ID+" = "+rotateList.getId()+"", null, null, null, null);
		return cursorToWallpapers(c);
	}
	
	/**
	 * Insert a new RotateListWallpaper
	 * @param rotateListWallpaper
	 * @return id of the new RotateListWallpaper, -1 otherwise
	 */
	public long insertRotateListWallpaper(RotateListWallpaper rotateListWallpaper) {
		if(rotateListWallpaper.getRotateListId() == -1 || rotateListWallpaper.getWallpaperId() == -1 || rtlWppExist(rotateListWallpaper))
			return -1;
		
		ContentValues values = new ContentValues();
		values.put(WALLPAPER_ID, rotateListWallpaper.getWallpaperId());
		values.put(ROTATELIST_ID, rotateListWallpaper.getRotateListId());
		return mDataBase.insert(TABLE, null, values);
	}
	
	/**
	 * Insert the folder's content in the rotate list 
	 * @param folder
	 * @param rotateList
	 */
	public void insertRotateListWallpaperForFolder(Folder folder, RotateList rotateList) {
		ArrayList<Wallpaper> wallpapersList = mWallpapersDBAdapter.getWallpapersFromFolder(folder);
		RotateListWallpaper rotateListWallpaper = new RotateListWallpaper(-1,rotateList.getId());
		for(Wallpaper wallpaper : wallpapersList){
			rotateListWallpaper.setWallpaperId(wallpaper.getId());
			this.insertRotateListWallpaper(rotateListWallpaper);
		}
	}
	
	/**
	 * Remove the rotate list wallpaper
	 * @param rotateListWallpaper
	 * @return 1 if the row is deleted, 0 otherwise
	 */
	public int remove(RotateListWallpaper rotateListWallpaper) {
		return mDataBase.delete(TABLE, WALLPAPER_ID+" = "+ rotateListWallpaper.getWallpaperId()+" AND "+ROTATELIST_ID+" = "+ rotateListWallpaper.getRotateListId(), null);
	}
	
	public int removeFromWallpaperId(int wallpaperId) {
		return mDataBase.delete(TABLE, WALLPAPER_ID+" = "+ wallpaperId, null);
	}
	
	public int removeFromRotateListId(int rotateListId) {
		return mDataBase.delete(TABLE, ROTATELIST_ID+" = "+ rotateListId, null);
	}
	
	/**
	 * Get a ArrayList of Wallpaper from Cursor
	 * @param c
	 * @return ArrayList<Wallpaper>
	 */
	private ArrayList<Wallpaper> cursorToWallpapers(Cursor c) {
		if(c.getCount() == 0) return new ArrayList<Wallpaper>(0);
		
		ArrayList<Wallpaper> wallpapersList = new ArrayList<Wallpaper>(c.getCount());
		c.moveToFirst();
		do{
			wallpapersList.add(mWallpapersDBAdapter.getWallpaper(c.getInt(0)));
		}while(c.moveToNext());
		c.close();
		return wallpapersList;
	}
}
