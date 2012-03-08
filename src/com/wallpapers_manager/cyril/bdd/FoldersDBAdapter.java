package com.wallpapers_manager.cyril.bdd;

import java.nio.channels.SelectableChannel;
import java.util.ArrayList;

import com.wallpapers_manager.cyril.data.Folder;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class FoldersDBAdapter extends AbstractDBAdapter {
	public static final String TABLE = "folders";
	public static final String NAME = "name";
	
	public FoldersDBAdapter(Context context) {
		super(context);
	}
	
	@Override
	public String table() {
		return TABLE;
	}

	@Override
	public String[] columns() {
		return new String[] {ID,NAME};
	}

	public Folder getFolder(int id){
		return cursorToFolder(select(ID+" = "+id));
	}

	public ArrayList<Folder> getFolders(){
		return cursorToFolders(getCursor());
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
		return delete(NAME+" = "+ name);
	}
	
	public int removeFolder(int id) {
		return delete(ID+" = "+ id);
	}
	
	public int removeFolder(Folder folder) {
		return delete(ID+" = "+ folder.getId());
	}
	
	private Folder cursorToFolder(Cursor cursor) {
		if(cursor.moveToFirst() == false) return null;
		Folder folder = new Folder(cursor);
		cursor.close();
		return folder;
	}
	private ArrayList<Folder> cursorToFolders(Cursor cursor) {
		if(cursor.moveToFirst() == false) return new ArrayList<Folder>(0);
		
		ArrayList<Folder> folders = new ArrayList<Folder>(cursor.getCount());
		do{
			folders.add(new Folder(cursor));
		}while(cursor.moveToNext());
		cursor.close();
		return folders;
	}
}
