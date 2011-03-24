package com.wallpapers_manager.cyril;

import java.io.File;

public final class WallpaperManagerConstants {
	public final static File _registrationFilesDir = new File("/sdcard/WallpaperManager/");
	
	public final static boolean makeRegistrationFilesDir() {
		return _registrationFilesDir.mkdir();
	}

}
