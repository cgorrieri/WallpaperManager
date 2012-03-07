package com.wallpapers_manager.cyril.playlists;

import java.util.List;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.util.Log;

import com.wallpapers_manager.cyril.R;
import com.wallpapers_manager.cyril.RunPlaylistService;

public class PlaylistsSettingActivity extends PreferenceActivity {
	
	public static final String 	HOUR = "hour";
	public static final String 	MINUTE = "minute";
	public static final String 	DAY = "day";
	
	private Context mContext;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.playlists_preferences);
		
		mContext = this;
		final PlaylistsDBAdapter playlistsDBAdapter = new PlaylistsDBAdapter(mContext);
		playlistsDBAdapter.open();
			final Playlist playlist = playlistsDBAdapter.getSelectedPlaylist();
		playlistsDBAdapter.close();
		Preference disableRotateListPreference = (Preference) findPreference("disable_playlist");
		if(playlist != null) {
			disableRotateListPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
				public boolean onPreferenceClick(Preference preference) {
					playlistsDBAdapter.open();
						playlist.setSelected(false);
						playlistsDBAdapter.updatePlaylist(playlist);
					playlistsDBAdapter.close();
					return true;
				}
			});
		} else {
			disableRotateListPreference.setEnabled(false);
		}
		
		final CheckBoxPreference StartStopRotateListCheckBoxPreference = (CheckBoxPreference) findPreference("start_stop_playlist");
		
		if(isServiceRunning()) {
			StartStopRotateListCheckBoxPreference.setChecked(true);
		} else {
			StartStopRotateListCheckBoxPreference.setChecked(false);
		}
		
		final Intent serviceIntent = new Intent(mContext, RunPlaylistService.class);
		
		StartStopRotateListCheckBoxPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				boolean newBool = (Boolean) newValue;
				StartStopRotateListCheckBoxPreference.setChecked(newBool);
				if(newBool) {
					Log.i("Service", "Start");
					mContext.startService(serviceIntent);
				} else {
					Log.i("Service", "Stop");
					mContext.stopService(serviceIntent);
				}
				return false;
			}
		});
	}
	
	public boolean isServiceRunning() {
		final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		final List<ActivityManager.RunningServiceInfo> servicesList = activityManager.getRunningServices(Integer.MAX_VALUE);

		boolean isServiceFound = false;

		for (int i = 0; i < servicesList.size(); i++) {
			if ("com.wallpapers_manager.cyril".equals(servicesList.get(i).service.getPackageName())) {
				if ("com.wallpapers_manager.cyril.RunPlaylistService".equals(servicesList.get(i).service.getClassName())) {
					isServiceFound = true;
				}
			}
		}
		return isServiceFound;
	}

}