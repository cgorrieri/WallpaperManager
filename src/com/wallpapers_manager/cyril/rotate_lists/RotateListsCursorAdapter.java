package com.wallpapers_manager.cyril.rotate_lists;

import com.wallpapers_manager.cyril.R;
import com.wallpapers_manager.cyril.RotateListsTabActivityGroup;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

public class RotateListsCursorAdapter extends CursorAdapter {
	private final LayoutInflater mInflater;
	private final Context mContext;

	public RotateListsCursorAdapter(Context context, Cursor cursor) {
		super(context, cursor);
		mInflater = LayoutInflater.from(context);
		mContext = context;
	}

	@Override
	public void bindView(final View view, Context context, final Cursor cursor) {
		final RotateList rtl = new RotateList(cursor.getInt(0),
				cursor.getString(1), cursor.getInt(2));

		ImageView image_view = (ImageView) view.findViewById(R.id.image);
		if(rtl.isSelected())
			image_view.setImageResource(R.drawable.selected_rotate_list);
		else
			image_view.setImageResource(R.drawable.rotate_list);

		TextView name_view = (TextView) view.findViewById(R.id.name);
		name_view.setText(cursor.getString(1));

		view.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				openRotateList(rtl);
			}
		});

		final CursorAdapter cursor_adp = this;

		view.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(final View v) {
				final CharSequence[] items = { "Open", "", "Rename", "Delete" };
				items[1] = rtl.isSelected() ? "UnSelect" : "Select";
				final RotateListsDBAdapter rtlsDBA = new RotateListsDBAdapter(mContext);

				AlertDialog.Builder builder = new AlertDialog.Builder(RotateListsTabActivityGroup.group);
				builder.setTitle("Actions");
				builder.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						switch (item) {
						case 0:
							openRotateList(rtl);
							break;
						case 1:
							rtlsDBA.open();
							rtl.setSelected(rtl.isSelected() ? 0 : 1);
							rtlsDBA.updateRotateList(rtl);
							Cursor curs1 = rtlsDBA.getCursor();
							cursor_adp.changeCursor(curs1);
							rtlsDBA.close();
							break;
						case 2:
							AlertDialog.Builder rename_rotate_list_dl = new AlertDialog.Builder(RotateListsTabActivityGroup.group);
							rename_rotate_list_dl.setTitle("Rename Rotate List");

							final EditText rotate_list_name_box = new EditText(mContext);
							rotate_list_name_box.setText(rtl.getName());
							rotate_list_name_box.setMaxLines(1);
							rotate_list_name_box.setInputType(InputType.TYPE_CLASS_TEXT);
							rotate_list_name_box.setSelection(0, rtl.getName().length());

							rename_rotate_list_dl.setView(rotate_list_name_box);

							rename_rotate_list_dl.setPositiveButton("OK", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface d, int which) {
										rtlsDBA.open();
										rtlsDBA.updateRotateList(new RotateList(
												rtl.getId(),
												rotate_list_name_box
														.getText()
														.toString()));
										Cursor curs2 = rtlsDBA.getCursor();
										cursor_adp.changeCursor(curs2);
										rtlsDBA.close();
									}
								}
							);
							rename_rotate_list_dl.show();
							break;
						case 3:
							rtlsDBA.open();
							rtlsDBA.removeRotateList(rtl);
							Cursor curs3 = rtlsDBA.getCursor();
							cursor_adp.changeCursor(curs3);
							rtlsDBA.close();
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
		return mInflater.inflate(R.layout.rotate_list, parent, false);
	}

	private void openRotateList(RotateList rtl) {
		Intent wallpapers = new Intent(mContext, RotateListWallpapersActivity.class);
		wallpapers.putExtra("rotate_list_id", rtl.getId());

		RotateListsTabActivityGroup.group.startChildActivity(
				"RotateListWallpapersActivity",
				wallpapers.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	}
}
