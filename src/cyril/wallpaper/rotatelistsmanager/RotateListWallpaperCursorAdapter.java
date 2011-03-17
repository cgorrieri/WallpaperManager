package cyril.wallpaper.rotatelistsmanager;

import java.io.File;

import cyril.wallpaper.Helper;
import cyril.wallpaper.R;
import cyril.wallpaper.WallpaperManagerConstants;
import cyril.wallpaper.wallpapersmanager.Wallpaper;
import cyril.wallpaper.wallpapersmanager.WallpapersDBAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CursorAdapter;
import android.widget.ImageView;

public class RotateListWallpaperCursorAdapter extends CursorAdapter {
	protected final LayoutInflater mInflater;
	protected final Context mContext;
	
	public RotateListWallpaperCursorAdapter(Context context, Cursor cursor) {
		super(context, cursor);
		mInflater = LayoutInflater.from(context);
		mContext = context;
	}
	
	@Override
	public void bindView(final View view, Context context, final Cursor cursor) {
		final RotateListWallpaper rtlWpp = new RotateListWallpaper(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2));
		WallpapersDBAdapter wppDBA = new WallpapersDBAdapter(mContext);
        wppDBA.open();
        	final Wallpaper wpp = wppDBA.getWallpaper(rtlWpp.getWpp_id());
        wppDBA.close();

		File wallpaperFile = new File(WallpaperManagerConstants.registrationFilesDir, wpp.getAddress());
		Bitmap wallpaperBitmap = Helper.decodeFile(wallpaperFile);
		
		final ImageView wallpaper = (ImageView) view.findViewById(R.id.wallpaper);
		wallpaper.setImageBitmap(wallpaperBitmap);
		
		final CursorAdapter cursor_adp = this;

		view.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				final CharSequence[] items = {"Delete"};

				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setTitle("Actions");
				builder.setItems(items, new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int item) {
				    	switch(item){
				    	case 0:
				    		RotateListWallpapersDBAdapter rtlWppDBA = new RotateListWallpapersDBAdapter(mContext);
				            rtlWppDBA.open();
					        	rtlWppDBA.remove(rtlWpp);
					            Cursor curs = rtlWppDBA.getCursor(rtlWpp.getRtl_id());
					            cursor_adp.changeCursor(curs);
				            rtlWppDBA.close();
				    		break;
				    	}
				    	
				    }
				});
				
				builder.show();
			}
		});
	}
	
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return mInflater.inflate(R.layout.wallpaper, parent, false);
	}
}
