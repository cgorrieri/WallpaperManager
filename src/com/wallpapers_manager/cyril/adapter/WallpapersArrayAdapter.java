package com.wallpapers_manager.cyril.adapter;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.wallpapers_manager.cyril.R;
import com.wallpapers_manager.cyril.WallpaperManagerConstants;
import com.wallpapers_manager.cyril.data.Wallpaper;

public class WallpapersArrayAdapter extends ArrayAdapter<Wallpaper> {
	protected LayoutInflater 		mInflater;
	protected Context 				mContext;

	public WallpapersArrayAdapter(Context context, List<Wallpaper> objects) {
		super(context, 0, objects);
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		final Wallpaper wallpaper = this.getItem(position);
		View view = convertView;
		if(view == null)
	        view = mInflater.inflate(R.layout.wallpaper_selectable, null);
		
		File wallpaperFile = new File(WallpaperManagerConstants._registrationFilesDir, wallpaper.getAddress());
		Bitmap wallpaperBitmap = BitmapFactory.decodeFile(wallpaperFile.getAbsolutePath());
		
		final ImageView wallpaperImageView = (ImageView) view.findViewById(R.id.wallpaper);
		wallpaperImageView.setImageBitmap(wallpaperBitmap);
		
		return view;
	}
	
	@Override
	public long getItemId(int position) {
		return getItem(position).getId();
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}
}
