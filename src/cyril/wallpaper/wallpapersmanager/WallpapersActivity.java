package cyril.wallpaper.wallpapersmanager;


import cyril.wallpaper.R;
import cyril.wallpaper.WallpapersTabActivityGroup;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.TextView;

//public class WallpapersActivity extends ListActivity {
public class WallpapersActivity extends Activity {
	/* MENU */
	private static final int MENU_ADD_CURRENT_WALLPAPER = 0;
	private static final int MENU_SET_AND_ADD_NEW_WALLPAPER = 1;
	private static final int REQUEST_CODE = 0;
	
	private Context mContext;
	private Folder mFolder;
	private ProgressDialog dialog;
	private GetCurrentWallpaperThread getCurrentWallpaper;
	
	private GridView gridview;
	private TextView textview;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallpapers_grid);
        
        mContext = WallpapersTabActivityGroup.group;
        
        int folder_id = this.getIntent().getIntExtra("folder_id", 0);
        FoldersDBAdapter fdDBA = new FoldersDBAdapter(mContext);
        fdDBA.open();
        mFolder = fdDBA.getFolder(folder_id);
        fdDBA.close();

        final WallpapersDBAdapter wppDBA = new WallpapersDBAdapter(this);
        wppDBA.open();
        Cursor curs = wppDBA.getCursor(folder_id);
  
        this.textview = (TextView) findViewById(R.id.name);
        this.textview.setText(mFolder.getName());
        
        this.gridview = (GridView) findViewById(R.id.gridview);
        this.gridview.setAdapter(new WallpaperCursorAdapter(mContext,curs));
        
        this.dialog = ProgressDialog.show(mContext, "", "geting current wallpaper", true);
        this.dialog.cancel();
		
		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				wppDBA.close();
                wppDBA.open();
                gridview.setAdapter(new WallpaperCursorAdapter(mContext,wppDBA.getCursor(mFolder.getId())));
                dialog.dismiss();
			}
		};
        
		this.getCurrentWallpaper = new GetCurrentWallpaperThread(getWindowManager().getDefaultDisplay(), handler, mContext, mFolder.getId());
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add(0, MENU_ADD_CURRENT_WALLPAPER, 0, "Add curent wallpaper");
        menu.add(0, MENU_SET_AND_ADD_NEW_WALLPAPER, 0, "Set and add new");
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
	        	WallpapersTabActivityGroup.group.startActivityForResult(intent, REQUEST_CODE);
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
    	dialog.show();
    	try {
        	getCurrentWallpaper.start();
        }
        catch (IllegalThreadStateException e) {
        	getCurrentWallpaper.run();
        }
    }       
}