package com.wallpapers_manager.cyril;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;

import com.wallpapers_manager.cyril.playlists.PlaylistsSettingActivity;

public class RunPlaylistService extends Service {

	private SharedPreferences mSharedPreferences;
	private ServiceThread mThread;

	public void onCreate() {
		super.onCreate();
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		mThread = new ServiceThread(getApplicationContext(), getTimeInMillis());
	}

	public void onStart(Intent intent, int startId) {
		// Time in millisecond to wait before start thread
		String result = mSharedPreferences.getString("rotate_time", PlaylistsSettingActivity.MINUTE);
		long timeToWaitBeforeStart = 0;
		if (result.compareTo(PlaylistsSettingActivity.DAY) == 0) {
			// wait to next day
		} else if (result.compareTo(PlaylistsSettingActivity.HOUR) == 0) {
			// wait to next hour
		} else {
			// wait to next minute
		}
		mThread.setTimeToSleepBeforeStart(timeToWaitBeforeStart);
		mThread.start();
	}

	public void onDestroy() {
		mThread.kill();
	}

	/** Get the time in millisecond according to preferences */
	private long getTimeInMillis() {
		String result = mSharedPreferences.getString("rotate_time",	PlaylistsSettingActivity.HOUR);
		if (result.compareTo(PlaylistsSettingActivity.DAY) == 0) {
			return 60000 * 60 * 24;
		} else if (result.compareTo(PlaylistsSettingActivity.MINUTE) == 0) {
			return 10000;
		} else {
			//return 60000 * 60;
			return 10000;
		}
	}

	public IBinder onBind(Intent arg0) {
		return null;
	}

}
