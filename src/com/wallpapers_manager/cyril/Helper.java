package com.wallpapers_manager.cyril;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.app.WallpaperManager;
import android.content.Context;

public class Helper {
	
	public static void setWallpaper(Context ctxt, String filePath) throws IOException {
		WallpaperManager wallpaperManager = WallpaperManager.getInstance(ctxt);
		FileInputStream wpp = new FileInputStream(new File(WallpaperManagerConstants._registrationFilesDir, filePath));
		wallpaperManager.setStream(wpp);
	}

}
