package com.wallpapers_manager.cyril.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wallpapers_manager.cyril.R;
import com.wallpapers_manager.cyril.data.Playlist;

public class PlaylistsArrayAdapter extends ArrayAdapter<Playlist> {
	private LayoutInflater			mInflater;
	
	public PlaylistsArrayAdapter(Context context, List<Playlist> objects) {
		super(context, 0, objects);
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		final Playlist playlist = this.getItem(position);
		View view = convertView;
		if(view == null)
	        view = mInflater.inflate(R.layout.playlist_selectable, null);
		
		ImageView image_view = (ImageView) view.findViewById(R.id.image);
		image_view.setImageResource(R.drawable.playlist);

		TextView name_view = (TextView) view.findViewById(R.id.name);
		name_view.setText(playlist.getName());

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
