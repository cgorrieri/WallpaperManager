package com.wallpapers_manager.cyril.rotate_lists;

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

import com.wallpapers_manager.cyril.R;
import com.wallpapers_manager.cyril.RotateWallpaperService;

public class RotateListSettingActivity extends PreferenceActivity {
	
	public static final String 	HOUR = "hour";
	public static final String 	MINUTE = "minute";
	public static final String 	DAY = "day";
	
	private Context mContext;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.rotate_list_preferences);
		
		mContext = this;
		final RotateListsDBAdapter rotateListsDBAdapter = new RotateListsDBAdapter(mContext);
		rotateListsDBAdapter.open();
		final RotateList rotateList = rotateListsDBAdapter.getSelectedRotateList();
		rotateListsDBAdapter.close();
		Preference disableRotateListPreference = (Preference) findPreference("disableRotateListPreference");
		if(rotateList != null) {
			disableRotateListPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
				public boolean onPreferenceClick(Preference preference) {
					rotateListsDBAdapter.open();
					rotateList.setSelected(false);
					rotateListsDBAdapter.updateRotateList(rotateList);
					rotateListsDBAdapter.close();
					return true;
				}
			});
		} else {
			disableRotateListPreference.setEnabled(false);
		}
		
		final CheckBoxPreference StartStopRotateListCheckBoxPreference = (CheckBoxPreference) findPreference("start_stop_rotate_list");
		
		if(isServiceRunning()) {
			StartStopRotateListCheckBoxPreference.setChecked(true);
		} else {
			StartStopRotateListCheckBoxPreference.setChecked(false);
		}
		
		final Intent serviceIntent = new Intent(mContext, RotateWallpaperService.class);
		
		StartStopRotateListCheckBoxPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				boolean newBool = (Boolean) newValue;
				StartStopRotateListCheckBoxPreference.setChecked(newBool);
				if(newBool) {
					mContext.startService(serviceIntent);
				} else {
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
				if ("com.wallpapers_manager.cyril.RotateWallpaperService".equals(servicesList.get(i).service.getClassName())) {
					isServiceFound = true;
				}
			}
		}
		return isServiceFound;
	}

}