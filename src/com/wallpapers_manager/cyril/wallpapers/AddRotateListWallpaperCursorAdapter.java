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
	private Wallpaper wpp;
	private Folder fd;
	private Dialog dg;
	protected final LayoutInflater mInflater;
	protected final Context mContext;
	
	public AddRotateListWallpaperCursorAdapter(Context context, Cursor cursor, Wallpaper wpp, Dialog dg) {
		super(context, cursor);
		mInflater = LayoutInflater.from(context);
		mContext = context;
		this.wpp = wpp;
		this.dg = dg;
	}
	
	public AddRotateListWallpaperCursorAdapter(Context context, Cursor cursor, Folder fd, Dialog dg) {
		super(context, cursor);
		mInflater = LayoutInflater.from(context);
		mContext = context;
		this.fd = fd;
		this.dg = dg;
	}
	
	@Override
	public void bindView(final View view, Context context, final Cursor cursor) {
		final RotateList rtl = new RotateList(cursor.getInt(0),	cursor.getString(1), cursor.getInt(2));

		ImageView image_view = (ImageView) view.findViewById(R.id.image);
		image_view.setImageResource(rtl.isSelected() ? R.drawable.selected_rotate_list : R.drawable.rotate_list);

		TextView name_view = (TextView) view.findViewById(R.id.name);
		name_view.setText(cursor.getString(1));

		view.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				RotateListWallpapersDBAdapter rtlWppDBA = new RotateListWallpapersDBAdapter(mContext);
				rtlWppDBA.open();
				if(wpp != null) {
					RotateListWallpaper rtlWpp = new RotateListWallpaper(wpp.getId(), rtl.getId());
					rtlWppDBA.insertRotateListWallpaper(rtlWpp);
				} else {
					rtlWppDBA.insertRotateListWallpaperForFolder(fd, rtl);
				}
				rtlWppDBA.close();
				dg.dismiss();
			}
		});
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return mInflater.inflate(R.layout.rotate_list, parent, false);
	}
}
