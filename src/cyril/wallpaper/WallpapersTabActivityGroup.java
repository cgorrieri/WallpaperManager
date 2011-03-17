package cyril.wallpaper;

import cyril.wallpaper.wallpapersmanager.FoldersActivity;
import cyril.wallpaper.wallpapersmanager.WallpapersActivity;
import android.content.Intent;
import android.os.Bundle;

public class WallpapersTabActivityGroup extends TabActivityGroup {
	/* Keep this in a static variable to make it accessible for all the nested
	   activities, lets them manipulate the view */
	public static WallpapersTabActivityGroup group;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		group = this;
		// Toast.makeText(this, "onCreate", 2).show();

		this.startChildActivity("FoldersActivity", new Intent(this,
			FoldersActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

	}
	
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK) {
			// Toast.makeText(this, "group onActivityResult result OK", 1).show();
			WallpapersActivity wppA = (WallpapersActivity) getCurrentActivity();
			wppA.externOnActivityResult(requestCode, resultCode, data);
		} else {
			// Toast.makeText(this, "group onActivityResult error "+resultCode, 1).show();
		}
	}
}
