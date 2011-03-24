package com.wallpapers_manager.cyril;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import android.app.Service;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.preference.PreferenceManager;

import com.wallpapers_manager.cyril.rotate_lists.RotateList;
import com.wallpapers_manager.cyril.rotate_lists.RotateListSettingActivity;
import com.wallpapers_manager.cyril.rotate_lists.RotateListWallpapersDBAdapter;
import com.wallpapers_manager.cyril.rotate_lists.RotateListsDBAdapter;
import com.wallpapers_manager.cyril.wallpapers.Wallpaper;

public class RotateWallpaperService extends Service {
	private static Random _rand = new Random();
	
	private SharedPreferences 	mSharedPreferences;
	private Thread 				mThread;

	public void onCreate() {
		super.onCreate();
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		mThread = new Thread() {
			public void run() {
				WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
				RotateListWallpapersDBAdapter rotateListWallpaperDBAdapter = new RotateListWallpapersDBAdapter(
						getApplicationContext());
				RotateListsDBAdapter rotateListsDBAdapter = new RotateListsDBAdapter(
						getApplicationContext());
				Wallpaper oldWallpaper = new Wallpaper(-1, -1, "");
				Wallpaper currentWallpaper = null;
				ArrayList<Wallpaper> wallpapersList = null;
				RotateList rotateList = null;
				File registrationFilesDir = WallpaperManagerConstants._registrationFilesDir;
				while (true) {
					try {
						rotateListsDBAdapter.open();
						rotateList = rotateListsDBAdapter.getSelectedRotateList();
						rotateListsDBAdapter.close();
						if(rotateList == null) {
							// notification error
							break;
						}
							
						rotateListWallpaperDBAdapter.open();
						wallpapersList = rotateListWallpaperDBAdapter.getWallpapersFromRotateList(rotateList);
						rotateListWallpaperDBAdapter.close();
						
						if(wallpapersList.size() < 2) {
							// notification error
							break;
						}

						do {
							currentWallpaper = wallpapersList.get(_rand.nextInt(wallpapersList.size() - 1));
						} while (currentWallpaper.getId() == oldWallpaper.getId());
						oldWallpaper = currentWallpaper;

						File wallpaperFile = new File(registrationFilesDir,
								currentWallpaper.getAddress());
						Bitmap wallpaperBitmap = BitmapFactory
								.decodeFile(wallpaperFile.getAbsolutePath());

						wallpaperManager.setBitmap(wallpaperBitmap);
						sleep(getLongFromPrefs());
					} catch (IOException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			
			}
		};
	}

	public void onStart(Intent intent, int startId) {
		// timer pour lancer le thread au bon moment
		String result = mSharedPreferences.getString("rotate_time", RotateListSettingActivity.HOUR);
		if (result.compareTo(RotateListSettingActivity.DAY) == 0) {
			
		} else if (result.compareTo(RotateListSettingActivity.MINUTE) == 0) {
			
		} else {}
			
		mThread.start();
	}

	public void onDestroy() {
		mThread.stop();
	}

	private long getLongFromPrefs() {
		String result = mSharedPreferences.getString("rotate_time", RotateListSettingActivity.HOUR);
		if (result.compareTo(RotateListSettingActivity.DAY) == 0)
			return 60000 * 60 * 24;
		else if (result.compareTo(RotateListSettingActivity.MINUTE) == 0)
			return 60000 * 60;
		else
			return 60000;
	}

	public IBinder onBind(Intent arg0) {
		return null;
	}

}
