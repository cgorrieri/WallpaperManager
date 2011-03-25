package com.wallpapers_manager.cyril;

import java.util.ArrayList;

import com.wallpapers_manager.cyril.playlists.PlaylistsActivity;

import android.content.Intent;
import android.os.Bundle;

public class PlaylistsTabActivityGroup extends TabActivityGroup {
	
	public static PlaylistsTabActivityGroup 	_group;

	public ArrayList<String> 	mIdList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_group = this;

		this.startChildActivity("PlaylistsActivity", 
			new Intent(this,PlaylistsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

	}
}
