package com.wallpapers_manager.cyril.widget;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class MultiSelectGridView extends GridView {

	public MultiSelectGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MultiSelectGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MultiSelectGridView(Context context) {
		super(context);
	}
	
	public long[] getCheckItemIds() {
		int size = getCount();
		ArrayList<Long> idList = new ArrayList<Long>();
		for(int i = 0; i<size;i++) {
			if( ((CheckableRelativeLayout)getChildAt(i)).isChecked() )
				idList.add(getItemIdAtPosition(i));
		}
		
		long[] result = new long[idList.size()];
		for(int i = 0; i< result.length; i++)
			result[i] = idList.get(i);
		
		return result;
	}
	
	public void setItemChecked(int position, boolean checked) {
		((CheckableRelativeLayout)getChildAt(position)).setChecked(checked);
	}
}
