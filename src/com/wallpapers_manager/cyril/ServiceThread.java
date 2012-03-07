package com.wallpapers_manager.cyril;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.util.Log;

import com.wallpapers_manager.cyril.playlists.Playlist;
import com.wallpapers_manager.cyril.playlists.PlaylistsDBAdapter;
import com.wallpapers_manager.cyril.wallpapers.Wallpaper;
import com.wallpapers_manager.cyril.wallpapers_playlist.WallpapersPlaylistDBAdapter;

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
		WallpapersPlaylistDBAdapter rotateListWallpaperDBAdapter = new WallpapersPlaylistDBAdapter(mContext);
		PlaylistsDBAdapter playlistsDBAdapter = new PlaylistsDBAdapter(mContext);
		Wallpaper oldWallpaper = new Wallpaper(-1, -1, "");
		Wallpaper currentWallpaper = null;
		ArrayList<Wallpaper> wallpapersList = null;
		Playlist playlist = null;
		
		// get selected playlist
		playlistsDBAdapter.open();
		playlist = playlistsDBAdapter.getSelectedPlaylist();
		playlistsDBAdapter.close();
		// it never append
		if (playlist == null) throw new NullPointerException("No playlist selected");
		
		// get playlist's wallpaper
		rotateListWallpaperDBAdapter.open();
		/* -- ERROR wallpaperList contains Null reference -- */
		wallpapersList = rotateListWallpaperDBAdapter.getWallpapersFromPlaylist(playlist);
		rotateListWallpaperDBAdapter.close();
		
		if (wallpapersList.size() < 2) {
			// notification error
		}
		
		boolean kill = false;
		long sleepInMillis = this.mSleepTimeInMillis;
		
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
