package com.wallpapers_manager.cyril.bdd;

import java.util.ArrayList;

import com.wallpapers_manager.cyril.data.Playlist;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PlaylistsDBAdapter extends AbstractDBAdapter {
	
	private static final String 	TABLE = "playlist";
	private static final String 	NAME = "name";
	private static final String 	SELECTED = "selected";
	
	private WallpapersPlaylistDBAdapter 	mPlaylistWallpaperDBAdapter;
	
	public PlaylistsDBAdapter(Context context) {
		super(context);
		mPlaylistWallpaperDBAdapter = new WallpapersPlaylistDBAdapter(context);
	}
	
	@Override
	public String table() {
		return TABLE;
	}

	@Override
	public String[] columns() {
		return new String[] {ID,NAME,SELECTED};
	}
	
	public Playlist getPlaylist(String name){
		return cursorToPlaylist(select(NAME+" = '"+name));
	}
	
	public Playlist getSelectedPlaylist(){
		return cursorToPlaylist(select(SELECTED+" = 1"));
	}

	public Playlist getPlaylist(int id){
		return cursorToPlaylist(select(ID+" = "+id));
	}

	public ArrayList<Playlist> getPlaylists(){
		return cursorToPlaylists(select(null));
	}
	
	public ArrayList<Playlist> getPlaylists(int folderId){
		return cursorToPlaylists(select(ID+" = "+folderId));
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
		delete(ID+" = "+ id);
		mPlaylistWallpaperDBAdapter.open();
			mPlaylistWallpaperDBAdapter.removeFromPlaylistId(id);
		mPlaylistWallpaperDBAdapter.close();
	}
	
	public void removePlaylist(Playlist playlist) {
		this.removePlaylist(playlist.getId());
	}
	
	private Playlist cursorToPlaylist(Cursor cursor) {
		if(cursor.moveToFirst() == false) {
			cursor.close();
			return null;
		}
		Playlist playlist = new Playlist(cursor);
		cursor.close();
		return playlist;
	}
	
	private ArrayList<Playlist> cursorToPlaylists(Cursor cursor) {
		if(cursor.moveToFirst() == false){
			cursor.close();
			return new ArrayList<Playlist>(0);
		}
		
		ArrayList<Playlist> playlists = new ArrayList<Playlist>(cursor.getCount());
		do{
			playlists.add(new Playlist(cursor));
		}while(cursor.moveToNext());
		cursor.close();
		return playlists;
	}
}
