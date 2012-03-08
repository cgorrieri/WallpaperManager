package com.wallpapers_manager.cyril.thread_and_service;

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
import com.wallpapers_manager.cyril.bdd.WallpapersDBAdapter;
import com.wallpapers_manager.cyril.data.Wallpaper;

/**
 * A thread who get the current system wallpaper and add it in the current directory
 */
public class GetCurrentWallpaperThread extends Thread {
	private Handler mHandler;
	private Context mContext;
	private int mFolderId;
	
	public GetCurrentWallpaperThread(Handler handler, Context context, int folderId) {
		super();
		mHandler = handler;
		mContext = context;
		mFolderId = folderId;
	}

	/**
	 * Get the current wallpaper
	 */
	public void run() {
		try {
			// Get wallpaperManager and wallpaper
			WallpaperManager wallpaperManager = WallpaperManager.getInstance(this.mContext);
            Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        	
            // Create the name of the wallpaper
            long date = new Date().getTime();
            String filename = "wpp_"+date+".jpg";
            
            // Create file
			OutputStream fOut = null;
			File file = new File(WallpaperManagerConstants._registrationFilesDir, filename);
            fOut = new FileOutputStream(file);
            
            int width, height;
            // getDesiredMinimumWidth() can return -1
            if(wallpaperManager.getDesiredMinimumWidth() > 0) {
	            width = wallpaperManager.getDesiredMinimumWidth();
	            height = wallpaperManager.getDesiredMinimumHeight();
            } else {
                width = wallpaperDrawable.getMinimumWidth();
                height = wallpaperDrawable.getMinimumHeight();
            }
            
            /*/ Log for resolution
			Log.d("Resolution", width+"x"+height);
			//*/
            
            // transform Drawable to Bitmap
            Bitmap wallpaperBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(wallpaperBitmap); 
            wallpaperDrawable.setBounds(0, 0, width, height); 
            wallpaperDrawable.draw(canvas);

            // Write bitmap into the file
            wallpaperBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
            
            //MediaStore.Images.Media.insertImage(mContext.getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());
      
            // Insert the wallpaper into the database
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
