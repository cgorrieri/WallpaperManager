package com.wallpapers_manager.cyril.bdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public abstract class AbstractDBAdapter {
	private static final int VERSION = 1;
	protected static final String ID = "_id";

	protected SQLiteDatabase mDataBase;
	protected WMSQLiteOpenHelper mBaseHelper;

	public AbstractDBAdapter(Context context) {
		mBaseHelper = new WMSQLiteOpenHelper(context, "database.db", null, VERSION);
	}

	/**
	 * Return the name of the table
	 * 
	 * @return
	 */
	public abstract String table();

	public abstract String[] columns();

	public void open() {
		mDataBase = mBaseHelper.getWritableDatabase();
	}

	public void close() {
		mDataBase.close();
	}

	public SQLiteDatabase getDataBase() {
		return mDataBase;
	}

	protected Cursor select(String condition) {
		return mDataBase.query(table(), columns(), condition, null, null, null, null);
	}
	
	protected long insert(ContentValues values) {
		return mDataBase.insert(table(), null, values);
	}
	
	protected int update(String where, ContentValues values) {
		return mDataBase.update(table(), values, where, null);
	}

	protected int delete(String condition) {
		return mDataBase.delete(table(), condition, null);
	}

	/**
	 * Get a Cursor for playlist_id
	 * 
	 * @param e_playlist_id
	 * @return Cursor
	 */
	public Cursor getCursor() {
		return select(null);
	}
}
