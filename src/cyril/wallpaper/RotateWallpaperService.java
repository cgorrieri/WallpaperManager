package cyril.wallpaper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import cyril.wallpaper.rotatelistsmanager.RotateList;
import cyril.wallpaper.rotatelistsmanager.RotateListWallpapersDBAdapter;
import cyril.wallpaper.rotatelistsmanager.RotateListsDBAdapter;
import cyril.wallpaper.wallpapersmanager.Wallpaper;

import android.app.Service;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.preference.PreferenceManager;

public class RotateWallpaperService extends Service {
	private static Random rand = new Random();
	private SharedPreferences prefs;
	private Thread th;

	public void onCreate() {
		super.onCreate();
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		th = new Thread() {
			public void run() {
				WallpaperManager wallpaperManager = WallpaperManager
						.getInstance(getApplicationContext());
				RotateListWallpapersDBAdapter rtlWppDBA = new RotateListWallpapersDBAdapter(
						getApplicationContext());
				RotateListsDBAdapter rtlDBA = new RotateListsDBAdapter(
						getApplicationContext());
				Wallpaper old_wpp = new Wallpaper(-1, -1, "");
				Wallpaper wpp = null;
				ArrayList<Wallpaper> wpps = null;
				RotateList rtl = null;
				File registrationFilesDir = WallpaperManagerConstants.registrationFilesDir;
				while (true) {
					try {
						rtlDBA.open();
						rtl = rtlDBA.getSelectedRotateList();
						rtlDBA.close();
						rtlWppDBA.open();
						wpps = rtlWppDBA.getWallpapersFromRotateList(rtl);
						rtlWppDBA.close();

						do {
							wpp = wpps.get(rand.nextInt(wpps.size() - 1));
						} while (wpp.getId() == old_wpp.getId());
						old_wpp = wpp;

						File wallpaperFile = new File(registrationFilesDir,
								wpp.getAddress());
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
		th.start();
	}

	public void onDestroy() {
		th.stop();
	}

	private long getLongFromPrefs() {
		String result = prefs.getString("rotate_time", "hour");
		if (result.compareTo("day") == 0)
			return 60000 * 60 * 24;
		else if (result.compareTo("minutes") == 0)
			return 60000 * 60;
		else
			return 60000;
	}

	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
