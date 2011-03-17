package cyril.wallpaper.rotatelistsmanager;

import cyril.wallpaper.R;
import cyril.wallpaper.RotateWallpaperService;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

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
		
		ActivityManager.RunningServiceInfo rnginfo = new ActivityManager.RunningServiceInfo();
		if(rnginfo.started) {
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
}