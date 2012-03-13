package com.wallpapers_manager.cyril.activity;

import static com.wallpapers_manager.cyril.WallpaperManagerConstants.BROADCAST_UPDATE_WPP_PL;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.TextView;

import com.wallpapers_manager.cyril.R;
import com.wallpapers_manager.cyril.adapter.WallpapersPlaylistCursorAdapter;
import com.wallpapers_manager.cyril.bdd.PlaylistsDBAdapter;
import com.wallpapers_manager.cyril.bdd.WallpapersPlaylistDBAdapter;
import com.wallpapers_manager.cyril.data.Playlist;

public class WallpapersPlaylistActivity extends Activity {
	private static final int MENU_MORE = 0;
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
				if(intent.getAction().compareTo(BROADCAST_UPDATE_WPP_PL) == 0) {
					wallpapersPlaylistDBAdapter.open();
				        Cursor curs = wallpapersPlaylistDBAdapter.getCursor(mPlaylist.getId());
				        mGridView.setAdapter(new WallpapersPlaylistCursorAdapter(mContext, curs));
			        wallpapersPlaylistDBAdapter.close();
				}
			}
		};
		registerReceiver(broadcastReceiver, new IntentFilter(BROADCAST_UPDATE_WPP_PL));
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_MORE, 0, "More");
        return true;
    }

    /* Handles item selections */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
	        case MENU_MORE:
	        	Intent intent = new Intent(mContext, WallpapersPlaylistSelectableActivity.class);
	        	intent.putExtra("playlistId", mPlaylist.getId());
	        	PlaylistsTabActivityGroup._group.startChildActivity("WallpapersPlaylistSelectableActivity", intent);
	        	return true;
        }
        return false;
    }
}