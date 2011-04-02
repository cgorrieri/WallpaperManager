package com.wallpapers_manager.cyril.folders;

import java.util.ArrayList;

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
import com.wallpapers_manager.cyril.wallpapers.Wallpaper;
import com.wallpapers_manager.cyril.wallpapers.WallpapersActivity;
import com.wallpapers_manager.cyril.wallpapers.WallpapersDBAdapter;

public class FoldersCursorAdapter extends CursorAdapter {
	private final LayoutInflater 	mInflater;
	private final Context 			mContext;
	private Resources				mResource;
	private CursorAdapter			mCursorAdapter;
	

	public FoldersCursorAdapter(Context context, Cursor cursor) {
		super(context, cursor);
		mInflater = LayoutInflater.from(context);
		mContext = context;
		mResource = context.getResources();
		mCursorAdapter = this;
	}

	@Override
	public void bindView(final View view, Context context, final Cursor cursor) {
		final Folder folder = new Folder(cursor);

		ImageView image_view = (ImageView) view.findViewById(R.id.image);
		image_view.setImageResource(R.drawable.folder);

		TextView name_view = (TextView) view.findViewById(R.id.name);
		name_view.setText(cursor.getString(1));

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
								CursorAdapter ca = new AddFoldersInPlaylistCursorAdapter(mContext, cur, folder, dialog);
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
												Cursor curs = foldersDBAdapter.getCursor();
												mCursorAdapter.changeCursor(curs);
											foldersDBAdapter.close();
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
								Cursor curs = foldersDBAdapter.getCursor();
								mCursorAdapter.changeCursor(curs);
							foldersDBAdapter.close();
							break;
						}
					}
				});

				builder.show();
				return false;
			}
		});
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return mInflater.inflate(R.layout.folder, parent, false);
	}

	private void openFolder(Folder folder) {
		Intent wallpapers = new Intent(mContext, WallpapersActivity.class);
		wallpapers.putExtra("folderId", folder.getId());

		WallpapersTabActivityGroup._group.startChildActivity(
				"WallpapersActivity",
				wallpapers.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	}
}
