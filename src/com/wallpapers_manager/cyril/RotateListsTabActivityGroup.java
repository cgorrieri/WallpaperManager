package com.wallpapers_manager.cyril;

import java.util.ArrayList;

import com.wallpapers_manager.cyril.rotate_lists.RotateListsActivity;

import android.content.Intent;
import android.os.Bundle;

public class RotateListsTabActivityGroup extends TabActivityGroup {
	/* Keep this in a static variable to make it accessible for all the nested
	   activities, lets them manipulate the view */
	public static RotateListsTabActivityGroup group;

	public ArrayList<String> mIdList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		group = this;
		// Toast.makeText(this, "onCreate", 2).show();

			this.startChildActivity("RotateListsActivity", new Intent(this,
					RotateListsActivity.class)
					.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

	}
}
