package com.wallpapers_manager.cyril.folders;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckedTextView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.wallpapers_manager.cyril.R;
import com.wallpapers_manager.cyril.WallpaperManagerConstants;
import com.wallpapers_manager.cyril.WallpapersTabActivityGroup;
import com.wallpapers_manager.cyril.playlists.PlaylistsDBAdapter;
import com.wallpapers_manager.cyril.wallpapers.Wallpaper;
import com.wallpapers_manager.cyril.wallpapers.WallpapersDBAdapter;
import com.wallpapers_manager.cyril.widget.CheckableRelativeLayout;

public class FoldersSelectableActivity extends ListActivity {	
	private Context 				mContext;
	private ListView				mListView;
	private CheckedTextView			mCheckedTextView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.folders_selectable_list);
        
        // Create the wallpaper directory if not already exist
        WallpaperManagerConstants.makeRegistrationFilesDir();
        
        mContext = WallpapersTabActivityGroup._group;
        
        final FoldersDBAdapter foldersDBAdapter = new FoldersDBAdapter(this);
        foldersDBAdapter.open();
	        ArrayList<Folder> foldersList = foldersDBAdapter.getFolders();
        foldersDBAdapter.close();
        
        setListAdapter(new FoldersArrayAdapter(mContext, foldersList));
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
			deleteFolders();
			break;
		case R.id.add_to:
			addFoldersTo();
			break;
		}
	}
    
	/**
	 * Show a message giving the selected item captions
	 */
	private void deleteFolders() {
		final SparseBooleanArray checkedItems = mListView.getCheckedItemPositions();
		if (checkedItems == null) {
			Toast.makeText(this, "No folder selected",
					Toast.LENGTH_LONG).show();
			return;
		}
		final int checkedItemsCount = checkedItems.size();
		FoldersDBAdapter foldersDBAdapter = new FoldersDBAdapter(mContext);
		WallpapersDBAdapter wallpapersDBAdapter = new WallpapersDBAdapter(mContext);
		Folder folder;
		for (int i = 0; i < checkedItemsCount; ++i) {
			final int position = checkedItems.keyAt(i);
			folder = (Folder) mListView.getItemAtPosition(position);

			final boolean isChecked = checkedItems.valueAt(i);

			if (isChecked) {
				wallpapersDBAdapter.open();
					ArrayList<Wallpaper> wallpapersList = wallpapersDBAdapter.getWallpapersFromFolder(folder);
					for (int j = 0; i < wallpapersList.size(); j++)
						wallpapersList.get(j).delete(wallpapersDBAdapter);
				wallpapersDBAdapter.close();
				foldersDBAdapter.open();
					foldersDBAdapter.removeFolder(folder);
				foldersDBAdapter.close();
			}
		}
		
		WallpapersTabActivityGroup._group.finishFromChild(this);
	}
	
	private void addFoldersTo() {

		final SparseBooleanArray checkedItems = mListView.getCheckedItemPositions();
		if (checkedItems == null) {
			Toast.makeText(this, "No folder selected",
					Toast.LENGTH_LONG).show();
			return;
		}
		
		final int checkedItemsCount = checkedItems.size();
		ArrayList<Folder> foldersList = new ArrayList<Folder>(checkedItemsCount);
		for (int i = 0; i < checkedItemsCount; ++i) {
			final int position = checkedItems.keyAt(i);
			foldersList.add((Folder) mListView.getItemAtPosition(position));
		}
		
		final Activity activity = this;
		Handler handler = new Handler(new Callback() {	
			@Override
			public boolean handleMessage(Message msg) {
				WallpapersTabActivityGroup._group.finishFromChild(activity);
				return false;
			}
		});
		
		final Dialog dialog = new Dialog(mContext);

		PlaylistsDBAdapter playlistsDBAdapter = new PlaylistsDBAdapter(mContext);
		playlistsDBAdapter.open();
			Cursor cur = playlistsDBAdapter.getCursor();
			ListView lstA = new ListView(mContext);
			CursorAdapter ca = new AddFoldersInPlaylistCursorAdapter(mContext, cur, foldersList, dialog, handler);
			lstA.setAdapter(ca);
		playlistsDBAdapter.close();
		dialog.setContentView(lstA);
		dialog.setTitle("Add to");
		dialog.show();
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