package com.wallpapers_manager.cyril.activity;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.wallpapers_manager.cyril.R;
import com.wallpapers_manager.cyril.bdd.PlaylistsDBAdapter;
import com.wallpapers_manager.cyril.bdd.WallpapersPlaylistDBAdapter;
import com.wallpapers_manager.cyril.data.Playlist;
import com.wallpapers_manager.cyril.data.Wallpaper;
import com.wallpapers_manager.cyril.data.WallpaperPlaylist;

//public class WallpapersActivity extends ListActivity {
public class WallpapersPlaylistSelectableActivity extends WallpapersSelectableActivityAbstract {
	private static final int DELETE = 10;
	private Playlist mPlaylist;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

	/**
	 * Remove selected wallpaper from the playlist
	 */
	private void deleteWallpapersInPlaylist() {
		final SparseBooleanArray checkedItems = mGridView.getCheckedItemPositions();
		if (checkedItems == null) {
			Toast.makeText(this, "No wallpaper selected", Toast.LENGTH_LONG).show();
			return;
		}
		final int checkedItemsCount = checkedItems.size();
		WallpapersPlaylistDBAdapter wallpapersPlaylistDBAdapter = new WallpapersPlaylistDBAdapter(mContext);
		for (int i = 0; i < checkedItemsCount; ++i) {
			boolean isChecked = checkedItems.valueAt(i);
			if (isChecked) {
				int id = checkedItems.keyAt(i);
				wallpapersPlaylistDBAdapter.open();
					wallpapersPlaylistDBAdapter.remove(new WallpaperPlaylist(id, mPlaylist.getId()));
				wallpapersPlaylistDBAdapter.close();
			}
		}
		
		PlaylistsTabActivityGroup._group.finishFromChild(this);
	}
	
	@Override
	protected void initConteneur() {
		int playlistId = this.getIntent().getIntExtra("playlistId", 0);
        PlaylistsDBAdapter playlistsDBAdapter = new PlaylistsDBAdapter(mContext);
        playlistsDBAdapter.open();
        	mPlaylist = playlistsDBAdapter.getPlaylist(playlistId);
        playlistsDBAdapter.close();
	}  

	@Override
	protected ArrayList<Wallpaper> getWallpapers() {
        final WallpapersPlaylistDBAdapter wallpapersPlaylistDBAdapter = new WallpapersPlaylistDBAdapter(mContext);
        wallpapersPlaylistDBAdapter.open();
        	ArrayList<Wallpaper> wallpapersList = wallpapersPlaylistDBAdapter.getWallpapersFromPlaylist(mPlaylist);
        wallpapersPlaylistDBAdapter.close();
        return wallpapersList;
	}

	@Override
	protected String getConteneurName() {
		return mPlaylist.getName();
	}

	@Override
	protected void displayButtonAction() {
		mLayoutForButton = (LinearLayout) findViewById(R.id.buttonLayout);
    	/* DELETE */
		Button buttonDelete = new Button(mContext);
		buttonDelete.setId(DELETE);
		// TODO use @string
		buttonDelete.setText("Delete");
		buttonDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				deleteWallpapersInPlaylist();
			}
		});
		mLayoutForButton.addView(buttonDelete);
		// TODO copy, move
	}

	@Override
	protected Context getContext() {
		return PlaylistsTabActivityGroup._group;
	}  
}