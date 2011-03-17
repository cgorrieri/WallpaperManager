package cyril.wallpaper.wallpapersmanager;

public class Folder {	
	private int id;
	private String name;
	
	public Folder(String e_name) {
		this.name = e_name;
	}
	
	public Folder(int e_id, String e_name) {
		this.id = e_id;
		this.name = e_name;
	}
	
	public int getId() { return this.id; }
	public void setId(int e_id) { this.id = e_id; }
	
	public String getName() { return this.name; }
	public void setAddress(String e_name) { this.name = e_name; }
}
