package com.wallpapers_manager.cyril.folders;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wallpapers_manager.cyril.R;

public class FoldersArrayAdapter extends ArrayAdapter<Folder> {
	private LayoutInflater			mInflater;
	
	public FoldersArrayAdapter(Context context, List<Folder> objects) {
		super(context, 0, objects);
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		final Folder folder = this.getItem(position);
		View view = convertView;
		if(view == null)
	        view = mInflater.inflate(R.layout.folder_selectable, null);
		
		ImageView image_view = (ImageView) view.findViewById(R.id.image);
		image_view.setImageResource(R.drawable.folder);

		TextView name_view = (TextView) view.findViewById(R.id.name);
		name_view.setText(folder.getName());

		return view;
	}
	
	@Override
	public long getItemId(int position) {
		return getItem(position).getId();
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}
}
