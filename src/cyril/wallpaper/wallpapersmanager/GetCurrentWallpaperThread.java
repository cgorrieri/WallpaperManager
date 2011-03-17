package cyril.wallpaper.wallpapersmanager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import cyril.wallpaper.WallpaperManagerConstants;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.Display;

public class GetCurrentWallpaperThread extends Thread {
	private Display display;
	private Handler handler;
	private Context context;
	private int folder_id;
	
	public GetCurrentWallpaperThread(Display display, Handler handler, Context ctxt, int folder_id)
	{
		super();
		this.setDisplay(display);
		this.setHandler(handler);
		this.setContext(ctxt);
		this.folder_id = folder_id;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public Handler getHandler() {
		return handler;
	}

	public void run() {
		try {	
			WallpaperManager wallpaperManager = WallpaperManager.getInstance(this.context);
			
            Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        	
            long date = new Date().getTime();
            
            String filename = "wallpaper_"+date+".png";
            
			OutputStream fOut = null;
			File file = new File(WallpaperManagerConstants.registrationFilesDir, filename);
            fOut = new FileOutputStream(file);
            
            Bitmap wallpaperBitmap = Bitmap.createBitmap(wallpaperDrawable.getMinimumWidth(), wallpaperDrawable.getMinimumHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(wallpaperBitmap); 
            wallpaperDrawable.setBounds(0, 0, wallpaperDrawable.getMinimumWidth(), wallpaperDrawable.getMinimumHeight()); 
            wallpaperDrawable.draw(canvas);

            wallpaperBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
      
            WallpapersDBAdapter wppDBA = new WallpapersDBAdapter(this.context);
	        wppDBA.open();
			wppDBA.insertWallpaper(new Wallpaper(this.folder_id, filename));
			wppDBA.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        handler.sendEmptyMessage(0);
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public Context getContext() {
		return context;
	}

	public void setDisplay(Display display) {
		this.display = display;
	}

	public Display getDisplay() {
		return display;
	}
}
