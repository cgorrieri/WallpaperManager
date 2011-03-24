package com.wallpapers_manager.cyril.rotate_lists;

import com.wallpapers_manager.cyril.R;
import com.wallpapers_manager.cyril.RotateListsTabActivityGroup;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CursorAdapter;
import android.widget.EditText;

public class RotateListsActivity extends ListActivity {
	/* Menu */
	private static final int 	MENU_NEW = 0;
	private static final int 	MENU_SETTINGS = 1;
	
	private Context 			mContext;
	private AlertDialog.Builder mAddRotateListDialog;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rotate_lists_list);
        
        mContext = RotateListsTabActivityGroup._group;
        
        final RotateListsDBAdapter rotateListsDBAdapter = new RotateListsDBAdapter(mContext);
        rotateListsDBAdapter.open();
        Cursor cursor = rotateListsDBAdapter.getCursor();
        final CursorAdapter cursorAdapter = new RotateListsCursorAdapter(mContext,cursor);
        setListAdapter(cursorAdapter);
        rotateListsDBAdapter.close();
        
        mAddRotateListDialog = new AlertDialog.Builder(mContext);
        mAddRotateListDialog.setTitle("New Rotate List");

        final EditText rotateListNameEditText = new EditText(mContext);
        rotateListNameEditText.setText("");
        rotateListNameEditText.setPadding(10, 5, 10, 5);

        mAddRotateListDialog.setView(rotateListNameEditText).setIcon(R.drawable.ic_new_rotate_list);
        
        
        mAddRotateListDialog.setPositiveButton("OK", new DialogInterface.OnClickListener()	{
    		public void onClick(DialogInterface d, int which)
    		{
    			rotateListsDBAdapter.open();
    			rotateListsDBAdapter.insertRotateList(new RotateList(rotateListNameEditText.getText().toString()));
    	        Cursor cursor = rotateListsDBAdapter.getCursor();
    	        cursorAdapter.changeCursor(cursor);
    	        rotateListsDBAdapter.close();
    		}
    	});
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add(0, MENU_NEW, Menu.NONE, "New").setIcon(R.drawable.ic_new_rotate_list);
    	menu.add(0, MENU_SETTINGS, Menu.NONE, "Settings").setIcon(R.drawable.ic_settings);
        return true;
    }

    /* Handles item selections */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
	        case MENU_NEW:
	        	mAddRotateListDialog.show();
	            return true;
	        case MENU_SETTINGS:
	        	Intent settings = new Intent(mContext, RotateListSettingActivity.class);
	    		startActivity(settings);
	            return true;
        }
        return false;
    }
}