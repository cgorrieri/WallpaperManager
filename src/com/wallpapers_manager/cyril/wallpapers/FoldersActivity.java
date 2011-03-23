package com.wallpapers_manager.cyril.wallpapers;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
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
	private static final int MENU_ADD_FOLDER = 0;
	private static final int MENU_QUIT = 1;
	private AlertDialog.Builder add_folder_dl;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.folders_list);
        
        // Create the wallpaper directory if not already exist
        WallpaperManagerConstants.makeRegistrationFilesDir();
        
//        stopService(new Intent(this, RotateWallpaperService.class));
//        startService(new Intent(this, RotateWallpaperService.class));   
        
        final Context ctxt = WallpapersTabActivityGroup.group;
        
        final FoldersDBAdapter fdsDBA = new FoldersDBAdapter(this);
        fdsDBA.open();
        Cursor curs = fdsDBA.getCursor();
        final CursorAdapter adapter = new FoldersCursorAdapter(ctxt,curs);
        setListAdapter(adapter);
        fdsDBA.close();
        
        add_folder_dl = new AlertDialog.Builder(ctxt);
        add_folder_dl.setTitle("New Folder").setIcon(R.drawable.new_folder);

        final EditText folder_name_box = new EditText(ctxt);
        folder_name_box.setText("");
        folder_name_box.setPadding(10, 5, 10, 5);

        add_folder_dl.setView(folder_name_box);
        
        
        add_folder_dl.setPositiveButton("OK", new DialogInterface.OnClickListener()	{
    		public void onClick(DialogInterface d, int which)
    		{
    			fdsDBA.open();
    			fdsDBA.insertFolder(new Folder(folder_name_box.getText().toString()));
    	        Cursor curs = fdsDBA.getCursor();
    	        adapter.changeCursor(curs);
    	        fdsDBA.close();
    		}
    	});
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_ADD_FOLDER, 0, "New Folder").setIcon(R.drawable.new_folder);
        menu.add(0, MENU_QUIT, 0, "Quit").setIcon(R.drawable.quit);
        return true;
    }

    /* Handles item selections */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
	        case MENU_ADD_FOLDER:
	        	add_folder_dl.show();
	            return true;
	        case MENU_QUIT:
	            finish();
	            return true;
        }
        return false;
    }

}