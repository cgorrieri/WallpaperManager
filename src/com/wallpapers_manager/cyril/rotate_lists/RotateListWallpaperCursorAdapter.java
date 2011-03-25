package com.wallpapers_manager.cyril.rotate_lists;

import java.io.File;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.wallpapers_manager.cyril.Helper;
import com.wallpapers_manager.cyril.R;
import com.wallpapers_manager.cyril.WallpaperManagerConstants;
import com.wallpapers_manager.cyril.wallpapers.Wallpaper;
import com.wallpapers_manager.cyril.wallpapers.WallpapersDBAdapter;

public class RotateListWallpaperCursorAdapter extends CursorAdapter {
	protected final LayoutInflater 	mInflater;
	protected final Context 		mContext;
	private Resources				mResources;
	
	public RotateListWallpaperCursorAdapter(Context context, Cursor cursor) {
		super(context, cursor);
		mInflater = LayoutInflater.from(context);
		mContext = context;
		mResources = mContext.getResources();
	}
	
	@Override
	public void bindView(final View view, Context context, final Cursor cursor) {
		final RotateListWallpaper rotateListWallpaper = new RotateListWallpaper(cursor.getInt(RotateListWallpapersDBAdapter.ID_IC),
				cursor.getInt(RotateListWallpapersDBAdapter.WALLPAPER_ID_IC), cursor.getInt(RotateListWallpapersDBAdapter.ROTATELIST_ID_IC));
		WallpapersDBAdapter wallpapersDBAdapter = new WallpapersDBAdapter(mContext);
        wallpapersDBAdapter.open();
        	final Wallpaper wallpaper = wallpapersDBAdapter.getWallpaper(rotateListWallpaper.getWallpaperId());
        wallpapersDBAdapter.close();

		File wallpaperFile = new File(WallpaperManagerConstants._registrationFilesDir, wallpaper.getAddress());
		Bitmap wallpaperBitmap = Helper._decodeFile(wallpaperFile);
		
		final ImageView wallpaperImageView = (ImageView) view.findViewById(R.id.wallpaper);
		wallpaperImageView.setImageBitmap(wallpaperBitmap);
		
		final CursorAdapter cursorAdapter = this;

		view.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				final CharSequence[] items = mResources.getTextArray(R.array.rotate_list_wallpaper_menu);

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
				alertDialogBuilder.setTitle(mResources.getText(R.string.actions));
				alertDialogBuilder.setItems(items, new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialogInterface, int item) {
				    	switch(item){
				    	case 0:
				    		RotateListWallpapersDBAdapter rotateListWallpapersDBAdapter = new RotateListWallpapersDBAdapter(mContext);
				            rotateListWallpapersDBAdapter.open();
					        	rotateListWallpapersDBAdapter.remove(rotateListWallpaper);
					            Cursor curs = rotateListWallpapersDBAdapter.getCursor(rotateListWallpaper.getRotateListId());
					            cursorAdapter.changeCursor(curs);
				            rotateListWallpapersDBAdapter.close();
				    		break;
				    	}
				    	
				    }
				});
				
				alertDialogBuilder.show();
			}
		});
	}
	
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return mInflater.inflate(R.layout.wallpaper, parent, false);
	}
}
