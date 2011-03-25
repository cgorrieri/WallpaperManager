package com.wallpapers_manager.cyril.rotate_lists;

import java.util.ArrayList;

import com.wallpapers_manager.cyril.WMSQLiteOpenHelper;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RotateListsDBAdapter {

	private static final int 		VERSION = 1;
	
	private static final String 	TABLE = "rotate_list";
	private static final String 	ID = "_id";
	public static final int 		ID_IC = 0;
	private static final String 	NAME = "name";
	public static final int 		NAME_IC = 1;
	private static final String 	SELECTED = "selected";
	public static final int 		SELECTED_IC = 2;
	
	private SQLiteDatabase 					mDataBase;
	private WMSQLiteOpenHelper 				mBaseHelper;
	private RotateListWallpapersDBAdapter 	mRotateListWallpaperDBAdapter;
	
	public RotateListsDBAdapter(Context context) {
		mBaseHelper = new WMSQLiteOpenHelper(context, TABLE+".db", null, VERSION);
		mRotateListWallpaperDBAdapter = new RotateListWallpapersDBAdapter(context);
	}
	
	public void open() {
		mDataBase = mBaseHelper.getWritableDatabase();
		mRotateListWallpaperDBAdapter.open();
	}
	
	public void close() {
		mRotateListWallpaperDBAdapter.close();
		mDataBase.close();
	}
	
	public SQLiteDatabase getDataBase() {
		return mDataBase;
	}
	
	public Cursor getCursor(){
		return mDataBase.query(TABLE, new String[] {ID,NAME,SELECTED}, null, null, null, null, null);
	}
	
	public RotateList getRotateList(String name){
		Cursor c = mDataBase.query(TABLE, new String[] {ID,NAME,SELECTED}, NAME+" = '"+name+"'",null, null, null,  null);
		return cursorToRotateList(c);
	}
	
	public RotateList getSelectedRotateList(){
		Cursor c = mDataBase.query(TABLE, new String[] {ID,NAME,SELECTED}, SELECTED+" = 1", null, null, null, null);
		return cursorToRotateList(c);
	}

	public RotateList getRotateList(int id){
		Cursor c = mDataBase.query(TABLE, new String[] {ID,NAME,SELECTED}, ID+" = "+id+"", null, null, null, null);
		return cursorToRotateList(c);
	}

	public ArrayList<RotateList> getRotateLists(int folderId){
		Cursor c = mDataBase.query(TABLE, new String[] {ID,NAME,SELECTED}, ID+" = "+folderId+"", null, null, null, null);
		return cursorToRotateLists(c);
	}
	
	public long insertRotateList(RotateList rotateList) {
		ContentValues values = new ContentValues();
		values.put(NAME, rotateList.getName());
		return mDataBase.insert(TABLE, null, values);
	}
	
	public int updateRotateList(RotateList rotateList) {
		ContentValues values = new ContentValues();
		values.put(ID, rotateList.getId());
		values.put(NAME, rotateList.getName());
		values.put(SELECTED, rotateList.getSelected());
		return mDataBase.update(TABLE, values, ID+" = "+rotateList.getId(), null);
	}
	
	public int updateRotateList(ContentValues values, String where, String[] whereArgs) {
		return mDataBase.update(TABLE, values, where, whereArgs);
	}
	
	public void removeRotateList(int id) {
		mDataBase.delete(TABLE, ID+" = "+ id, null);
		mRotateListWallpaperDBAdapter.removeFromRotateListId(id);
	}
	
	public void removeRotateList(RotateList rotateList) {
		this.removeRotateList(rotateList.getId());
	}
	
	private RotateList cursorToRotateList(Cursor cursor) {
		if(cursor.getCount() == 0) {
			cursor.close();
			return null;
		}
		cursor.moveToFirst();
		RotateList rotateList = new RotateList(cursor.getInt(ID_IC), cursor.getString(NAME_IC), cursor.getInt(SELECTED_IC));
		cursor.close();
		return rotateList;
	}
	private ArrayList<RotateList> cursorToRotateLists(Cursor cursor) {
		if(cursor.getCount() == 0){
			cursor.close();
			return new ArrayList<RotateList>(0);
		}
		
		ArrayList<RotateList> rotateLists = new ArrayList<RotateList>(cursor.getCount());
		cursor.moveToFirst();
		do{
			rotateLists.add(new RotateList(cursor.getInt(ID_IC), cursor.getString(NAME_IC), cursor.getInt(SELECTED_IC)));
		}while(cursor.moveToNext());
		cursor.close();
		return rotateLists;
	}
}
