package com.wallpapers_manager.cyril.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.widget.GridView;
import android.widget.TextView;

import com.wallpapers_manager.cyril.R;
import com.wallpapers_manager.cyril.adapter.WallpapersPlaylistCursorAdapter;
import com.wallpapers_manager.cyril.bdd.PlaylistsDBAdapter;
import com.wallpapers_manager.cyril.bdd.WallpapersPlaylistDBAdapter;
import com.wallpapers_manager.cyril.data.Playlist;

public class WallpapersPlaylistActivity extends Activity {
	private Context 	mContext;
	private Playlist 	mPlaylist;
	private GridView 	mGridView;
	private TextView 	mTextView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallpapers_grid);
        
        mContext = PlaylistsTabActivityGroup._group;
        
        PlaylistsDBAdapter playlistsDBAdapter = new PlaylistsDBAdapter(mContext);
        playlistsDBAdapter.open();
        	mPlaylist = playlistsDBAdapter.getPlaylist(this.getIntent().getIntExtra("playlistId", 0));
        playlistsDBAdapter.close();

        final WallpapersPlaylistDBAdapter wallpapersPlaylistDBAdapter = new WallpapersPlaylistDBAdapter(this);
        wallpapersPlaylistDBAdapter.open();
	        Cursor curs = wallpapersPlaylistDBAdapter.getCursor(mPlaylist.getId());
	        mGridView = (GridView) findViewById(R.id.gridview);
	        mGridView.setAdapter(new WallpapersPlaylistCursorAdapter(mContext, curs));
        wallpapersPlaylistDBAdapter.close();
        
        mTextView = (TextView) findViewById(R.id.name);
        mTextView.setText(mPlaylist.getName());
        
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if(intent.getAction().compareTo("com.wallpaper_manager.cyril.updateWallpapersPlaylistCursor") == 0) {
					wallpapersPlaylistDBAdapter.open();
				        Cursor curs = wallpapersPlaylistDBAdapter.getCursor(mPlaylist.getId());
				        mGridView.setAdapter(new WallpapersPlaylistCursorAdapter(mContext, curs));
			        wallpapersPlaylistDBAdapter.close();
				}
			}
		};
		
		registerReceiver(broadcastReceiver, new IntentFilter("com.wallpaper_manager.cyril.updateWallpapersPlaylistCursor"));
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    } 
}