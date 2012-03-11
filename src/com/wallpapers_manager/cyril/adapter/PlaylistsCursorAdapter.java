package com.wallpapers_manager.cyril.adapter;

import android.app.AlertDialog;
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
import android.widget.TextView;

import com.wallpapers_manager.cyril.R;
import com.wallpapers_manager.cyril.activity.PlaylistsTabActivityGroup;
import com.wallpapers_manager.cyril.activity.WallpapersPlaylistActivity;
import com.wallpapers_manager.cyril.bdd.PlaylistsDBAdapter;
import com.wallpapers_manager.cyril.data.Playlist;

public class PlaylistsCursorAdapter extends CursorAdapter {
	private final LayoutInflater 	mInflater;
	private final Context			mContext;
	private Resources				mResources;

	public PlaylistsCursorAdapter(Context context, Cursor cursor) {
		super(context, cursor);
		mInflater = LayoutInflater.from(context);
		mContext = context;
		mResources = mContext.getResources();
	}

	@Override
	public void bindView(final View view, Context context, final Cursor cursor) {
		final Playlist playlist = new Playlist(cursor);

		ImageView imageView = (ImageView) view.findViewById(R.id.image);
		if(playlist.isSelected())
			imageView.setImageResource(R.drawable.selected_playlist);
		else
			imageView.setImageResource(R.drawable.playlist);

		TextView nameView = (TextView) view.findViewById(R.id.name);
		nameView.setText(cursor.getString(1));

		view.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				openPlaylist(playlist);
			}
		});

		final CursorAdapter cursorAdapter = this;

		view.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(final View v) {
				final CharSequence[] items = mResources.getTextArray(R.array.playlist_menu);
				items[1] = playlist.isSelected() ? mResources.getText(R.string.playlist_menu_unselect) : mResources.getText(R.string.playlist_menu_select);

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
				alertDialogBuilder.setTitle(mResources.getText(R.string.actions));
				alertDialogBuilder.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						final PlaylistsDBAdapter playlistsDBAdapter = new PlaylistsDBAdapter(mContext);
						Cursor cursor = null;
						switch (item) {
						case 0: // Open
							openPlaylist(playlist);
							break;
						case 1: // Select or UnSelect
							playlistsDBAdapter.open();
								playlist.setSelected(playlist.isSelected() ? 0 : 1);
								playlistsDBAdapter.updatePlaylist(playlist);
								cursor = playlistsDBAdapter.getCursor();
								cursorAdapter.changeCursor(cursor);
							playlistsDBAdapter.close();

							playlistsDBAdapter.open();
								Playlist p = playlistsDBAdapter.getPlaylist(playlist.getId());
							playlistsDBAdapter.close();
							break;
						case 2:
							AlertDialog.Builder renamePlaylistDialog = new AlertDialog.Builder(mContext);
							renamePlaylistDialog.setTitle(items[2]);

							final EditText rotateListNameEditText = new EditText(mContext);
							rotateListNameEditText.setText(playlist.getName());
							rotateListNameEditText.setMaxLines(1);
							rotateListNameEditText.setInputType(InputType.TYPE_CLASS_TEXT);
							rotateListNameEditText.setSelection(0, playlist.getName().length());

							renamePlaylistDialog.setView(rotateListNameEditText);

							renamePlaylistDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface d, int which) {
										playlistsDBAdapter.open();
											playlistsDBAdapter.updatePlaylist(new Playlist(playlist.getId(),
													rotateListNameEditText.getText().toString()));
											Cursor cursor2 = playlistsDBAdapter.getCursor();
											cursorAdapter.changeCursor(cursor2);
										playlistsDBAdapter.close();
									}
								}
							);
							renamePlaylistDialog.show();
							break;
						case 3:
							playlistsDBAdapter.open();
								playlistsDBAdapter.removePlaylist(playlist);
								cursor = playlistsDBAdapter.getCursor();
								cursorAdapter.changeCursor(cursor);
							playlistsDBAdapter.close();
							break;
						}
					}
				});

				alertDialogBuilder.show();
				return false;
			}
		});
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return mInflater.inflate(R.layout.playlist, parent, false);
	}

	private void openPlaylist(Playlist playlist) {
		Intent wallpapersIntent = new Intent(mContext, WallpapersPlaylistActivity.class);
		wallpapersIntent.putExtra("playlistId", playlist.getId());

		PlaylistsTabActivityGroup._group.startChildActivity(
				"PlaylistWallpapersActivity",
				wallpapersIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	}
}
