package com.wallpapers_manager.cyril.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TabHost;

import com.wallpapers_manager.cyril.R;

public class HomeTabActivity extends TabActivity{

	private TabHost 	mTabHost;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTabHost = getTabHost();
        Resources resource = getResources();
 
        mTabHost.addTab(mTabHost.newTabSpec("wallpapers").setIndicator(resource.getText(R.string.wallpapers_tab_name), resource.getDrawable(R.drawable.ic_wallpapers_tab)).
        		setContent(new Intent(this, WallpapersTabActivityGroup.class)));
        
        mTabHost.addTab(mTabHost.newTabSpec("playlist").setIndicator(resource.getText(R.string.playlists_tab_name), resource.getDrawable(R.drawable.ic_playlists_tab))
        		.setContent(new Intent(this, PlaylistsTabActivityGroup.class)));
 
        mTabHost.setCurrentTab(0);
    }
    
 
    public void setFullscreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

}
