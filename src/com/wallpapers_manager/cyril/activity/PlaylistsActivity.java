package com.wallpapers_manager.cyril.activity;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CursorAdapter;
import android.widget.EditText;

import com.wallpapers_manager.cyril.R;
import com.wallpapers_manager.cyril.adapter.PlaylistsCursorAdapter;
import com.wallpapers_manager.cyril.bdd.PlaylistsDBAdapter;
import com.wallpapers_manager.cyril.data.Playlist;

/** Activity which contains the list of play list */
public class PlaylistsActivity extends ListActivity {
	/* Menu */
	private static final int 	MENU_NEW = 0;
	private static final int 	MENU_SETTINGS = 1;
	private static final int 	MENU_MORE = 2;
	
	private Context 			mContext;
	private Resources			mResources;
	private AlertDialog.Builder mAddPlaylistDialog;
	
    /** Called when the activity is first created */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlists_list);
        
        mContext = PlaylistsTabActivityGroup._group;
        mResources = mContext.getResources();
        
        // Get playlist list
        final PlaylistsDBAdapter playlistsDBAdapter = new PlaylistsDBAdapter(mContext);
        playlistsDBAdapter.open();
	        Cursor cursor = playlistsDBAdapter.getCursor();
	        final CursorAdapter cursorAdapter = new PlaylistsCursorAdapter(mContext,cursor);
	        setListAdapter(cursorAdapter);
        playlistsDBAdapter.close();
        
        // Create a dialog to add a new playlist
        mAddPlaylistDialog = new AlertDialog.Builder(mContext);
        mAddPlaylistDialog.setTitle(mResources.getText(R.string.new_playlist));

        final EditText rotateListNameEditText = new EditText(mContext);
        rotateListNameEditText.setText("");
        rotateListNameEditText.setPadding(10, 5, 10, 5);

        mAddPlaylistDialog.setView(rotateListNameEditText).setIcon(R.drawable.ic_new_playlist);
        
        mAddPlaylistDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface d, int which) {
    			playlistsDBAdapter.open();
	    			playlistsDBAdapter.insertPlaylist(new Playlist(rotateListNameEditText.getText().toString()));
	    	        Cursor cursor = playlistsDBAdapter.getCursor();
	    	        cursorAdapter.changeCursor(cursor);
    	        playlistsDBAdapter.close();
    		}
    	});
        
        // When a playlist is updated the playlist list is updated to
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if(intent.getAction().compareTo("com.wallpaper_manager.playlists.updatePlaylistCursor") == 0) {
					playlistsDBAdapter.open();
				        Cursor cursor = playlistsDBAdapter.getCursor();
				        cursorAdapter.changeCursor(cursor);
			        playlistsDBAdapter.close();
				}
			}
		};
		registerReceiver(broadcastReceiver, new IntentFilter("com.wallpaper_manager.playlists.updatePlaylistCursor"));
    }
    
    /** Create the menu */
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add(0, MENU_NEW, Menu.NONE, mResources.getText(R.string.new_playlist)).setIcon(R.drawable.ic_new_playlist);
    	menu.add(0, MENU_SETTINGS, Menu.NONE, mResources.getText(R.string.menu_settings)).setIcon(R.drawable.ic_settings);
    	menu.add(0, MENU_MORE, Menu.NONE, "more");
        return true;
    }

    /** Handles item selections in the menu */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
	        case MENU_NEW:
	        	mAddPlaylistDialog.show();
	            return true;
	        case MENU_SETTINGS:
	        	Intent settings = new Intent(mContext, PlaylistsSettingActivity.class);
	    		startActivity(settings);
	            return true;
	        case MENU_MORE:
	        	Intent selectable = new Intent(mContext, PlaylistsSelectableActivity.class);
	    		PlaylistsTabActivityGroup._group.startChildActivity(
	    				"PlaylistsSelectableActivity",
	    				selectable.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	            return true;
        }
        return false;
    }
}