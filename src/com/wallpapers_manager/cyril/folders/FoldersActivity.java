package com.wallpapers_manager.cyril.folders;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CursorAdapter;
import android.widget.EditText;

import com.wallpapers_manager.cyril.R;
import com.wallpapers_manager.cyril.WallpaperManagerConstants;
import com.wallpapers_manager.cyril.WallpapersTabActivityGroup;

public class FoldersActivity extends ListActivity {
	private static final int 	MENU_ADD_FOLDER = 0;
	private static final int 	MENU_MORE = 1;
	
	private Context 				mContext;
	private Resources 				mResources;
	private AlertDialog				mAddFolderAlertDialog;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.folders_list);
        
        // Create the wallpaper directory if not already exist
        WallpaperManagerConstants.makeRegistrationFilesDir();
        
        mContext = WallpapersTabActivityGroup._group;
        mResources = mContext.getResources();
        
        final FoldersDBAdapter foldersDBAdapter = new FoldersDBAdapter(this);
        foldersDBAdapter.open();
	        Cursor foldersCursor = foldersDBAdapter.getCursor();
	        final CursorAdapter foldersCursorAdapter = new FoldersCursorAdapter(mContext,foldersCursor);
	        setListAdapter(foldersCursorAdapter);
        foldersDBAdapter.close();
        
        
        AlertDialog.Builder addFolderAlertDialogBuilder = new AlertDialog.Builder(mContext);
        addFolderAlertDialogBuilder.setTitle(mResources.getText(R.string.new_folder)).setIcon(R.drawable.ic_new_folder);

        final EditText folderNameEditText = new EditText(mContext);
        folderNameEditText.setText("");
        folderNameEditText.setPadding(10, 5, 10, 5);

        addFolderAlertDialogBuilder.setView(folderNameEditText);
        
        
        addFolderAlertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener()	{
    		public void onClick(DialogInterface d, int which)
    		{
    			foldersDBAdapter.open();
	    			foldersDBAdapter.insertFolder(new Folder(folderNameEditText.getText().toString()));
	    	        Cursor foldersCursor = foldersDBAdapter.getCursor();
	    	        foldersCursorAdapter.changeCursor(foldersCursor);
    	        foldersDBAdapter.close();
    	        mAddFolderAlertDialog.dismiss();
    	        folderNameEditText.setText("");
    		}
    	});
        
        mAddFolderAlertDialog = addFolderAlertDialogBuilder.create();
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_ADD_FOLDER, 0, mResources.getText(R.string.new_folder)).setIcon(R.drawable.ic_new_folder);
        menu.add(0, MENU_MORE, 0, "more");
        return true;
    }

    /* Handles item selections */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
	        case MENU_ADD_FOLDER:
	        	try {
	        		mAddFolderAlertDialog.show();
	        	}catch (Exception e) {
	        		e.printStackTrace();
	        	}
	            return true;
	            
	        case MENU_MORE:
	        	Intent intent = new Intent(mContext, FoldersSelectableActivity.class);
	        	WallpapersTabActivityGroup._group.startChildActivity("FoldersSelectableActivity", intent);
        }
        return false;
    }

}