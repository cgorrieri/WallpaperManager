package cyril.wallpaper.wallpapersmanager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cyril.wallpaper.R;
import cyril.wallpaper.WallpapersTabActivityGroup;

public class AddWallpaperCursorAdapter extends CursorAdapter {
	private Wallpaper wpp;
	private Dialog dg;
	protected final LayoutInflater mInflater;
	protected final Context mContext;
	
	public AddWallpaperCursorAdapter(Context context, Cursor cursor, Wallpaper wpp, Dialog dg) {
		super(context, cursor);
		mInflater = LayoutInflater.from(context);
		mContext = context;
		this.wpp = wpp;
		this.dg = dg;
	}
	
	@Override
	public void bindView(final View view, Context context, final Cursor cursor) {
		final Folder fd = new Folder(cursor.getInt(0),	cursor.getString(1));

		ImageView image_view = (ImageView) view.findViewById(R.id.image);
		image_view.setImageResource(R.drawable.folder);

		TextView name_view = (TextView) view.findViewById(R.id.name);
		name_view.setText(cursor.getString(1));

		view.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				WallpapersDBAdapter wppDBA = new WallpapersDBAdapter(mContext);
				wpp.setFolderId(fd.getId());
				wppDBA.open();
				wppDBA.updateWallpaper(wpp);
				wppDBA.close();
				dg.dismiss();
				WallpapersTabActivityGroup.group.finishFromChild(WallpapersTabActivityGroup.group.getCurrentActivity());
				Intent wallpapers = new Intent(mContext, WallpapersActivity.class);
				wallpapers.putExtra("folder_id", wpp.getFolderId());

				WallpapersTabActivityGroup.group.startChildActivity(
						"WallpapersActivity",
						wallpapers.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			}
		});
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return mInflater.inflate(R.layout.folder, parent, false);
	}
}
