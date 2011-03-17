package cyril.wallpaper.wallpapersmanager;

import java.util.ArrayList;

import cyril.wallpaper.WMSQLiteOpenHelper;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class WallpapersDBAdapter {

	private static final int VERSION = 1;
	
	private static final String TABLE = "wallpapers";
	private static final String ID = "_id";
	private static final String FOLDER = "folder_id";
	private static final String ADDRESS = "address";
	
	private SQLiteDatabase myDataBase;
	private WMSQLiteOpenHelper baseHelper;
	
	public WallpapersDBAdapter(Context context) {
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
		return myDataBase.query(TABLE, new String[] {ID,FOLDER,ADDRESS}, null, null, null, null, null);
	}
	
	public Cursor getCursor(int e_folder_id){
		return myDataBase.query(TABLE, new String[] {ID,FOLDER,ADDRESS}, FOLDER+" = "+e_folder_id+"", null, null, null, null);
	}
	
	public Wallpaper getWallpaper(String e_address){
		Cursor c = myDataBase.query(TABLE, new String[] {ID,FOLDER,ADDRESS}, null, null, null, ADDRESS+" = '"+e_address+"'", null);
		return cursorToWallpaper(c);
	}

	public Wallpaper getWallpaper(int e_id){
		Cursor c = myDataBase.query(TABLE, new String[] {ID,FOLDER,ADDRESS}, ID+" = "+e_id+"", null, null, null, null);
		return cursorToWallpaper(c);
	}

	public ArrayList<Wallpaper> getWallpapersFromFolder(Folder fd){
		Cursor c = myDataBase.query(TABLE, new String[] {ID,FOLDER,ADDRESS}, FOLDER+" = "+fd.getId()+"", null, null, null, null);
		return cursorToWallpapers(c);
	}
	
	public ArrayList<Wallpaper> getWallpapers(){
		return cursorToWallpapers(this.getCursor());
	}
	
	public long insertWallpaper(Wallpaper wpp) {
		ContentValues values = new ContentValues();
		values.put(FOLDER, wpp.getFolderId());
		values.put(ADDRESS, wpp.getAddress());
		return myDataBase.insert(TABLE, null, values);
	}
	
	public int updateWallpaper(Wallpaper wpp) {
		ContentValues values = new ContentValues();
		values.put(FOLDER, wpp.getFolderId());
		values.put(ADDRESS, wpp.getAddress());
		return myDataBase.update(TABLE, values, ID+" = "+wpp.getId(), null);
	}
	
	public int updateWallpaper(ContentValues values, String where, String[] whereArgs) {
		return myDataBase.update(TABLE, values, where, whereArgs);
	}
	
	public int removeWallpaper(String address) {
		return myDataBase.delete(TABLE, ADDRESS+" = "+ address, null);
	}
	
	public int removeWallpaper(int id) {
		return myDataBase.delete(TABLE, ID+" = "+ id, null);
	}
	
	public int removeWallpaperFromFolder(Folder fd) {
		return myDataBase.delete(TABLE, FOLDER+" = "+ fd.getId(), null);
	}
	
	private Wallpaper cursorToWallpaper(Cursor c) {
		if(c.getCount() == 0) return null;
		c.moveToFirst();
		Wallpaper w = new Wallpaper(c.getInt(0), c.getInt(1), c.getString(2));
		c.close();
		return w;
	}
	private ArrayList<Wallpaper> cursorToWallpapers(Cursor c) {
		if(c.getCount() == 0) return new ArrayList<Wallpaper>(0);
		
		ArrayList<Wallpaper> ws = new ArrayList<Wallpaper>(c.getCount());
		c.moveToFirst();
		do{
			ws.add(new Wallpaper(c.getInt(0), c.getInt(1), c.getString(2)));
		}while(c.moveToNext());
		c.close();
		return ws;
	}
}
