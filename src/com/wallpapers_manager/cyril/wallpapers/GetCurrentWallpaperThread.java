package com.wallpapers_manager.cyril.wallpapers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

import com.wallpapers_manager.cyril.WallpaperManagerConstants;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.Display;

public class GetCurrentWallpaperThread extends Thread {
	private Display mDisplay;
	private Handler mHandler;
	private Context mContext;
	private int mFolderId;
	
	public GetCurrentWallpaperThread(Display mDisplay, Handler mHandler, Context ctxt, int mFolderId)
	{
		super();
		this.setDisplay(mDisplay);
		this.setHandler(mHandler);
		this.setContext(ctxt);
		this.mFolderId = mFolderId;
	}

	public void setHandler(Handler mHandler) {
		this.mHandler = mHandler;
	}

	public Handler getHandler() {
		return mHandler;
	}

	public void run() {
		try {	
			WallpaperManager wallpaperManager = WallpaperManager.getInstance(this.mContext);
			
            Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        	
            long date = new Date().getTime();
            
            String filename = "wpp_"+date+".png";
            
			OutputStream fOut = null;
			File file = new File(WallpaperManagerConstants._registrationFilesDir, filename);
            fOut = new FileOutputStream(file);
            
            Bitmap wallpaperBitmap = Bitmap.createBitmap(wallpaperDrawable.getMinimumWidth(), wallpaperDrawable.getMinimumHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(wallpaperBitmap); 
            wallpaperDrawable.setBounds(0, 0, wallpaperDrawable.getMinimumWidth(), wallpaperDrawable.getMinimumHeight()); 
            wallpaperDrawable.draw(canvas);

            wallpaperBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
      
            WallpapersDBAdapter wppDBA = new WallpapersDBAdapter(this.mContext);
	        wppDBA.open();
			wppDBA.insertWallpaper(new Wallpaper(this.mFolderId, filename));
			wppDBA.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mHandler.sendEmptyMessage(0);
	}

	public void setContext(Context mContext) {
		this.mContext = mContext;
	}

	public Context getContext() {
		return mContext;
	}

	public void setDisplay(Display mDisplay) {
		this.mDisplay = mDisplay;
	}

	public Display getDisplay() {
		return mDisplay;
	}
}
