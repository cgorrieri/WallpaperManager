package com.wallpapers_manager.cyril.bdd;

import com.wallpapers_manager.cyril.R;
import com.wallpapers_manager.cyril.R.string;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class WMSQLiteOpenHelper extends SQLiteOpenHelper {
	private Resources 	mResources;

	public WMSQLiteOpenHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		mResources = context.getResources();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE wallpapers (_id integer primary key autoincrement, folder_id integer NOT NULL, address varchar(22) NOT NULL);");
		db.execSQL("CREATE TABLE folders (_id integer primary key autoincrement, name varchar(22) NOT NULL);");
		db.execSQL("INSERT INTO folders VALUES(0, '"+mResources.getText(R.string.folder_name_for_first)+"');");
		db.execSQL("CREATE TABLE playlist (_id integer primary key autoincrement, name varchar(22) NOT NULL, selected integer DEFAULT 0);");
		db.execSQL("INSERT INTO playlist VALUES(0, '"+mResources.getText(R.string.playlist_name_for_first)+"', 0);");
		db.execSQL("CREATE TABLE playlist_wallpaper_assoc (_id integer primary key autoincrement, wallpaper_id integer NOT NULL, playlist_id integer NOT NULL);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE wallpapers;");
		db.execSQL("DROP TABLE folders;");
		db.execSQL("DROP TABLE playlist;");
		db.execSQL("DROP TABLE playlist_wallpaper_assoc;");
		onCreate(db);
	}

}
