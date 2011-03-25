package com.wallpapers_manager.cyril.rotate_lists;

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
import com.wallpapers_manager.cyril.RotateListsTabActivityGroup;

public class RotateListsCursorAdapter extends CursorAdapter {
	private final LayoutInflater 	mInflater;
	private final Context			mContext;
	private Resources				mResources;

	public RotateListsCursorAdapter(Context context, Cursor cursor) {
		super(context, cursor);
		mInflater = LayoutInflater.from(context);
		mContext = context;
		mResources = mContext.getResources();
	}

	@Override
	public void bindView(final View view, Context context, final Cursor cursor) {
		final RotateList rotateList = new RotateList(cursor.getInt(RotateListsDBAdapter.ID_IC),
				cursor.getString(RotateListsDBAdapter.NAME_IC), cursor.getInt(RotateListsDBAdapter.SELECTED_IC));

		ImageView imageView = (ImageView) view.findViewById(R.id.image);
		if(rotateList.isSelected())
			imageView.setImageResource(R.drawable.selected_rotate_list);
		else
			imageView.setImageResource(R.drawable.rotate_list);

		TextView nameView = (TextView) view.findViewById(R.id.name);
		nameView.setText(cursor.getString(1));

		view.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				openRotateList(rotateList);
			}
		});

		final CursorAdapter cursorAdapter = this;

		view.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(final View v) {
				final CharSequence[] items = mResources.getTextArray(R.array.rotate_list_menu);
				items[1] = rotateList.isSelected() ? mResources.getText(R.string.rotate_list_menu_unselect) : mResources.getText(R.string.rotate_list_menu_select);

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
				alertDialogBuilder.setTitle(mResources.getText(R.string.actions));
				alertDialogBuilder.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						final RotateListsDBAdapter rotateListsDBAdapter = new RotateListsDBAdapter(mContext);
						Cursor cursor = null;
						switch (item) {
						case 0:
							openRotateList(rotateList);
							break;
						case 1:
							rotateListsDBAdapter.open();
							rotateList.setSelected(rotateList.isSelected() ? 0 : 1);
							rotateListsDBAdapter.updateRotateList(rotateList);
							cursor = rotateListsDBAdapter.getCursor();
							cursorAdapter.changeCursor(cursor);
							rotateListsDBAdapter.close();
							break;
						case 2:
							AlertDialog.Builder renameRotateListDialog = new AlertDialog.Builder(mContext);
							renameRotateListDialog.setTitle(items[2]);

							final EditText rotateListNameEditText = new EditText(mContext);
							rotateListNameEditText.setText(rotateList.getName());
							rotateListNameEditText.setMaxLines(1);
							rotateListNameEditText.setInputType(InputType.TYPE_CLASS_TEXT);
							rotateListNameEditText.setSelection(0, rotateList.getName().length());

							renameRotateListDialog.setView(rotateListNameEditText);

							renameRotateListDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface d, int which) {
										rotateListsDBAdapter.open();
										rotateListsDBAdapter.updateRotateList(new RotateList(
												rotateList.getId(),
												rotateListNameEditText
														.getText()
														.toString()));
										Cursor cursor2 = rotateListsDBAdapter.getCursor();
										cursorAdapter.changeCursor(cursor2);
										rotateListsDBAdapter.close();
									}
								}
							);
							renameRotateListDialog.show();
							break;
						case 3:
							rotateListsDBAdapter.open();
							rotateListsDBAdapter.removeRotateList(rotateList);
							cursor = rotateListsDBAdapter.getCursor();
							cursorAdapter.changeCursor(cursor);
							rotateListsDBAdapter.close();
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
		return mInflater.inflate(R.layout.rotate_list, parent, false);
	}

	private void openRotateList(RotateList rotateList) {
		Intent wallpapersIntent = new Intent(mContext, RotateListWallpapersActivity.class);
		wallpapersIntent.putExtra("rotate_list_id", rotateList.getId());

		RotateListsTabActivityGroup._group.startChildActivity(
				"RotateListWallpapersActivity",
				wallpapersIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	}
}
