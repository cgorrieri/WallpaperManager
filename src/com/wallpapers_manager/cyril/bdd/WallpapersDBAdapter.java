package com.wallpapers_manager.cyril.bdd;

import java.util.ArrayList;

import com.wallpapers_manager.cyril.data.Folder;
import com.wallpapers_manager.cyril.data.Wallpaper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class WallpapersDBAdapter extends AbstractDBAdapter {
	private static final String 	TABLE = "wallpapers";
	private static final String 	FOLDER_ID = "folder_id";
	private static final String 	ADDRESS = "address";
	
	private Context 			mContext;
	
	public WallpapersDBAdapter(Context context) {
		super(context);
		mContext = context;
	}
	
	@Override
	public String table() {
		return TABLE;
	}

	@Override
	public String[] columns() {
		return new String[] {ID,FOLDER_ID,ADDRESS};
	}
	
	public Cursor getCursor(){
		return select(null);
	}
	
	public Cursor getCursor(int folderId){
		return select(FOLDER_ID+" = "+folderId);
	}
	
	public Wallpaper getWallpaper(String address){
		return cursorToWallpaper(select(ADDRESS+" = '"+address));
	}

	public Wallpaper getWallpaper(int id){
		return cursorToWallpaper(select(ID+" = "+id));
	}

	public ArrayList<Wallpaper> getWallpapersFromFolder(Folder folder){
		return cursorToWallpapers(select(FOLDER_ID+" = "+folder.getId()));
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
		return delete(ADDRESS+" = "+ address);
	}
	
	public int removeWallpaper(int id) {
		WallpapersPlaylistDBAdapter wallpapersPlaylistDBAdapter = new WallpapersPlaylistDBAdapter(mContext);
		wallpapersPlaylistDBAdapter.open();
			wallpapersPlaylistDBAdapter.removeFromWallpaperId(id);
		wallpapersPlaylistDBAdapter.close();
		return delete(ID+" = "+ id);
	}
	
	public int removeWallpaperFromFolder(Folder folder) {
		ArrayList<Wallpaper> wallpapersList = getWallpapersFromFolder(folder);
		int result = 0;
		for(Wallpaper wallpaper : wallpapersList) {
			result += removeWallpaper(wallpaper.getId());
		}
		return delete(FOLDER_ID+" = "+ folder.getId());
	}
	
	private Wallpaper cursorToWallpaper(Cursor cursor) {
		if(cursor.moveToFirst() == false) return null;
		Wallpaper w = new Wallpaper(cursor);
		cursor.close();
		return w;
	}
	private ArrayList<Wallpaper> cursorToWallpapers(Cursor cursor) {
		if(cursor.moveToFirst() == false) return new ArrayList<Wallpaper>(0);
		
		ArrayList<Wallpaper> wallpapersList = new ArrayList<Wallpaper>(cursor.getCount());
		do {
			wallpapersList.add(new Wallpaper(cursor));
		} while(cursor.moveToNext());
		cursor.close();
		return wallpapersList;
	}
}
