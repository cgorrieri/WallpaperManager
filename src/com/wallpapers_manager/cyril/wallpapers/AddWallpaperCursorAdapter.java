package com.wallpapers_manager.cyril.wallpapers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wallpapers_manager.cyril.R;
import com.wallpapers_manager.cyril.WallpaperManagerConstants;
import com.wallpapers_manager.cyril.WallpapersTabActivityGroup;
import com.wallpapers_manager.cyril.folders.Folder;

public class AddWallpaperCursorAdapter extends CursorAdapter {
	protected final LayoutInflater 	mInflater;
	protected final Context 		mContext;
	
	private Wallpaper 	mWallpaper;
	private Dialog 		mDialog;
	private boolean 	mCopy;
	
	public AddWallpaperCursorAdapter(Context context, Cursor cursor, Wallpaper wpp, Dialog dg) {
		this(context, cursor, wpp, dg, false);
	}
	
	public AddWallpaperCursorAdapter(Context context, Cursor cursor, Wallpaper wpp, Dialog dg, boolean copy) {
		super(context, cursor);
		mInflater = LayoutInflater.from(context);
		mContext = context;
		mWallpaper = wpp;
		mDialog = dg;
		mCopy = copy;
	}
	
	@Override
	public void bindView(final View view, Context context, final Cursor cursor) {
		final Folder folder = new Folder(cursor.getInt(0),	cursor.getString(1));

		ImageView imageView = (ImageView) view.findViewById(R.id.image);
		imageView.setImageResource(R.drawable.folder);

		TextView nameTextView = (TextView) view.findViewById(R.id.name);
		nameTextView.setText(cursor.getString(1));

		view.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				WallpapersDBAdapter wallpapersDBAdapter = new WallpapersDBAdapter(mContext);
				mWallpaper.setFolderId(folder.getId());
				wallpapersDBAdapter.open();
					if(mCopy) {
						String oldName = mWallpaper.getAddress();
						mWallpaper.setAddress(mWallpaper.getAddress().replace(".", "c."));
						copy_file(oldName, mWallpaper.getAddress());
						wallpapersDBAdapter.insertWallpaper(mWallpaper);
					} else {
						wallpapersDBAdapter.updateWallpaper(mWallpaper);
					}
				wallpapersDBAdapter.close();
				mDialog.dismiss();
				
				
				WallpapersTabActivityGroup._group.finishFromChild(WallpapersTabActivityGroup._group.getCurrentActivity());
				Intent wallpapers = new Intent(mContext, WallpapersActivity.class);
				wallpapers.putExtra("folder_id", mWallpaper.getFolderId());
				WallpapersTabActivityGroup._group.startChildActivity("WallpapersActivity", wallpapers.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			}
		});
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return mInflater.inflate(R.layout.folder, parent, false);
	}
	
	private void copy_file(String name, String nameDest) {
		File file = new File(WallpaperManagerConstants._registrationFilesDir, name);
		File fileDest = new File(WallpaperManagerConstants._registrationFilesDir, nameDest);

		OutputStream fos;
		FileInputStream fis;
		try {
			fos = new FileOutputStream(fileDest);
			fis = new FileInputStream(file);
			Bitmap res = BitmapFactory.decodeStream(fis);
	        fis.close();
			res.compress(Bitmap.CompressFormat.PNG, 100, fos);
	        fos.flush();
	        fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
