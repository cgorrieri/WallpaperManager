package com.wallpapers_manager.cyril.wallpapers;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wallpapers_manager.cyril.R;
import com.wallpapers_manager.cyril.WallpapersTabActivityGroup;
import com.wallpapers_manager.cyril.playlists.Playlist;
import com.wallpapers_manager.cyril.playlists.PlaylistsDBAdapter;
import com.wallpapers_manager.cyril.playlists.WallpapersPlaylistDBAdapter;

public class FoldersArrayAdapter extends ArrayAdapter {
	private final Context 			mContext;
	private Resources				mResource;
	private LayoutInflater			mInflater;
	
	public FoldersArrayAdapter(Context context, int textViewResourceId, List objects) {
		super(context, textViewResourceId, objects);
		mInflater = LayoutInflater.from(context);
		mContext = context;
		mResource = context.getResources();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		final Folder folder = (Folder) this.getItem(position);
		View view = convertView;
		if(view == null){
			LayoutInflater li = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        //le layout représentant la ligne dans le listView
	        view = li.inflate(R.layout.folder, null);
		}
		
		ImageView image_view = (ImageView) view.findViewById(R.id.image);
		image_view.setImageResource(R.drawable.folder);

		TextView name_view = (TextView) view.findViewById(R.id.name);
		name_view.setText(folder.getName());

		view.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				openFolder(folder);
			}
		});

		view.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(final View v) {
				final CharSequence[] items = mResource.getTextArray(R.array.folder_menu);

				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setTitle(mResource.getText(R.string.actions));
				builder.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialogInterface, int item) {
						switch (item) {
						case 0: // Open
							openFolder(folder);
							break;
						case 1: // Create rotate list from it
							PlaylistsDBAdapter playlistsDBAdapter = new PlaylistsDBAdapter(mContext);
							playlistsDBAdapter.open();
								Playlist playlist = new Playlist(folder.getName());
								playlist.setId((int) playlistsDBAdapter.insertPlaylist(playlist));
							playlistsDBAdapter.close();
							WallpapersPlaylistDBAdapter wallpapersPlaylistDBAdapter = new WallpapersPlaylistDBAdapter(mContext);
							wallpapersPlaylistDBAdapter.open();							
								wallpapersPlaylistDBAdapter.insertPlaylistWallpaperForFolder(folder, playlist);
							wallpapersPlaylistDBAdapter.close();
							Intent intentBroadcast = new Intent("com.wallpaper_manager.playlists.updatePlaylistCursor");
							mContext.sendBroadcast(intentBroadcast);
							break;
						case 2: // Add to rotate list
							final Dialog dialog = new Dialog(mContext);

							PlaylistsDBAdapter playlistsDBAdapter2 = new PlaylistsDBAdapter(mContext);
							playlistsDBAdapter2.open();
								Cursor cur = playlistsDBAdapter2.getCursor();
								ListView lstA = new ListView(mContext);
								CursorAdapter ca = new AddWallpaperInPlaylistCursorAdapter(mContext, cur, folder, dialog);
								lstA.setAdapter(ca);
							playlistsDBAdapter2.close();
							dialog.setContentView(lstA);
							dialog.setTitle(items[2]);
							dialog.show();
							break;
						case 3: // rename
							AlertDialog.Builder renameFolderAlertDialogBuilder = new AlertDialog.Builder(mContext);
							renameFolderAlertDialogBuilder.setTitle(items[3]);

							final EditText folderNameEditText = new EditText(mContext);
							folderNameEditText.setText(folder.getName());
							folderNameEditText.setMaxLines(1);
							folderNameEditText.setInputType(InputType.TYPE_CLASS_TEXT);
							folderNameEditText.setSelection(0, folder.getName().length());

							renameFolderAlertDialogBuilder.setView(folderNameEditText);

							renameFolderAlertDialogBuilder.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface d, int which) {
											FoldersDBAdapter foldersDBAdapter = new FoldersDBAdapter(
													mContext);
											foldersDBAdapter.open();
												foldersDBAdapter.updateFolder(new Folder(folder.getId(),
														folderNameEditText.getText().toString()));
											foldersDBAdapter.close();
											WallpapersTabActivityGroup._group.refreshCurrent();
										}
									});
							renameFolderAlertDialogBuilder.show();
							break;
						case 4: // delete
							WallpapersDBAdapter wallpapersDBAdapter2 = new WallpapersDBAdapter(mContext);
							wallpapersDBAdapter2.open();
								ArrayList<Wallpaper> wallpapersList = wallpapersDBAdapter2.getWallpapersFromFolder(folder);
								for (int i = 0; i < wallpapersList.size(); i++)
									wallpapersList.get(i).delete(wallpapersDBAdapter2);
							wallpapersDBAdapter2.close();
							FoldersDBAdapter foldersDBAdapter = new FoldersDBAdapter(mContext);
							foldersDBAdapter.open();
								foldersDBAdapter.removeFolder(folder);
							foldersDBAdapter.close();
							WallpapersTabActivityGroup._group.refreshCurrent();
							break;
						}
					}
				});

				builder.show();
				return false;
			}
		});
		
		return view;
	}

	private void openFolder(Folder folder) {
		Intent wallpapers = new Intent(mContext, WallpapersActivity.class);
		wallpapers.putExtra("folderId", folder.getId());

		WallpapersTabActivityGroup._group.startChildActivity(
				"WallpapersActivity",
				wallpapers.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	}
}
