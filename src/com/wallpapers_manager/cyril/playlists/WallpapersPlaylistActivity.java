package com.wallpapers_manager.cyril.playlists;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.widget.GridView;
import android.widget.TextView;

import com.wallpapers_manager.cyril.R;
import com.wallpapers_manager.cyril.PlaylistsTabActivityGroup;

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
	        this.mGridView = (GridView) findViewById(R.id.gridview);
	        this.mGridView.setAdapter(new WallpapersPlaylistCursorAdapter(mContext, curs));
        wallpapersPlaylistDBAdapter.close();
        
        this.mTextView = (TextView) findViewById(R.id.name);
        this.mTextView.setText(mPlaylist.getName());
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    } 
}