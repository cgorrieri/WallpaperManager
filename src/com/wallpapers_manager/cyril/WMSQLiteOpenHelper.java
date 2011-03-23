package com.wallpapers_manager.cyril;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class WMSQLiteOpenHelper extends SQLiteOpenHelper {

	public WMSQLiteOpenHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE wallpapers (_id integer primary key autoincrement, folder_id integer NOT NULL, address varchar(22) NOT NULL);");
		db.execSQL("CREATE TABLE folders (_id integer primary key autoincrement, name varchar(22) NOT NULL);");
		db.execSQL("INSERT INTO folders VALUES(0, 'My folder');");
		db.execSQL("CREATE TABLE rotate_list (_id integer primary key autoincrement, name varchar(22) NOT NULL, selected integer DEFAULT 0);");
		db.execSQL("INSERT INTO rotate_list VALUES(0, 'My rotate list', 0);");
		db.execSQL("CREATE TABLE rotate_list_wallpaper_assoc (_id integer primary key autoincrement, wallpaper_id integer NOT NULL, rotate_list_id integer NOT NULL);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE wallpapers;");
		db.execSQL("DROP TABLE folders;");
		db.execSQL("DROP TABLE rotate_list;");
		db.execSQL("DROP TABLE rotate_list_wallpaper_assoc;");
		onCreate(db);
	}

}
