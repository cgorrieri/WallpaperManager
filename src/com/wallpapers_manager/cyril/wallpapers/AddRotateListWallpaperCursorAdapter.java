package com.wallpapers_manager.cyril.wallpapers;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wallpapers_manager.cyril.R;
import com.wallpapers_manager.cyril.rotate_lists.RotateList;
import com.wallpapers_manager.cyril.rotate_lists.RotateListWallpaper;
import com.wallpapers_manager.cyril.rotate_lists.RotateListWallpapersDBAdapter;

public class AddRotateListWallpaperCursorAdapter extends CursorAdapter {
	protected final LayoutInflater 	mInflater;
	protected final Context 		mContext;
	
	private Wallpaper 	mWallpaper;
	private Folder 		mFolder;
	private Dialog 		mDialog;
	
	public AddRotateListWallpaperCursorAdapter(Context context, Cursor cursor, Wallpaper wpp, Dialog dg) {
		super(context, cursor);
		mInflater = LayoutInflater.from(context);
		mContext = context;
		mWallpaper = wpp;
		mDialog = dg;
	}
	
	public AddRotateListWallpaperCursorAdapter(Context context, Cursor cursor, Folder fd, Dialog dg) {
		super(context, cursor);
		mInflater = LayoutInflater.from(context);
		mContext = context;
		mFolder = fd;
		mDialog = dg;
	}
	
	@Override
	public void bindView(final View view, Context context, final Cursor cursor) {
		final RotateList rotateList = new RotateList(cursor.getInt(0),	cursor.getString(1), cursor.getInt(2));

		ImageView imageView = (ImageView) view.findViewById(R.id.image);
		imageView.setImageResource(rotateList.isSelected() ? R.drawable.selected_rotate_list : R.drawable.rotate_list);

		TextView nameTextView = (TextView) view.findViewById(R.id.name);
		nameTextView.setText(cursor.getString(1));

		view.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				RotateListWallpapersDBAdapter rotateListWallpapersDBAdapter = new RotateListWallpapersDBAdapter(mContext);
				rotateListWallpapersDBAdapter.open();
				if(mWallpaper != null) {
					RotateListWallpaper rotateListWallpaper = new RotateListWallpaper(mWallpaper.getId(), rotateList.getId());
					rotateListWallpapersDBAdapter.insertRotateListWallpaper(rotateListWallpaper);
				} else {
					rotateListWallpapersDBAdapter.insertRotateListWallpaperForFolder(mFolder, rotateList);
				}
				rotateListWallpapersDBAdapter.close();
				mDialog.dismiss();
			}
		});
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return mInflater.inflate(R.layout.rotate_list, parent, false);
	}
}
