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

public class AddWallpaperCursorAdapter extends CursorAdapter {
	private Wallpaper mWpp;
	private Dialog mDialog;
	protected final LayoutInflater mInflater;
	protected final Context mContext;
	private boolean mCopy;
	
	public AddWallpaperCursorAdapter(Context context, Cursor cursor, Wallpaper wpp, Dialog dg) {
		this(context, cursor, wpp, dg, false);
	}
	
	public AddWallpaperCursorAdapter(Context context, Cursor cursor, Wallpaper wpp, Dialog dg, boolean copy) {
		super(context, cursor);
		mInflater = LayoutInflater.from(context);
		mContext = context;
		mWpp = wpp;
		mDialog = dg;
		mCopy = copy;
	}
	
	@Override
	public void bindView(final View view, Context context, final Cursor cursor) {
		final Folder fd = new Folder(cursor.getInt(0),	cursor.getString(1));

		ImageView image_view = (ImageView) view.findViewById(R.id.image);
		image_view.setImageResource(R.drawable.folder);

		TextView name_view = (TextView) view.findViewById(R.id.name);
		name_view.setText(cursor.getString(1));

		view.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				WallpapersDBAdapter wppDBA = new WallpapersDBAdapter(mContext);
				mWpp.setFolderId(fd.getId());
				wppDBA.open();
				if(mCopy) {
					String old_name = mWpp.getAddress();
					mWpp.setAddress(mWpp.getAddress().replace(".", "c."));
					copy_file(old_name, mWpp.getAddress());
					wppDBA.insertWallpaper(mWpp);
				} else {
					wppDBA.updateWallpaper(mWpp);
				}
				wppDBA.close();
				mDialog.dismiss();
				
				
				WallpapersTabActivityGroup.group.finishFromChild(WallpapersTabActivityGroup.group.getCurrentActivity());
				Intent wallpapers = new Intent(mContext, WallpapersActivity.class);
				wallpapers.putExtra("folder_id", mWpp.getFolderId());
				WallpapersTabActivityGroup.group.startChildActivity(
						"WallpapersActivity",
						wallpapers.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			}
		});
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return mInflater.inflate(R.layout.folder, parent, false);
	}
	
	private void copy_file(String name, String dest_name)
	{
		
		File file = new File(WallpaperManagerConstants.registrationFilesDir, name);
		File file_dest = new File(WallpaperManagerConstants.registrationFilesDir, dest_name);

		OutputStream fos;
		FileInputStream fis;
		try {
			fos = new FileOutputStream(file_dest);
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
