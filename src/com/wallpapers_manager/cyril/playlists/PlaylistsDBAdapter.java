package com.wallpapers_manager.cyril.playlists;

import java.util.ArrayList;

import com.wallpapers_manager.cyril.WMSQLiteOpenHelper;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PlaylistsDBAdapter {

	private static final int 		VERSION = 1;
	
	private static final String 	TABLE = "playlist";
	private static final String 	ID = "_id";
	public static final int 		ID_IC = 0;
	private static final String 	NAME = "name";
	public static final int 		NAME_IC = 1;
	private static final String 	SELECTED = "selected";
	public static final int 		SELECTED_IC = 2;
	
	private SQLiteDatabase 					mDataBase;
	private WMSQLiteOpenHelper 				mBaseHelper;
	private WallpapersPlaylistDBAdapter 	mPlaylistWallpaperDBAdapter;
	
	public PlaylistsDBAdapter(Context context) {
		mBaseHelper = new WMSQLiteOpenHelper(context, TABLE+".db", null, VERSION);
		mPlaylistWallpaperDBAdapter = new WallpapersPlaylistDBAdapter(context);
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
		return mDataBase.query(TABLE, new String[] {ID,NAME,SELECTED}, null, null, null, null, null);
	}
	
	public Playlist getPlaylist(String name){
		Cursor c = mDataBase.query(TABLE, new String[] {ID,NAME,SELECTED}, NAME+" = '"+name+"'",null, null, null,  null);
		return cursorToPlaylist(c);
	}
	
	public Playlist getSelectedPlaylist(){
		Cursor c = mDataBase.query(TABLE, new String[] {ID,NAME,SELECTED}, SELECTED+" = 1", null, null, null, null);
		return cursorToPlaylist(c);
	}

	public Playlist getPlaylist(int id){
		Cursor c = mDataBase.query(TABLE, new String[] {ID,NAME,SELECTED}, ID+" = "+id+"", null, null, null, null);
		return cursorToPlaylist(c);
	}

	public ArrayList<Playlist> getPlaylists(int folderId){
		Cursor c = mDataBase.query(TABLE, new String[] {ID,NAME,SELECTED}, ID+" = "+folderId+"", null, null, null, null);
		return cursorToPlaylists(c);
	}
	
	public long insertPlaylist(Playlist playlist) {
		ContentValues values = new ContentValues();
		values.put(NAME, playlist.getName());
		return mDataBase.insert(TABLE, null, values);
	}
	
	public int updatePlaylist(Playlist playlist) {
		ContentValues values = new ContentValues();
		values.put(ID, playlist.getId());
		values.put(NAME, playlist.getName());
		values.put(SELECTED, playlist.getSelected());
		return mDataBase.update(TABLE, values, ID+" = "+playlist.getId(), null);
	}
	
	public int updatePlaylist(ContentValues values, String where, String[] whereArgs) {
		return mDataBase.update(TABLE, values, where, whereArgs);
	}
	
	public void removePlaylist(int id) {
		mDataBase.delete(TABLE, ID+" = "+ id, null);
		mPlaylistWallpaperDBAdapter.open();
			mPlaylistWallpaperDBAdapter.removeFromPlaylistId(id);
		mPlaylistWallpaperDBAdapter.close();
	}
	
	public void removePlaylist(Playlist playlist) {
		this.removePlaylist(playlist.getId());
	}
	
	private Playlist cursorToPlaylist(Cursor cursor) {
		if(cursor.getCount() == 0) {
			cursor.close();
			return null;
		}
		cursor.moveToFirst();
		Playlist playlist = new Playlist(cursor.getInt(ID_IC), cursor.getString(NAME_IC), cursor.getInt(SELECTED_IC));
		cursor.close();
		return playlist;
	}
	private ArrayList<Playlist> cursorToPlaylists(Cursor cursor) {
		if(cursor.getCount() == 0){
			cursor.close();
			return new ArrayList<Playlist>(0);
		}
		
		ArrayList<Playlist> playlists = new ArrayList<Playlist>(cursor.getCount());
		cursor.moveToFirst();
		do{
			playlists.add(new Playlist(cursor.getInt(ID_IC), cursor.getString(NAME_IC), cursor.getInt(SELECTED_IC)));
		}while(cursor.moveToNext());
		cursor.close();
		return playlists;
	}
}
