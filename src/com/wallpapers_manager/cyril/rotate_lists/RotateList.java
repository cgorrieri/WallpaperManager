package com.wallpapers_manager.cyril.rotate_lists;

public class RotateList {	
	private int id;
	private String name;
	private int selected;
	
	public RotateList(String e_name) {
		this(-1, e_name, 0);
	}
	
	public RotateList(int e_id, String e_name) {
		this(e_id, e_name, 0);
	}
	
	public RotateList(int e_id, String e_name, int e_selected) {
		this.id = e_id;
		this.name = e_name;
		this.selected = e_selected;
	}
	
	public int getId() { return this.id; }
	public void setId(int e_id) { this.id = e_id; }
	
	public String getName() { return this.name; }
	public void setAddress(String e_name) { this.name = e_name; }

	public int getSelected() { return selected; }
	public boolean isSelected() { return selected == 1 ? true : false; }
	public void setSelected(int selected) { this.selected = selected; }

}
