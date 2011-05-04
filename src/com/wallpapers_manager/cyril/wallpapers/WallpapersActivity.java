package com.wallpapers_manager.cyril.wallpapers;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.TextView;

import com.wallpapers_manager.cyril.R;
import com.wallpapers_manager.cyril.WallpapersTabActivityGroup;
import com.wallpapers_manager.cyril.folders.Folder;
import com.wallpapers_manager.cyril.folders.FoldersDBAdapter;

//public class WallpapersActivity extends ListActivity {
public class WallpapersActivity extends Activity {
	/* MENU */
	private static final int MENU_ADD_CURRENT_WALLPAPER = 0;
	private static final int MENU_SET_AND_ADD_NEW_WALLPAPER = 1;
	private static final int MENU_MORE = 2;
	private static final int REQUEST_CODE = 0;
	
	private Context 		mContext;
	private Resources		mResources;
	private Folder 			mFolder;
	private ProgressDialog 	mDialog;
	private GetCurrentWallpaperThread 	mGetCurrentWallpaperThread;
	
	private GridView mGridView;
	private TextView mTextView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallpapers_grid);
        
        mContext = WallpapersTabActivityGroup._group;
        mResources = mContext.getResources();
        
        int folderId = this.getIntent().getIntExtra("folderId", 0);
        FoldersDBAdapter foldersDBAdapter = new FoldersDBAdapter(mContext);
        foldersDBAdapter.open();
        	mFolder = foldersDBAdapter.getFolder(folderId);
        foldersDBAdapter.close();

        final WallpapersDBAdapter wallpapersDBAdapter = new WallpapersDBAdapter(mContext);
        wallpapersDBAdapter.open();
	        Cursor cursor = wallpapersDBAdapter.getCursor(folderId);
	        this.mGridView = (GridView) findViewById(R.id.gridview);
	        this.mGridView.setAdapter(new WallpapersCursorAdapter(mContext,cursor));
        wallpapersDBAdapter.close();
  
        this.mTextView = (TextView) findViewById(R.id.name);
        this.mTextView.setText(mFolder.getName());
        
        mDialog = ProgressDialog.show(mContext, "", mResources.getText(R.string.getting_current_wallpaper), true);
        mDialog.cancel();
		
		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
                wallpapersDBAdapter.open();
                	mGridView.setAdapter(new WallpapersCursorAdapter(mContext,wallpapersDBAdapter.getCursor(mFolder.getId())));
				wallpapersDBAdapter.close();
                mDialog.dismiss();
			}
		};
        
		mGetCurrentWallpaperThread = new GetCurrentWallpaperThread(handler, mContext, mFolder.getId());
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add(0, MENU_ADD_CURRENT_WALLPAPER, 0, mResources.getText(R.string.wallpaper_context_menu_add_current));
        menu.add(0, MENU_SET_AND_ADD_NEW_WALLPAPER, 0, mResources.getText(R.string.wallpaper_context_menu_set_and_add));
        menu.add(0, MENU_MORE, 0, "More");
        return true;
    }

    /* Handles item selections */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
	        case MENU_ADD_CURRENT_WALLPAPER:
            	this.startGetCurrentWallpaper();
	            return true;
	        case MENU_SET_AND_ADD_NEW_WALLPAPER:
	        	Intent intent = new Intent();
	        	intent.setAction(Intent.ACTION_SET_WALLPAPER);
	        	WallpapersTabActivityGroup._group.startActivityForResult(intent, REQUEST_CODE);
	            return true;
	        case MENU_MORE:
	        	Intent wallpapersSelectable = new Intent(mContext, WallpapersSelectableActivity.class);
	        	wallpapersSelectable.putExtra("folderId", mFolder.getId());
	        	WallpapersTabActivityGroup._group.startChildActivity("WallpapersSelectableActivity", wallpapersSelectable);
	        	return true;
        }
        return false;
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
        	this.startGetCurrentWallpaper();
        }
    }
    
    public void externOnActivityResult(int requestCode, int resultCode, Intent data) {
    	this.onActivityResult(requestCode, requestCode, data);
    }
    
    private void startGetCurrentWallpaper() {
    	mDialog.show();
    	try {
    		mGetCurrentWallpaperThread.start();
        }
        catch (IllegalThreadStateException e) {
        	mGetCurrentWallpaperThread.run();
        }
    }       
}