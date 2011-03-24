package com.wallpapers_manager.cyril;

import java.util.ArrayList;

import com.wallpapers_manager.cyril.rotate_lists.RotateListsActivity;

import android.content.Intent;
import android.os.Bundle;

public class RotateListsTabActivityGroup extends TabActivityGroup {
	
	public static RotateListsTabActivityGroup 	_group;

	public ArrayList<String> 	mIdList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_group = this;

		this.startChildActivity("RotateListsActivity", 
			new Intent(this,RotateListsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

	}
}
