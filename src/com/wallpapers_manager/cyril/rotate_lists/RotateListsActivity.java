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
	private Context mContext;
	private AlertDialog.Builder add_rotate_list_dl;
	
	/* Menu */
	private static final int MENU_NEW = 0;
	private static final int MENU_SETTINGS = 1;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rotate_lists_list);
        
        mContext = RotateListsTabActivityGroup.group;
        
        final RotateListsDBAdapter rtlDBA = new RotateListsDBAdapter(mContext);
        rtlDBA.open();
        Cursor curs = rtlDBA.getCursor();
        final CursorAdapter adapter = new RotateListsCursorAdapter(mContext,curs);
        setListAdapter(adapter);
        rtlDBA.close();
        
        add_rotate_list_dl = new AlertDialog.Builder(mContext);
        add_rotate_list_dl.setTitle("New Rotate List");

        final EditText rotate_list_name_box = new EditText(mContext);
        rotate_list_name_box.setText("");
        rotate_list_name_box.setPadding(10, 5, 10, 5);

        add_rotate_list_dl.setView(rotate_list_name_box).setIcon(R.drawable.ic_new_rotate_list);
        
        
        add_rotate_list_dl.setPositiveButton("OK", new DialogInterface.OnClickListener()	{
    		public void onClick(DialogInterface d, int which)
    		{
    			rtlDBA.open();
    			rtlDBA.insertRotateList(new RotateList(rotate_list_name_box.getText().toString()));
    	        Cursor curs = rtlDBA.getCursor();
    	        adapter.changeCursor(curs);
    	        rtlDBA.close();
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
	        	add_rotate_list_dl.show();
	            return true;
	        case MENU_SETTINGS:
	        	Intent settings = new Intent(mContext, RotateListSettingActivity.class);
	    		startActivity(settings);
	            return true;
        }
        return false;
    }
}