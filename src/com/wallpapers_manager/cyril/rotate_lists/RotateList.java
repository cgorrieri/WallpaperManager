package com.wallpapers_manager.cyril.rotate_lists;

public class RotateList {	
	private int 	mId;
	private String 	mName;
	private int 	mSelected;
	
	public RotateList(String name) {
		this(-1, name, 0);
	}
	
	public RotateList(int id, String name) {
		this(id, name, 0);
	}
	
	public RotateList(int id, String name, int selected) {
		this.setId(id);
		this.setName(name);
		this.mSelected = selected;
	}
	

	public int getSelected() { return mSelected; }
	public boolean isSelected() { return mSelected == 1 ? true : false; }
	public void setSelected(int selected) { this.mSelected = selected; }
	public void setSelected(boolean selected) { this.mSelected = selected ? 1 : 0; }

	public void setId(int mId) { this.mId = mId; }

	public int getId() { return mId; }

	public void setName(String mName) { this.mName = mName; }

	public String getName() { return mName; }

}
