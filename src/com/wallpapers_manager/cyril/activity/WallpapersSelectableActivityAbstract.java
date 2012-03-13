package com.wallpapers_manager.cyril.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wallpapers_manager.cyril.R;
import com.wallpapers_manager.cyril.adapter.WallpapersArrayAdapter;
import com.wallpapers_manager.cyril.data.Wallpaper;
import com.wallpapers_manager.cyril.widget.CheckableRelativeLayout;
import com.wallpapers_manager.cyril.widget.MultiSelectGridView;

/** An abstract class for wallpaper selectable in a grid */
public abstract class WallpapersSelectableActivityAbstract extends Activity {	
	protected Context 		mContext;
	
	protected MultiSelectGridView 	mGridView;
	private TextView 				mTextView;
	private CheckedTextView		mCheckedTextView;
	protected LinearLayout		mLayoutForButton;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallpapers_selectable_grid);
        
        mContext = getContext();
        
        initConteneur();
        
        mTextView = (TextView) findViewById(R.id.name);
        mTextView.setText(getConteneurName());

        mGridView = (MultiSelectGridView) findViewById(R.id.gridview);
        
        ArrayList<Wallpaper> wallpapersList = getWallpapers();
        
        mGridView.setAdapter(new WallpapersArrayAdapter(mContext, wallpapersList));
        
        mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				CheckableRelativeLayout checkableRelativeLayout = (CheckableRelativeLayout)view;
				if(checkableRelativeLayout.isChecked()) {
					checkableRelativeLayout.setChecked(false);
					if(mCheckedTextView.isChecked())
						mCheckedTextView.setChecked(false);
				} else
					checkableRelativeLayout.setChecked(true);
			}
		});
        
        mCheckedTextView = (CheckedTextView) findViewById(R.id.select_all);
		mCheckedTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mCheckedTextView.isChecked()) {
					clearSelection();
					mCheckedTextView.setChecked(false);
				} else {
					selectAll();
					mCheckedTextView.setChecked(true);
				}
			}
		});
		
		displayButtonAction();
    }
    
    /** Get the context of the activity */
    protected abstract Context getContext();

	/** Get the information of the conteneur */
    protected abstract void initConteneur();

    /** Get the list of the wallpaper selectable */
	protected abstract ArrayList<Wallpaper> getWallpapers();

	/** Get the name on the conteneur */
    protected abstract String getConteneurName();

    /** Add action button on the layout */
	protected abstract void displayButtonAction();

	/** Uncheck all the items */
	private void clearSelection() {
		final int itemCount = mGridView.getCount();
		for (int i = 0; i < itemCount; ++i) {
			mGridView.setItemChecked(i, false);
		}
	}
	
	/** Check all the items */
	private void selectAll() {
		final int itemCount = mGridView.getCount();
		for (int i = 0; i < itemCount; ++i) {
			mGridView.setItemChecked(i, true);
		}
	}
	
	/** */
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    /** Handles item selections */
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }
    
}