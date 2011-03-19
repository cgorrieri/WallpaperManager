package cyril.wallpaper.rotatelistsmanager;

import java.util.ArrayList;

import cyril.wallpaper.WMSQLiteOpenHelper;
import cyril.wallpaper.wallpapersmanager.Folder;
import cyril.wallpaper.wallpapersmanager.Wallpaper;
import cyril.wallpaper.wallpapersmanager.WallpapersDBAdapter;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RotateListWallpapersDBAdapter {

	private static final int VERSION = 1;
	
	private static final String TABLE = "rotate_list_wallpaper_assoc";
	private static final String ID = "_id";
	private static final String WALLPAPER_ID = "wallpaper_id";
	private static final String ROTATELIST_ID = "rotate_list_id";
	
	private SQLiteDatabase myDataBase;
	private WMSQLiteOpenHelper baseHelper;
	private WallpapersDBAdapter wppDBA;
	
	public RotateListWallpapersDBAdapter(Context context) {
		baseHelper = new WMSQLiteOpenHelper(context, TABLE+".db", null, VERSION);
		wppDBA = new WallpapersDBAdapter(context);
	}
	
	public void open() {
		myDataBase = baseHelper.getWritableDatabase();
		wppDBA.open();
	}
	
	public void close() {
		wppDBA.close();
		myDataBase.close();
	}
	
	public SQLiteDatabase getDataBase() {
		return myDataBase;
	}
	
	/**
	 * Get a Cursor for rotate_list_id
	 * @param e_rotate_list_id
	 * @return Cursor
	 */
	public Cursor getCursor(int e_rotate_list_id){
		Cursor c = myDataBase.query(TABLE, new String[] {ID,WALLPAPER_ID,ROTATELIST_ID}, ROTATELIST_ID+" = "+e_rotate_list_id+"", null, null, null, null);
		return c;
	}
	
	/**
	 * Test if a RotateListWallpaper already exist
	 * @param rtlWpp
	 * @return true if exist, false otherwise
	 */
	public boolean rtlWppExist(RotateListWallpaper rtlWpp) {
		Cursor c = myDataBase.query(TABLE, new String[] {ID,WALLPAPER_ID,ROTATELIST_ID}, ROTATELIST_ID+" = "+rtlWpp.getRtl_id()+" AND "+WALLPAPER_ID+" = "+rtlWpp.getWpp_id(), null, null, null, null);
		int count = c.getCount();
		c.close();
		return count == 1;
	}

	/**
	 * Get a ArrayList of Wallpaper from RotateList
	 * @param rtl
	 * @return ArrayList<Wallpaper>
	 */
	public ArrayList<Wallpaper> getWallpapersFromRotateList(RotateList rtl){
		Cursor c = myDataBase.query(TABLE, new String[] {ID,WALLPAPER_ID,ROTATELIST_ID}, ROTATELIST_ID+" = "+rtl.getId()+"", null, null, null, null);
		return cursorToWallpapers(c);
	}
	
	/**
	 * Insert a new RotateListWallpaper
	 * @param rtlWpp
	 * @return id of the new RotateListWallpaper, -1 otherwise
	 */
	public long insertRotateListWallpaper(RotateListWallpaper rtlWpp) {
		if(rtlWpp.getRtl_id() == -1 || rtlWpp.getWpp_id() == -1 || rtlWppExist(rtlWpp))
			return -1;
		
		ContentValues values = new ContentValues();
		values.put(WALLPAPER_ID, rtlWpp.getWpp_id());
		values.put(ROTATELIST_ID, rtlWpp.getRtl_id());
		return myDataBase.insert(TABLE, null, values);
	}
	
	/**
	 * Insert the folder's content in the rotate list 
	 * @param fd
	 * @param rtl
	 */
	public void insertRotateListWallpaperForFolder(Folder fd, RotateList rtl) {
		ArrayList<Wallpaper> wpps = wppDBA.getWallpapersFromFolder(fd);
		RotateListWallpaper rtlWpp = new RotateListWallpaper(-1,rtl.getId());
		for(Wallpaper wpp : wpps){
			rtlWpp.setWpp_id(wpp.getId());
			this.insertRotateListWallpaper(rtlWpp);
		}
	}
	
	/**
	 * Remove the rotate list wallpaper
	 * @param rtlWpp
	 * @return 1 if the row is deleted, 0 otherwise
	 */
	public int remove(RotateListWallpaper rtlWpp) {
		return myDataBase.delete(TABLE, WALLPAPER_ID+" = "+ rtlWpp.getWpp_id()+" AND "+ROTATELIST_ID+" = "+ rtlWpp.getRtl_id(), null);
	}
	
	public int removeFromWallpaperId(int wpp_id) {
		return myDataBase.delete(TABLE, WALLPAPER_ID+" = "+ wpp_id, null);
	}
	
	public int removeFromRotateListId(int rtl_id) {
		return myDataBase.delete(TABLE, ROTATELIST_ID+" = "+ rtl_id, null);
	}
	
	/**
	 * Get a ArrayList of Wallpaper from Cursor
	 * @param c
	 * @return ArrayList<Wallpaper>
	 */
	private ArrayList<Wallpaper> cursorToWallpapers(Cursor c) {
		if(c.getCount() == 0) return new ArrayList<Wallpaper>(0);
		
		ArrayList<Wallpaper> ws = new ArrayList<Wallpaper>(c.getCount());
		c.moveToFirst();
		do{
			ws.add(wppDBA.getWallpaper(c.getInt(0)));
		}while(c.moveToNext());
		c.close();
		return ws;
	}
}
