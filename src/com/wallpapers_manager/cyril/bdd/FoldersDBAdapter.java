package com.wallpapers_manager.cyril.bdd;

import java.util.ArrayList;

import com.wallpapers_manager.cyril.data.Folder;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class FoldersDBAdapter {

	private static final int VERSION = 1;
	
	public static final String TABLE = "folders";
	public static final String ID = "_id";
	public static final String NAME = "name";
	
	private SQLiteDatabase mDataBase;
	private WMSQLiteOpenHelper mBaseHelper;
	
	public FoldersDBAdapter(Context context) {
		mBaseHelper = new WMSQLiteOpenHelper(context, TABLE+".db", null, VERSION);
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
		return mDataBase.query(TABLE, new String[] {ID,NAME}, null, null, null, null, null);
	}

	public Folder getFolder(int id){
		Cursor c = mDataBase.query(TABLE, new String[] {ID,NAME}, ID+" = "+id+"", null, null, null, null);
		return cursorToFolder(c);
	}

	public ArrayList<Folder> getFolders(){
		Cursor c = getCursor();
		return cursorToFolders(c);
	}
	
	public long insertFolder(Folder folder) {
		ContentValues values = new ContentValues();
		values.put(NAME, folder.getName());
		return mDataBase.insert(TABLE, null, values);
	}
	
	public int updateFolder(Folder folder) {
		ContentValues values = new ContentValues();
		values.put(NAME, folder.getName());
		return mDataBase.update(TABLE, values, ID+" = "+folder.getId(), null);
	}
	
	public int updateFolder(ContentValues values, String where, String[] whereArgs) {
		return mDataBase.update(TABLE, values, where, whereArgs);
	}
	
	public int removeFolder(String name) {
		return mDataBase.delete(TABLE, NAME+" = "+ name, null);
	}
	
	public int removeFolder(int id) {
		return mDataBase.delete(TABLE, ID+" = "+ id, null);
	}
	
	public int removeFolder(Folder folder) {
		return mDataBase.delete(TABLE, ID+" = "+ folder.getId(), null);
	}
	
	private Folder cursorToFolder(Cursor c) {
		if(c.getCount() == 0) return null;
		c.moveToFirst();
		Folder folder = new Folder(c.getInt(0), c.getString(1));
		c.close();
		return folder;
	}
	private ArrayList<Folder> cursorToFolders(Cursor c) {
		if(c.getCount() == 0) return new ArrayList<Folder>(0);
		
		ArrayList<Folder> folders = new ArrayList<Folder>(c.getCount());
		c.moveToFirst();
		do{
			folders.add(new Folder(c.getInt(0), c.getString(1)));
		}while(c.moveToNext());
		c.close();
		return folders;
	}
}
