package com.wallpapers_manager.cyril.thread_and_service;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.util.Log;

import com.wallpapers_manager.cyril.Helper;
import com.wallpapers_manager.cyril.bdd.WallpapersPlaylistDBAdapter;
import com.wallpapers_manager.cyril.data.Wallpaper;

/** A trhead which change the system wallpaper every x time */
public class ServiceThread extends Thread {
	private static Random _rand = new Random();
	private boolean stopThread = false;
	
	private Context mContext;
	private long mSleepTimeInMillis;
	private long mTimeInMillisBeforeStart;
	
	public ServiceThread(Context mContext, long mSleepInMillis) {
		super();
		this.mContext = mContext;
		this.mSleepTimeInMillis = mSleepInMillis;
		this.mTimeInMillisBeforeStart = 0;
	}

	public void run() {
		Log.i("Thread", "RUN");
		try {
			sleep(mTimeInMillisBeforeStart);
		} catch (InterruptedException e1) {
			// TODO Send notification
			e1.printStackTrace();
		}
		
		boolean kill = false;
		long sleepInMillis = this.mSleepTimeInMillis;
		
		WallpapersPlaylistDBAdapter playListWallpaperDBAdapter = new WallpapersPlaylistDBAdapter(mContext);
		Wallpaper oldWallpaper = new Wallpaper(-1, -1, "");
		Wallpaper currentWallpaper = null;
		ArrayList<Wallpaper> wallpapersList = null;
				
		// get playlist's wallpaper
		playListWallpaperDBAdapter.open();
		/* -- ERROR wallpaperList contains Null reference -- */
		wallpapersList = playListWallpaperDBAdapter.getWallpapersFromSelectedPlaylist();
		playListWallpaperDBAdapter.close();
		
		if (wallpapersList == null) {
			// TODO: notify
			kill = true;
			synchronized (this) {
				this.kill();
			}
		}
		
		while (!kill) {
			try {
				// We want to put a different wallpaper than the older
				do {
					currentWallpaper = wallpapersList.get(_rand.nextInt(wallpapersList.size() - 1));
				} while (currentWallpaper.getId() == oldWallpaper.getId());
				oldWallpaper = currentWallpaper;

				Helper.setWallpaper(mContext, currentWallpaper.getAddress());
				sleep(sleepInMillis);
			} catch (Exception e) {
				// TODO Send notification
				e.printStackTrace();
			}
			// Get synchronized value
			synchronized (this) {
				Thread.yield();
				kill = this.stopThread;
				sleepInMillis = this.mSleepTimeInMillis;
			}
		}
	}

	/** Stop the process */
	public synchronized void kill() {
		this.stopThread = true;
	}
	
	/** Change the sleep duration between two wallpaper */
	public synchronized void changeSleep(long timeInMillis) {
		this.mSleepTimeInMillis = timeInMillis;
	}
	
	/** Change the sleep duration between two wallpaper */
	public void setTimeToSleepBeforeStart(long timeInMillis) {
		this.mTimeInMillisBeforeStart = timeInMillis;
	}
}
