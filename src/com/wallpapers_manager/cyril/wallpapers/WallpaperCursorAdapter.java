package com.wallpapers_manager.cyril.wallpapers;

import java.io.File;
import java.io.IOException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
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
import com.wallpapers_manager.cyril.rotate_lists.RotateListsDBAdapter;

public class WallpaperCursorAdapter extends CursorAdapter {
	protected final LayoutInflater mInflater;
	protected final Context mContext;

	public WallpaperCursorAdapter(Context context, Cursor cursor) {
		super(context, cursor);
		mInflater = LayoutInflater.from(context);
		mContext = context;
	}

	@Override
	public void bindView(final View view, Context context, final Cursor cursor) {
		final Wallpaper wpp = this.getWallpaperFromCursor(cursor);
		// Toast.makeText(mContext, wpp.toString(), 1).show();
		File wallpaperFile = new File(WallpaperManagerConstants.registrationFilesDir, wpp.getAddress());
		Bitmap wallpaperBitmap = Helper.decodeFile(wallpaperFile);
		
		final ImageView wallpaper = (ImageView) view.findViewById(R.id.wallpaper);
		wallpaper.setImageBitmap(wallpaperBitmap);

		final ProgressDialog p_dialog = ProgressDialog.show(mContext, "", "Mise en place du fond d'ecran", true);
		p_dialog.cancel();
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				p_dialog.dismiss();
			}
		};
		
		final Thread updatePhoneWallpaper = new Thread() {
			public void run() {
				try {
					File wallpaperFile = new File(WallpaperManagerConstants.registrationFilesDir, wpp.getAddress());
					Bitmap wallpaperBitmap = BitmapFactory.decodeFile(wallpaperFile.getAbsolutePath());
					
					WallpaperManager wallpaperManager = WallpaperManager.getInstance(wallpaper.getContext());
					wallpaperManager.setBitmap(wallpaperBitmap);
				} catch (IOException e) {
					e.printStackTrace();
				}
				handler.sendEmptyMessage(0);
			}
		};
		
		final CursorAdapter cursor_adp = this;

		view.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				final CharSequence[] items = {"Apply", "Add to playlist", "Move to", "Copy in", "Delete"};
				
				final Wallpaper wp = wpp; 

				final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setTitle("Actions");
				builder.setItems(items, new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int item) {
				    	switch(item){
				    	case 0:
				    		p_dialog.show();
				    		try {
				    			updatePhoneWallpaper.start();
				    		}
			                catch (IllegalThreadStateException e) {
			                	updatePhoneWallpaper.run();
			                }
				    		break;
				    	case 1:
				    		final Dialog dg = new Dialog(mContext);
				    		
				    		final RotateListsDBAdapter rtlDBA = new RotateListsDBAdapter(mContext);
				            rtlDBA.open();
				            Cursor cur = rtlDBA.getCursor();
				    		ListView lstA = new ListView(mContext);
				    		CursorAdapter ca = new AddRotateListWallpaperCursorAdapter(mContext, cur, wp, dg);
				    		lstA.setAdapter(ca);
				    		dg.setContentView(lstA);
				    		dg.setTitle("Add To");
				    		dg.show();
				    		break;
				    	case 2:
				    		final Dialog dg2 = new Dialog(mContext);
				    		
				    		final FoldersDBAdapter fdDBA = new FoldersDBAdapter(mContext);
				    		fdDBA.open();
				            Cursor cur2 = fdDBA.getCursor();
				    		ListView lstA2 = new ListView(mContext);
				    		CursorAdapter ca2 = new AddWallpaperCursorAdapter(mContext, cur2, wp, dg2);
				    		lstA2.setAdapter(ca2);
				    		dg2.setContentView(lstA2);
				    		dg2.setTitle("Move To");
				    		dg2.show();
				    		fdDBA.close();
				    		break;
				    	case 3:
				    		final Dialog dg3 = new Dialog(mContext);
				    		
				    		final FoldersDBAdapter fdDBA2 = new FoldersDBAdapter(mContext);
				    		fdDBA2.open();
				            Cursor cur3 = fdDBA2.getCursor();
				    		ListView lstA3 = new ListView(mContext);
				    		CursorAdapter ca3 = new AddWallpaperCursorAdapter(mContext, cur3, wp, dg3, true);
				    		lstA3.setAdapter(ca3);
				    		fdDBA2.close();
				    		dg3.setContentView(lstA3);
				    		dg3.setTitle("Copy in");
				    		dg3.show();
				    		break;
				    	case 4:
				    		WallpapersDBAdapter wppDBA = new WallpapersDBAdapter(mContext);
				            wppDBA.open();
					        	wpp.delete(wppDBA);
					            Cursor curs = wppDBA.getCursor(wpp.getFolderId());
					            cursor_adp.changeCursor(curs);
				            wppDBA.close();
				    		break;
				    	}
				    	
				    }
				});
				
				builder.show();
			}
		});
	}
	
	private Wallpaper getWallpaperFromCursor(Cursor cursor) {
		return new Wallpaper(cursor.getInt(0), cursor.getInt(1), cursor.getString(2));
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return mInflater.inflate(R.layout.wallpaper, parent, false);
	}
}
