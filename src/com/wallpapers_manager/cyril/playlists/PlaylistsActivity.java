package com.wallpapers_manager.cyril.playlists;

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
import com.wallpapers_manager.cyril.PlaylistsTabActivityGroup;

public class PlaylistsActivity extends ListActivity {
	/* Menu */
	private static final int 	MENU_NEW = 0;
	private static final int 	MENU_SETTINGS = 1;
	
	private Context 			mContext;
	private Resources			mResources;
	private AlertDialog.Builder mAddPlaylistDialog;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlists_list);
        
        mContext = PlaylistsTabActivityGroup._group;
        mResources = mContext.getResources();
        
        final PlaylistsDBAdapter playlistsDBAdapter = new PlaylistsDBAdapter(mContext);
        playlistsDBAdapter.open();
	        Cursor cursor = playlistsDBAdapter.getCursor();
	        final CursorAdapter cursorAdapter = new PlaylistsCursorAdapter(mContext,cursor);
	        setListAdapter(cursorAdapter);
        playlistsDBAdapter.close();
        
        mAddPlaylistDialog = new AlertDialog.Builder(mContext);
        mAddPlaylistDialog.setTitle(mResources.getText(R.string.new_playlist));

        final EditText rotateListNameEditText = new EditText(mContext);
        rotateListNameEditText.setText("");
        rotateListNameEditText.setPadding(10, 5, 10, 5);

        mAddPlaylistDialog.setView(rotateListNameEditText).setIcon(R.drawable.ic_new_playlist);
        
        
        mAddPlaylistDialog.setPositiveButton("OK", new DialogInterface.OnClickListener()	{
    		public void onClick(DialogInterface d, int which)
    		{
    			playlistsDBAdapter.open();
	    			playlistsDBAdapter.insertPlaylist(new Playlist(rotateListNameEditText.getText().toString()));
	    	        Cursor cursor = playlistsDBAdapter.getCursor();
	    	        cursorAdapter.changeCursor(cursor);
    	        playlistsDBAdapter.close();
    		}
    	});
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add(0, MENU_NEW, Menu.NONE, mResources.getText(R.string.new_playlist)).setIcon(R.drawable.ic_new_playlist);
    	menu.add(0, MENU_SETTINGS, Menu.NONE, mResources.getText(R.string.menu_settings)).setIcon(R.drawable.ic_settings);
        return true;
    }

    /* Handles item selections */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
	        case MENU_NEW:
	        	mAddPlaylistDialog.show();
	            return true;
	        case MENU_SETTINGS:
	        	Intent settings = new Intent(mContext, PlaylistsSettingActivity.class);
	    		startActivity(settings);
	            return true;
        }
        return false;
    }
}