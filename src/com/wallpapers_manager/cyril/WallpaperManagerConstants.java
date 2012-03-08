package com.wallpapers_manager.cyril;

import java.io.File;

public final class WallpaperManagerConstants {
	public final static String BASE_PACKAGE = "com.wallpapers_manager.cyril";
	public final static String SERVICE_PACKAGE = "thread_and_service";
	public final static File _registrationFilesDir = new File("/sdcard/WallpaperManager/");
	
	public final static boolean makeRegistrationFilesDir() {
		return _registrationFilesDir.mkdir();
	}

}
