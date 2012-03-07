package com.wallpapers_manager.cyril.activity;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.wallpapers_manager.cyril.R;
import com.wallpapers_manager.cyril.adapter.WallpapersArrayAdapter;
import com.wallpapers_manager.cyril.bdd.FoldersDBAdapter;
import com.wallpapers_manager.cyril.bdd.WallpapersDBAdapter;
import com.wallpapers_manager.cyril.data.Folder;
import com.wallpapers_manager.cyril.data.Wallpaper;
import com.wallpapers_manager.cyril.widget.CheckableRelativeLayout;
import com.wallpapers_manager.cyril.widget.MultiSelectGridView;

//public class WallpapersActivity extends ListActivity {
public class WallpapersSelectableActivity extends Activity {	
	private Context 		mContext;
	private Folder 			mFolder;
	
	private MultiSelectGridView 	mGridView;
	private TextView 				mTextView;
	private CheckedTextView		mCheckedTextView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallpapers_selectable_grid);
        
        mContext = WallpapersTabActivityGroup._group;
        
        int folderId = this.getIntent().getIntExtra("folderId", 0);
        FoldersDBAdapter foldersDBAdapter = new FoldersDBAdapter(mContext);
        foldersDBAdapter.open();
        	mFolder = foldersDBAdapter.getFolder(folderId);
        foldersDBAdapter.close();
        
        mTextView = (TextView) findViewById(R.id.name);
        mTextView.setText(mFolder.getName());

        mGridView = (MultiSelectGridView) findViewById(R.id.gridview);
        
        final WallpapersDBAdapter wallpapersDBAdapter = new WallpapersDBAdapter(mContext);
        wallpapersDBAdapter.open();
        	ArrayList<Wallpaper> wallpapersList = wallpapersDBAdapter.getWallpapersFromFolder(mFolder);
        wallpapersDBAdapter.close();
        
        mGridView.setAdapter(new WallpapersArrayAdapter(mContext, wallpapersList));
        
        mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				CheckableRelativeLayout checkableRelativeLayout = (CheckableRelativeLayout)view;
				if(checkableRelativeLayout.isChecked()) {
					checkableRelativeLayout.setChecked(false);
					if(mCheckedTextView.isChecked())
						mCheckedTextView.setChecked(false);
				} else
					checkableRelativeLayout.setChecked(true);
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
			deleteWallpapers();
			break;
//		case R.id.viewCheckedItemsButton:
//			showSelectedItems();
//			break;
//		case R.id.toggleChoiceModeButton:
//			toggleChoiceMode();
//			break;
		}
	}

	/**
	 * Show a message giving the selected item IDs. There seems to be a bug with
	 * ListView#getCheckItemIds() on Android 1.6 at least @see
	 * http://code.google.com/p/android/issues/detail?id=6609
	 */
	private void deleteWallpapers() {
		final SparseBooleanArray checkedItems = mGridView.getCheckedItemPositions();
		if (checkedItems == null) {
			Toast.makeText(this, "No folder selected",
					Toast.LENGTH_LONG).show();
			return;
		}
		final int checkedItemsCount = checkedItems.size();
		WallpapersDBAdapter wallpapersDBAdapter = new WallpapersDBAdapter(mContext);
		Wallpaper wallpaper;
		for (int i = 0; i < checkedItemsCount; ++i) {
			final int position = checkedItems.keyAt(i);
			wallpaper = (Wallpaper) mGridView.getItemAtPosition(position);

			final boolean isChecked = checkedItems.valueAt(i);

			if (isChecked) {
				wallpapersDBAdapter.open();
						wallpaper.delete(wallpapersDBAdapter);
				wallpapersDBAdapter.close();
			}
		}
		
		WallpapersTabActivityGroup._group.finishFromChild(this);
	}

	/**
	 * Uncheck all the items
	 */
	private void clearSelection() {
		final int itemCount = mGridView.getCount();
		for (int i = 0; i < itemCount; ++i) {
			mGridView.setItemChecked(i, false);
		}
	}
	
	/**
	 * Check all the items
	 */
	private void selectAll() {
		final int itemCount = mGridView.getCount();
		for (int i = 0; i < itemCount; ++i) {
			mGridView.setItemChecked(i, true);
		}
	}
	
    
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    /* Handles item selections */
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }
    
}