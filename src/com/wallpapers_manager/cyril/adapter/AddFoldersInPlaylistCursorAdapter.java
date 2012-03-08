package com.wallpapers_manager.cyril.adapter;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wallpapers_manager.cyril.R;
import com.wallpapers_manager.cyril.bdd.WallpapersPlaylistDBAdapter;
import com.wallpapers_manager.cyril.data.Folder;
import com.wallpapers_manager.cyril.data.Playlist;
import static com.wallpapers_manager.cyril.WallpaperManagerConstants.*;

public class AddFoldersInPlaylistCursorAdapter extends CursorAdapter {
	protected final LayoutInflater 	mInflater;
	protected final Context 		mContext;
	
	private ArrayList<Folder> 		mFolderList;
	private Dialog 					mDialog;
	private Handler					mHandler;
	
	public AddFoldersInPlaylistCursorAdapter(Context context, Cursor cursor, Folder folder, Dialog dg) {
		super(context, cursor);
		mInflater = LayoutInflater.from(context);
		mContext = context;
		mFolderList = new ArrayList<Folder>();
		mFolderList.add(folder);
		mDialog = dg;
	}
	
	public AddFoldersInPlaylistCursorAdapter(Context context, Cursor cursor, ArrayList<Folder> folderList, Dialog dg, Handler handler) {
		super(context, cursor);
		mInflater = LayoutInflater.from(context);
		mContext = context;
		mFolderList = new ArrayList<Folder>(folderList);
		mDialog = dg;
		mHandler = handler;
	}
	
	@Override
	public void bindView(final View view, Context context, final Cursor cursor) {
		final Playlist playlist = new Playlist(cursor.getInt(0), cursor.getString(1), cursor.getInt(2));

		ImageView imageView = (ImageView) view.findViewById(R.id.image);
		imageView.setImageResource(playlist.isSelected() ? R.drawable.selected_playlist : R.drawable.playlist);

		TextView nameTextView = (TextView) view.findViewById(R.id.name);
		nameTextView.setText(cursor.getString(1));

		view.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				WallpapersPlaylistDBAdapter wallpapersPlaylistDBAdapter = new WallpapersPlaylistDBAdapter(mContext);
				wallpapersPlaylistDBAdapter.open();
					for(Folder folder : mFolderList)
						wallpapersPlaylistDBAdapter.insertPlaylistWallpaperForFolder(folder, playlist);
				wallpapersPlaylistDBAdapter.close();
				Intent intentBroadcast = new Intent(BROADCAST_UPDATE_WPP_PL);
				mContext.sendBroadcast(intentBroadcast);
				if(mHandler != null)
					mHandler.sendEmptyMessage(0);
				mDialog.dismiss();
			}
		});
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return mInflater.inflate(R.layout.playlist, parent, false);
	}
}
