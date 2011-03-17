package cyril.wallpaper.wallpapersmanager;

import java.io.File;

import cyril.wallpaper.WallpaperManagerConstants;


public class Wallpaper {	
	private int id;
	private int folder_id;
	private String address;
	
	public Wallpaper(String e_address) {
		this.folder_id = 0;
		this.address = e_address;
	}
	public Wallpaper(int e_folder_id, String e_address) {
		this.folder_id = e_folder_id;
		this.address = e_address;
	}
	
	public Wallpaper(int e_id, int e_folder_id, String e_address) {
		this.id = e_id;
		this.folder_id = e_folder_id;
		this.address = e_address;
	}
	
	public int getId() { return this.id; }
	public void setId(int e_id) { this.id = e_id; }
	
	public int getFolderId() { return this.folder_id; }
	public void setFolderId(int e_folder_id) { this.folder_id = e_folder_id; }
	
	public String getAddress() { return this.address; }
	public void setAddress(String e_address) { this.address = e_address; }
	
	public boolean delete(WallpapersDBAdapter wppDBA) 
	{
		wppDBA.removeWallpaper(this.id);
		File file = new File(WallpaperManagerConstants.registrationFilesDir, this.address);
		File thumbnail_file = new File(WallpaperManagerConstants.registrationFilesDir, "thumbnail_"+this.address);
		return file.delete() && thumbnail_file.delete();
	}
	
	public String toString() {
		return "Id :"+this.id+", Folder_id :"+this.folder_id+", Address :"+this.address;
	}
}
