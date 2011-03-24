package com.wallpapers_manager.cyril;

import com.wallpapers_manager.cyril.wallpapers.FoldersActivity;
import com.wallpapers_manager.cyril.wallpapers.WallpapersActivity;

import android.content.Intent;
import android.os.Bundle;

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
	
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK) {
			// Toast.makeText(this, "group onActivityResult result OK", 1).show();
			WallpapersActivity wppsAct = (WallpapersActivity) getCurrentActivity();
			wppsAct.externOnActivityResult(requestCode, resultCode, data);
		} else {
			// Toast.makeText(this, "group onActivityResult error "+resultCode, 1).show();
		}
	}
}
