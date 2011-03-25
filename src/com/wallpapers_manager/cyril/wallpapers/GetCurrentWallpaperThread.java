package com.wallpapers_manager.cyril.wallpapers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;

import com.wallpapers_manager.cyril.WallpaperManagerConstants;

public class GetCurrentWallpaperThread extends Thread {
	private Handler mHandler;
	private Context mContext;
	private int mFolderId;
	
	public GetCurrentWallpaperThread(Handler handler, Context context, int folderId)
	{
		super();
		mHandler = handler;
		mContext = context;
		mFolderId = folderId;
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
            
            Bitmap wallpaperBitmap = Bitmap.createBitmap(wallpaperDrawable.getMinimumWidth(), wallpaperDrawable.getMinimumHeight(), Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(wallpaperBitmap); 
            wallpaperDrawable.setBounds(0, 0, wallpaperDrawable.getMinimumWidth(), wallpaperDrawable.getMinimumHeight()); 
            wallpaperDrawable.draw(canvas);

            wallpaperBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
      
            WallpapersDBAdapter wallpapersDBAdapter = new WallpapersDBAdapter(mContext);
	        wallpapersDBAdapter.open();
				wallpapersDBAdapter.insertWallpaper(new Wallpaper(mFolderId, filename));
			wallpapersDBAdapter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mHandler.sendEmptyMessage(0);
	}
}
