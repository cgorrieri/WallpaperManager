package com.wallpapers_manager.cyril;

import java.io.File;

public final class WallpaperManagerConstants {
	/* Packages name */
	public final static String BASE_PACKAGE = "com.wallpapers_manager.cyril";
	public final static String SERVICE_PACKAGE = "thread_and_service";
	
	/* Broadcast names */
	public final static String BROADCAST_UPDATE_WPP_PL = BASE_PACKAGE+".updateWallpapersPlaylistCursor";
	public final static String BROADCAST_UPDATE_PL = BASE_PACKAGE+".updatePlaylistCursor";
	
	/* Directories */
	public final static File _registrationFilesDir = new File("/sdcard/WallpaperManager/");
	
	public final static boolean makeRegistrationFilesDir() {
		return _registrationFilesDir.mkdir();
	}

}
