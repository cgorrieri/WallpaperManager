package com.wallpapers_manager.cyril.rotate_lists;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.GridView;

import com.wallpapers_manager.cyril.R;
import com.wallpapers_manager.cyril.RotateListsTabActivityGroup;

public class RotateListWallpapersActivity extends Activity {
	private Context mContext;
	
	private int rotate_list_id;
	
	private GridView gridview;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallpapers_grid);
        
        mContext = RotateListsTabActivityGroup.group;
        
        rotate_list_id = this.getIntent().getIntExtra("rotate_list_id", 0);

        final RotateListWallpapersDBAdapter rtlWppDBA = new RotateListWallpapersDBAdapter(this);
        rtlWppDBA.open();
        Cursor curs = rtlWppDBA.getCursor(rotate_list_id);
  
        this.gridview = (GridView) findViewById(R.id.gridview);
        this.gridview.setAdapter(new RotateListWallpaperCursorAdapter(mContext, curs));
    }
}