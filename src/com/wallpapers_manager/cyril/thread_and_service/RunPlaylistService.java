package com.wallpapers_manager.cyril.thread_and_service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;

public class RunPlaylistService extends Service {

	private SharedPreferences mSharedPreferences;
	private ServiceThread mThread;

	public void onCreate() {
		super.onCreate();
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		mThread = new ServiceThread(getApplicationContext(), getTimeInMillis());
	}

	public void onStart(Intent intent, int startId) {
		mThread.start();
	}

	public void onDestroy() {
		mThread.kill();
	}

	/** Get the time in millisecond according to preferences */
	private long getTimeInMillis() {
		String result = mSharedPreferences.getString("playlist_change_time", "01:00");
		String[] split = result.split(":");
	    int hour = Integer.parseInt(split[0]);
	    int minute = Integer.parseInt(split[1]);
	    return hour*60*60*1000 + minute*60*1000;
	}

	public IBinder onBind(Intent arg0) {
		return null;
	}

}
