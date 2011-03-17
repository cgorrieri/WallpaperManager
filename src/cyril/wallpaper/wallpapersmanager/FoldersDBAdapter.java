package cyril.wallpaper.wallpapersmanager;

import java.util.ArrayList;

import cyril.wallpaper.WMSQLiteOpenHelper;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class FoldersDBAdapter {

	private static final int VERSION = 1;
	
	public static final String TABLE = "folders";
	public static final String ID = "_id";
	public static final String NAME = "name";
	
	private SQLiteDatabase myDataBase;
	private WMSQLiteOpenHelper baseHelper;
	
	public FoldersDBAdapter(Context context) {
		baseHelper = new WMSQLiteOpenHelper(context, TABLE+".db", null, VERSION);
	}
	
	public void open() {
		myDataBase = baseHelper.getWritableDatabase();
	}
	
	public void close() {
		myDataBase.close();
	}
	
	public SQLiteDatabase getDataBase() {
		return myDataBase;
	}
	
	public Cursor getCursor(){
		return myDataBase.query(TABLE, new String[] {ID,NAME}, null, null, null, null, null);
	}

	public Folder getFolder(int e_id){
		Cursor c = myDataBase.query(TABLE, new String[] {ID,NAME}, ID+" = "+e_id+"", null, null, null, null);
		return cursorToFolder(c);
	}

	public ArrayList<Folder> getFolders(int e_folder_id){
		Cursor c = myDataBase.query(TABLE, new String[] {ID,NAME}, ID+" = "+e_folder_id+"", null, null, null, null);
		return cursorToFolders(c);
	}
	
	public long insertFolder(Folder fd) {
		ContentValues values = new ContentValues();
		values.put(NAME, fd.getName());
		return myDataBase.insert(TABLE, null, values);
	}
	
	public int updateFolder(Folder fd) {
		ContentValues values = new ContentValues();
		values.put(NAME, fd.getName());
		return myDataBase.update(TABLE, values, ID+" = "+fd.getId(), null);
	}
	
	public int updateFolder(ContentValues values, String where, String[] whereArgs) {
		return myDataBase.update(TABLE, values, where, whereArgs);
	}
	
	public int removeFolder(String name) {
		return myDataBase.delete(TABLE, NAME+" = "+ name, null);
	}
	
	public int removeFolder(int id) {
		return myDataBase.delete(TABLE, ID+" = "+ id, null);
	}
	
	public int removeFolder(Folder fd) {
		return myDataBase.delete(TABLE, ID+" = "+ fd.getId(), null);
	}
	
	private Folder cursorToFolder(Cursor c) {
		if(c.getCount() == 0) return null;
		c.moveToFirst();
		Folder fb = new Folder(c.getInt(0), c.getString(1));
		c.close();
		return fb;
	}
	private ArrayList<Folder> cursorToFolders(Cursor c) {
		if(c.getCount() == 0) return new ArrayList<Folder>(0);
		
		ArrayList<Folder> folders = new ArrayList<Folder>(c.getCount());
		c.moveToFirst();
		do{
			Folder fb = new Folder(c.getString(1));
			fb.setId(c.getInt(0));
			folders.add(fb);
		}while(c.moveToNext());
		c.close();
		return folders;
	}
}
