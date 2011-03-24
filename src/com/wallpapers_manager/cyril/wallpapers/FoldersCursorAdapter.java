package com.wallpapers_manager.cyril.wallpapers;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.ListView;
import android.widget.TextView;

import com.wallpapers_manager.cyril.R;
import com.wallpapers_manager.cyril.WallpapersTabActivityGroup;
import com.wallpapers_manager.cyril.rotate_lists.RotateList;
import com.wallpapers_manager.cyril.rotate_lists.RotateListWallpapersDBAdapter;
import com.wallpapers_manager.cyril.rotate_lists.RotateListsDBAdapter;

public class FoldersCursorAdapter extends CursorAdapter {
	private final LayoutInflater 	mInflater;
	private final Context 			mContext;

	public FoldersCursorAdapter(Context context, Cursor cursor) {
		super(context, cursor);
		mInflater = LayoutInflater.from(context);
		mContext = context;
	}

	@Override
	public void bindView(final View view, Context context, final Cursor cursor) {
		final Folder folder = new Folder(cursor.getInt(0), cursor.getString(1));

		ImageView image_view = (ImageView) view.findViewById(R.id.image);
		image_view.setImageResource(R.drawable.folder);

		TextView name_view = (TextView) view.findViewById(R.id.name);
		name_view.setText(cursor.getString(1));

		view.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				openFolder(folder);
			}
		});

		final CursorAdapter cursor_adp = this;

		view.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(final View v) {
				final CharSequence[] items = { "Open", "Create RL from it",
						"Add to playlist", "Rename", "Delete" };

				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setTitle("Actions");
				builder.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						switch (item) {
						case 0:
							openFolder(folder);
							break;
						case 1:
							RotateListsDBAdapter rtlDBA1 = new RotateListsDBAdapter(mContext);
							rtlDBA1.open();
							RotateList rtl = new RotateList(folder.getName());
							rtl.setId((int) rtlDBA1.insertRotateList(rtl));
							rtlDBA1.close();
							RotateListWallpapersDBAdapter rtlWppDBA = new RotateListWallpapersDBAdapter(
									mContext);
							rtlWppDBA.open();
							
							
									rtlWppDBA.insertRotateListWallpaperForFolder(folder, rtl);
								
							rtlWppDBA.close();
							break;
						case 2:
							final Dialog dg = new Dialog(mContext);

							RotateListsDBAdapter rtlDBA = new RotateListsDBAdapter(mContext);
							rtlDBA.open();
							Cursor cur = rtlDBA.getCursor();
							ListView lstA = new ListView(mContext);
							CursorAdapter ca = new AddRotateListWallpaperCursorAdapter(
									mContext, cur, folder, dg);
							lstA.setAdapter(ca);
							dg.setContentView(lstA);
							dg.setTitle("Add To");
							dg.show();
							break;
						case 3:
							AlertDialog.Builder rename_folder_dl = new AlertDialog.Builder(mContext);
							rename_folder_dl.setTitle("Rename Folder");

							final EditText folder_name_box = new EditText(mContext);
							folder_name_box.setText(folder.getName());
							folder_name_box.setMaxLines(1);
							folder_name_box
									.setInputType(InputType.TYPE_CLASS_TEXT);
							folder_name_box.setSelection(0, folder.getName().length());

							rename_folder_dl.setView(folder_name_box);

							rename_folder_dl.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface d,
												int which) {
											FoldersDBAdapter fdsDBA = new FoldersDBAdapter(
													mContext);
											fdsDBA.open();
											fdsDBA.updateFolder(new Folder(folder
													.getId(), folder_name_box
													.getText().toString()));
											Cursor curs = fdsDBA.getCursor();
											cursor_adp.changeCursor(curs);
											fdsDBA.close();
										}
									});
							rename_folder_dl.show();
							break;
						case 4:
							WallpapersDBAdapter wppDBA2 = new WallpapersDBAdapter(mContext);
							wppDBA2.open();
							ArrayList<Wallpaper> wpp_al = wppDBA2
									.getWallpapersFromFolder(folder);
							for (int i = 0; i < wpp_al.size(); i++)
								wpp_al.get(i).delete(wppDBA2);
							wppDBA2.close();
							FoldersDBAdapter fdsDBA = new FoldersDBAdapter(mContext);
							fdsDBA.open();
							fdsDBA.removeFolder(folder);
							Cursor curs = fdsDBA.getCursor();
							cursor_adp.changeCursor(curs);
							fdsDBA.close();
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
		wallpapers.putExtra("folder_id", folder.getId());

		WallpapersTabActivityGroup._group.startChildActivity(
				"WallpapersActivity",
				wallpapers.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	}
}
