package com.wallpapers_manager.cyril.rotate_lists;

public class RotateListWallpaper {	
	private int id;
	private int wpp_id;
	private int rtl_id;
	
	public RotateListWallpaper(int e_wpp_id, int e_rtl_id) {
		this(-1, e_wpp_id, e_rtl_id);
	}
	
	public RotateListWallpaper(int e_id, int e_wpp_id, int e_rtl_id) {
		this.setId(e_id);
		this.setWpp_id(e_wpp_id);
		this.setRtl_id(e_rtl_id);
	}
	
	public void setWpp_id(int wpp_id) {
		this.wpp_id = wpp_id;
	}

	public int getWpp_id() {
		return wpp_id;
	}

	public void setRtl_id(int rtl_id) {
		this.rtl_id = rtl_id;
	}

	public int getRtl_id() {
		return rtl_id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
