package com.wallpapers_manager.cyril.rotate_lists;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.widget.GridView;
import android.widget.TextView;

import com.wallpapers_manager.cyril.R;
import com.wallpapers_manager.cyril.RotateListsTabActivityGroup;

public class RotateListWallpapersActivity extends Activity {
	private Context mContext;
	
	private RotateList mRotateList;
	
	private GridView mGridView;
	private TextView mTextView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallpapers_grid);
        
        mContext = RotateListsTabActivityGroup._group;
        
        RotateListsDBAdapter rotateListsDBAdapter = new RotateListsDBAdapter(mContext);
        rotateListsDBAdapter.open();
        mRotateList = rotateListsDBAdapter.getRotateList(this.getIntent().getIntExtra("rotate_list_id", 0));

        final RotateListWallpapersDBAdapter rtlWppDBA = new RotateListWallpapersDBAdapter(this);
        rtlWppDBA.open();
        Cursor curs = rtlWppDBA.getCursor(mRotateList.getId());
        
        this.mTextView = (TextView) findViewById(R.id.name);
        this.mTextView.setText(mRotateList.getName());
  
        this.mGridView = (GridView) findViewById(R.id.gridview);
        this.mGridView.setAdapter(new RotateListWallpaperCursorAdapter(mContext, curs));
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    } 
}