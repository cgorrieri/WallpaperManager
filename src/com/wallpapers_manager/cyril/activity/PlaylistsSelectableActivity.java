package com.wallpapers_manager.cyril.activity;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.wallpapers_manager.cyril.R;
import com.wallpapers_manager.cyril.adapter.PlaylistsArrayAdapter;
import com.wallpapers_manager.cyril.bdd.PlaylistsDBAdapter;
import com.wallpapers_manager.cyril.bdd.WallpapersPlaylistDBAdapter;
import com.wallpapers_manager.cyril.data.Playlist;
import com.wallpapers_manager.cyril.widget.CheckableRelativeLayout;

public class PlaylistsSelectableActivity extends ListActivity {	
	private Context 				mContext;
	private ListView				mListView;
	private CheckedTextView			mCheckedTextView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist_selectable_list);
        
        mContext = PlaylistsTabActivityGroup._group;
        
        final PlaylistsDBAdapter playlistsDBAdapter = new PlaylistsDBAdapter(this);
        playlistsDBAdapter.open();
	        ArrayList<Playlist> foldersList = playlistsDBAdapter.getPlaylists();
	        playlistsDBAdapter.close();
        
        setListAdapter(new PlaylistsArrayAdapter(mContext, foldersList));
        mListView = getListView();
		mListView.setItemsCanFocus(false);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
				CheckableRelativeLayout view = (CheckableRelativeLayout) v;
				if(view.isChecked() && mCheckedTextView.isChecked())
					mCheckedTextView.setChecked(false);
			}
		});
		
		mCheckedTextView = (CheckedTextView) findViewById(R.id.select_all);
		mCheckedTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mCheckedTextView.isChecked()) {
					clearSelection();
					mCheckedTextView.setChecked(false);
				} else {
					selectAll();
					mCheckedTextView.setChecked(true);
				}
			}
		});
    }
    
    public void onButtonClick(View v) {
		switch (v.getId()) {
		case R.id.delete:
			deletePlaylists();
			break;
		}
	}
    
	/**
	 * Show a message giving the selected item captions
	 */
	private void deletePlaylists() {
		final SparseBooleanArray checkedItems = mListView.getCheckedItemPositions();
		if (checkedItems == null) {
			Toast.makeText(this, "No folder selected",
					Toast.LENGTH_LONG).show();
			return;
		}
		final int checkedItemsCount = checkedItems.size();
		PlaylistsDBAdapter playlistsDBAdapter = new PlaylistsDBAdapter(mContext);
		WallpapersPlaylistDBAdapter wallpapersPlaylistDBAdapter = new WallpapersPlaylistDBAdapter(mContext);
		Playlist playlist;
		for (int i = 0; i < checkedItemsCount; ++i) {
			final int position = checkedItems.keyAt(i);
			playlist = (Playlist) mListView.getItemAtPosition(position);

			final boolean isChecked = checkedItems.valueAt(i);

			if (isChecked) {
				wallpapersPlaylistDBAdapter.open();
					wallpapersPlaylistDBAdapter.removeFromPlaylistId(playlist.getId());
				wallpapersPlaylistDBAdapter.close();
				playlistsDBAdapter.open();
					playlistsDBAdapter.removePlaylist(playlist);
				playlistsDBAdapter.close();
			}
		}
		
		PlaylistsTabActivityGroup._group.finishFromChild(this);
	}

	/**
	 * Uncheck all the items
	 */
	private void clearSelection() {
		final int itemCount = mListView.getCount();
		for (int i = 0; i < itemCount; ++i) {
			mListView.setItemChecked(i, false);
		}
	}
	
	private void selectAll() {
		final int itemCount = mListView.getCount();
		for (int i = 0; i < itemCount; ++i) {
			mListView.setItemChecked(i, true);
		}
	}
    
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

}