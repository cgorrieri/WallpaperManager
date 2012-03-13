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
import com.wallpapers_manager.cyril.bdd.FoldersDBAdapter;
import com.wallpapers_manager.cyril.bdd.WallpapersDBAdapter;
import com.wallpapers_manager.cyril.data.Folder;
import com.wallpapers_manager.cyril.data.Wallpaper;

//public class WallpapersActivity extends ListActivity {
public class WallpapersSelectableActivity extends WallpapersSelectableActivityAbstract {	
	private Folder 	mFolder;	
	private static final int DELETE = 10;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
				deleteWallpapers();
			}
		});
		mLayoutForButton.addView(buttonDelete);
		// TODO copy, move, add to playlist
    }

	/**
	 * Delete selected wallpapers
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
		for (int i = 0; i < checkedItemsCount; ++i) {
			boolean isChecked = checkedItems.valueAt(i);
			if (isChecked) {
				int id = checkedItems.keyAt(i);
				wallpapersDBAdapter.open();
					wallpapersDBAdapter.removeWallpaper(id);
				wallpapersDBAdapter.close();
			}
		}
		
		WallpapersTabActivityGroup._group.finishFromChild(this);
	}

	@Override
	protected void initConteneur() {
		int folderId = this.getIntent().getIntExtra("folderId", 0);
        FoldersDBAdapter foldersDBAdapter = new FoldersDBAdapter(mContext);
        foldersDBAdapter.open();
        	mFolder = foldersDBAdapter.getFolder(folderId);
        foldersDBAdapter.close();
	}

	@Override
	protected ArrayList<Wallpaper> getWallpapers() {
		final WallpapersDBAdapter wallpapersDBAdapter = new WallpapersDBAdapter(mContext);
        wallpapersDBAdapter.open();
        	ArrayList<Wallpaper> wallpapersList = wallpapersDBAdapter.getWallpapersFromFolder(mFolder);
        wallpapersDBAdapter.close();
		return wallpapersList;
	}

	@Override
	protected String getConteneurName() {
		return mFolder.getName();
	}
	
	@Override
	protected Context getContext() {
		return WallpapersTabActivityGroup._group;
	}
}