package com.wallpapers_manager.cyril;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.wallpapers_manager.cyril.wallpapers.FoldersActivity;
import com.wallpapers_manager.cyril.wallpapers.WallpapersActivity;

public class WallpapersTabActivityGroup extends TabActivityGroup {
	
	public static WallpapersTabActivityGroup 	_group;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_group = this;
		// Toast.makeText(this, "onCreate", 2).show();

		this.startChildActivity("FoldersActivity", new Intent(this,
			FoldersActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

	}
	
	public void refreshCurrent() {
		Intent lastIntent = new Intent(this, getCurrentActivity().getClass());
		
		LocalActivityManager localActivityManager = getLocalActivityManager();
		String lastId = mIdList.get(mIdList.size() - 1);

		localActivityManager.destroyActivity(lastId, true);
		startChildActivity(lastId, lastIntent);
	}
	
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK) {
			WallpapersActivity wppsAct = (WallpapersActivity) getCurrentActivity();
			wppsAct.externOnActivityResult(requestCode, resultCode, data);
		} else {
			
		}
	}
}
