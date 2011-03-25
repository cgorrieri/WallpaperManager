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
import com.wallpapers_manager.cyril.playlists.Playlist;
import com.wallpapers_manager.cyril.playlists.WallpaperPlaylist;
import com.wallpapers_manager.cyril.playlists.WallpapersPlaylistDBAdapter;

public class AddWallpaperInPlaylistCursorAdapter extends CursorAdapter {
	protected final LayoutInflater 	mInflater;
	protected final Context 		mContext;
	
	private Wallpaper 	mWallpaper;
	private Folder 		mFolder;
	private Dialog 		mDialog;
	
	public AddWallpaperInPlaylistCursorAdapter(Context context, Cursor cursor, Wallpaper wpp, Dialog dg) {
		super(context, cursor);
		mInflater = LayoutInflater.from(context);
		mContext = context;
		mWallpaper = wpp;
		mDialog = dg;
	}
	
	public AddWallpaperInPlaylistCursorAdapter(Context context, Cursor cursor, Folder fd, Dialog dg) {
		super(context, cursor);
		mInflater = LayoutInflater.from(context);
		mContext = context;
		mFolder = fd;
		mDialog = dg;
	}
	
	@Override
	public void bindView(final View view, Context context, final Cursor cursor) {
		final Playlist playlist = new Playlist(cursor.getInt(0),	cursor.getString(1), cursor.getInt(2));

		ImageView imageView = (ImageView) view.findViewById(R.id.image);
		imageView.setImageResource(playlist.isSelected() ? R.drawable.selected_playlist : R.drawable.playlist);

		TextView nameTextView = (TextView) view.findViewById(R.id.name);
		nameTextView.setText(cursor.getString(1));

		view.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				WallpapersPlaylistDBAdapter wallpapersPlaylistDBAdapter = new WallpapersPlaylistDBAdapter(mContext);
				wallpapersPlaylistDBAdapter.open();
					if(mWallpaper != null) {
						WallpaperPlaylist wallpaperPlaylist = new WallpaperPlaylist(mWallpaper.getId(), playlist.getId());
						wallpapersPlaylistDBAdapter.insertWallpaperPlaylist(wallpaperPlaylist);
					} else
						wallpapersPlaylistDBAdapter.insertPlaylistWallpaperForFolder(mFolder, playlist);
				wallpapersPlaylistDBAdapter.close();
				mDialog.dismiss();
			}
		});
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return mInflater.inflate(R.layout.playlist, parent, false);
	}
}
