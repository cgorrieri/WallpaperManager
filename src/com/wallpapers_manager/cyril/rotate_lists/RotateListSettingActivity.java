package com.wallpapers_manager.cyril.rotate_lists;

import java.util.List;

import com.wallpapers_manager.cyril.R;
import com.wallpapers_manager.cyril.RotateWallpaperService;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.util.Log;

public class RotateListSettingActivity extends PreferenceActivity {
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.rotate_list_preferences);
		
		final Context ctxt = this;
		final RotateListsDBAdapter rtlDBA = new RotateListsDBAdapter(this);
		rtlDBA.open();
		final RotateList rtl = rtlDBA.getSelectedRotateList();
		rtlDBA.close();
		Preference disable_rotate_list = (Preference) findPreference("disable_rotate_list");
		if(rtl != null) {
			disable_rotate_list.setOnPreferenceClickListener(new OnPreferenceClickListener() {
				public boolean onPreferenceClick(Preference preference) {
					rtlDBA.open();
					rtl.setSelected(0);
					rtlDBA.updateRotateList(rtl);
					rtlDBA.close();
					return true;
				}

			});
		} else {
			disable_rotate_list.setEnabled(false);
		}
		
		final Preference start_rotate_list = (Preference) findPreference("start_rotate_list");
		final Preference stop_rotate_list = (Preference) findPreference("stop_rotate_list");
		
		if(isServiceRunning()) {
			start_rotate_list.setEnabled(false);
		} else {
			stop_rotate_list.setEnabled(false);
		}
		
		final Intent service = new Intent(ctxt, RotateWallpaperService.class);
		
		start_rotate_list.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				ctxt.startService(service);
				stop_rotate_list.setEnabled(true);
				preference.setEnabled(false);
				return true;
			}
		});
		
		stop_rotate_list.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				ctxt.stopService(service);
				start_rotate_list.setEnabled(true);
				preference.setEnabled(false);
				return true;
			}
		});
	}
	
	public boolean isServiceRunning() {
		final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		final List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

		boolean isServiceFound = false;

		for (int i = 0; i < services.size(); i++) {
			// Log.d(Global.TAG, "Service Nr. " + i + " :" +
			// services.get(i).service);
			// Log.d(Global.TAG, "Service Nr. " + i + " package name : " +
			// services.get(i).service.getPackageName());
			Log.i("service",
					"Service Nr. " + i + " class name : "
							+ services.get(i).service.getClassName()+ ", package name : " +services.get(i).service.getPackageName());

			if ("com.wallpapers_manager.cyril".equals(services.get(i).service.getPackageName())) {
//				Log.d(Global.TAG, "packagename stimmt überein !!!");
				// Log.d(LOG_TAG, "SpotService" + " : " +
				// services.get(i).service.getClassName());

				if ("com.wallpapers_manager.cyril.RotateWallpaperService"
						.equals(services.get(i).service.getClassName())) {
//					Log.d(Global.TAG, "getClassName stimmt überein !!!");
					isServiceFound = true;
				}
			}
		}
		return isServiceFound;
	}

}