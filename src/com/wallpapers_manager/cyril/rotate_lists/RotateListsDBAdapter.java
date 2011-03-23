package com.wallpapers_manager.cyril.rotate_lists;

import java.util.ArrayList;

import com.wallpapers_manager.cyril.WMSQLiteOpenHelper;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RotateListsDBAdapter {

	private static final int VERSION = 1;
	
	private static final String TABLE = "rotate_list";
	private static final String ID = "_id";
	private static final String NAME = "name";
	private static final String SELECTED = "selected";
	
	private SQLiteDatabase myDataBase;
	private WMSQLiteOpenHelper baseHelper;
	private RotateListWallpapersDBAdapter rtlWppDBA;
	
	public RotateListsDBAdapter(Context context) {
		baseHelper = new WMSQLiteOpenHelper(context, TABLE+".db", null, VERSION);
		rtlWppDBA = new RotateListWallpapersDBAdapter(context);
	}
	
	public void open() {
		myDataBase = baseHelper.getWritableDatabase();
		rtlWppDBA.open();
	}
	
	public void close() {
		rtlWppDBA.close();
		myDataBase.close();
	}
	
	public SQLiteDatabase getDataBase() {
		return myDataBase;
	}
	
	public Cursor getCursor(){
		return myDataBase.query(TABLE, new String[] {ID,NAME,SELECTED}, null, null, null, null, null);
	}
	
	public RotateList getRotateList(String e_name){
		Cursor c = myDataBase.query(TABLE, new String[] {ID,NAME,SELECTED}, NAME+" = '"+e_name+"'",null, null, null,  null);
		return cursorToRotateList(c);
	}
	
	public RotateList getSelectedRotateList(){
		Cursor c = myDataBase.query(TABLE, new String[] {ID,NAME,SELECTED}, SELECTED+" = 1", null, null, null, null);
		return cursorToRotateList(c);
	}

	public RotateList getRotateList(int e_id){
		Cursor c = myDataBase.query(TABLE, new String[] {ID,NAME,SELECTED}, ID+" = "+e_id+"", null, null, null, null);
		return cursorToRotateList(c);
	}

	public ArrayList<RotateList> getRotateLists(int e_folder_id){
		Cursor c = myDataBase.query(TABLE, new String[] {ID,NAME,SELECTED}, ID+" = "+e_folder_id+"", null, null, null, null);
		return cursorToRotateLists(c);
	}
	
	public long insertRotateList(RotateList rtl) {
		ContentValues values = new ContentValues();
		values.put(NAME, rtl.getName());
		return myDataBase.insert(TABLE, null, values);
	}
	
	public int updateRotateList(RotateList rtl) {
		ContentValues values = new ContentValues();
		values.put(ID, rtl.getId());
		values.put(NAME, rtl.getName());
		values.put(SELECTED, rtl.getSelected());
		return myDataBase.update(TABLE, values, ID+" = "+rtl.getId(), null);
	}
	
	public int updateRotateList(ContentValues values, String where, String[] whereArgs) {
		return myDataBase.update(TABLE, values, where, whereArgs);
	}
	
	public void removeRotateList(int id) {
		myDataBase.delete(TABLE, ID+" = "+ id, null);
		rtlWppDBA.removeFromRotateListId(id);
	}
	
	public void removeRotateList(RotateList rtl) {
		this.removeRotateList(rtl.getId());
	}
	
	private RotateList cursorToRotateList(Cursor c) {
		if(c.getCount() == 0) {
			c.close();
			return null;
		}
		c.moveToFirst();
		RotateList rtl = new RotateList(c.getInt(0), c.getString(1), c.getInt(2));
		c.close();
		return rtl;
	}
	private ArrayList<RotateList> cursorToRotateLists(Cursor c) {
		if(c.getCount() == 0){
			c.close();
			return new ArrayList<RotateList>(0);
		}
		
		ArrayList<RotateList> rotate_lists = new ArrayList<RotateList>(c.getCount());
		c.moveToFirst();
		do{
			rotate_lists.add(new RotateList(c.getInt(0), c.getString(1)));
		}while(c.moveToNext());
		c.close();
		return rotate_lists;
	}
}
