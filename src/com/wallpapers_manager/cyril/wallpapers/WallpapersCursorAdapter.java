package com.wallpapers_manager.cyril.wallpapers;

import java.io.File;
import java.io.IOException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.wallpapers_manager.cyril.Helper;
import com.wallpapers_manager.cyril.R;
import com.wallpapers_manager.cyril.WallpaperManagerConstants;
import com.wallpapers_manager.cyril.folders.FoldersDBAdapter;
import com.wallpapers_manager.cyril.playlists.PlaylistsDBAdapter;

public class WallpapersCursorAdapter extends CursorAdapter {
	protected final LayoutInflater 	mInflater;
	protected final Context 		mContext;
	private Resources				mResources;
	private CursorAdapter			mCursorAdapter;

	public WallpapersCursorAdapter(Context context, Cursor cursor) {
		super(context, cursor);
		mInflater = LayoutInflater.from(context);
		mContext = context;
		mResources = mContext.getResources();
	}

	@Override
	public void bindView(final View view, Context context, final Cursor cursor) {
		final Wallpaper wallpaper = new Wallpaper(cursor);
		// Toast.makeText(mContext, wallpaper.toString(), 1).show();
		File wallpaperFile = new File(WallpaperManagerConstants._registrationFilesDir, wallpaper.getAddress());
		Bitmap wallpaperBitmap = Helper._decodeFile(wallpaperFile);
		
		final ImageView wallpaperImageView = (ImageView) view.findViewById(R.id.wallpaper);
		wallpaperImageView.setImageBitmap(wallpaperBitmap);

		final ProgressDialog progressDialog = ProgressDialog.show(mContext, "", mResources.getText(R.string.setting_up_wallpaper), true);
		progressDialog.cancel();
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				progressDialog.dismiss();
			}
		};
		
		final Thread updatePhoneWallpaper = new Thread() {
			public void run() {
				try {
					File wallpaperFile = new File(WallpaperManagerConstants._registrationFilesDir, wallpaper.getAddress());
					Bitmap wallpaperBitmap = BitmapFactory.decodeFile(wallpaperFile.getAbsolutePath());
					
					WallpaperManager wallpaperManager = WallpaperManager.getInstance(mContext);
					wallpaperManager.setBitmap(wallpaperBitmap);
				} catch (IOException e) {
					e.printStackTrace();
				}
				handler.sendEmptyMessage(0);
			}
		};
		
		mCursorAdapter = this;

		view.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				
				final CharSequence[] items = mResources.getTextArray(R.array.wallpaper_menu);
				
				final Wallpaper wallpaperBis = wallpaper; 

				final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
				alertDialogBuilder.setTitle(mResources.getText(R.string.actions));
				alertDialogBuilder.setItems(items, new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialogInterface, int item) {
				    	Dialog dialog = new Dialog(mContext);
				    	Cursor cursor = null;
				    	CursorAdapter cursorAdapter = null;
				    	switch(item){
				    	case 0: // put it
				    		progressDialog.show();
				    		try {
				    			updatePhoneWallpaper.start();
				    		}
			                catch (IllegalThreadStateException e) {
			                	updatePhoneWallpaper.run();
			                }
				    		break;
				    	case 1:	// Add to rotate list	    		
				    		final PlaylistsDBAdapter playlistsDBAdapter = new PlaylistsDBAdapter(mContext);
				            playlistsDBAdapter.open();
					            cursor = playlistsDBAdapter.getCursor();
					    		ListView lstA = new ListView(mContext);
					    		cursorAdapter = new AddWallpaperInPlaylistCursorAdapter(mContext, cursor, wallpaperBis, dialog);
					    		lstA.setAdapter(cursorAdapter);
				    		playlistsDBAdapter.close();
				    		dialog.setContentView(lstA);
				    		dialog.setTitle(items[1]);
				    		dialog.show();
				    		break;
				    	case 2: // Move to
				    		final FoldersDBAdapter foldersDBAdapter = new FoldersDBAdapter(mContext);
				    		foldersDBAdapter.open();
					    		cursor = foldersDBAdapter.getCursor();
					    		ListView listView2 = new ListView(mContext);
					    		cursorAdapter = new AddWallpaperCursorAdapter(mContext, cursor, wallpaperBis, dialog);
					    		listView2.setAdapter(cursorAdapter);
					    	foldersDBAdapter.close();
				    		dialog.setContentView(listView2);
				    		dialog.setTitle(items[2]);
				    		dialog.show();
				    		break;
				    	case 3: // Copy in
				    		final FoldersDBAdapter foldersDBAdapter2 = new FoldersDBAdapter(mContext);
				    		foldersDBAdapter2.open();
					    		cursor = foldersDBAdapter2.getCursor();
					    		ListView listView3 = new ListView(mContext);
					    		cursorAdapter = new AddWallpaperCursorAdapter(mContext, cursor, wallpaperBis, dialog, true);
					    		listView3.setAdapter(cursorAdapter);
				    		foldersDBAdapter2.close();
				    		dialog.setContentView(listView3);
				    		dialog.setTitle(items[3]);
				    		dialog.show();
				    		break;
				    	case 4: // Delete
				    		WallpapersDBAdapter wppDBA = new WallpapersDBAdapter(mContext);
				            wppDBA.open();
					        	wallpaper.delete(wppDBA);
					        	cursor = wppDBA.getCursor(wallpaper.getFolderId());
					        	mCursorAdapter.changeCursor(cursor);
				            wppDBA.close();
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
