package com.wallpapers_manager.cyril;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Helper {
	
	public static Bitmap _decodeFile(File file){
	    try {
	        //Decode image size
	        BitmapFactory.Options bitmapFactoryOptions = new BitmapFactory.Options();
	        bitmapFactoryOptions.inJustDecodeBounds = true;
	        BitmapFactory.decodeStream(new FileInputStream(file),null,bitmapFactoryOptions);

	        //The new size we want to scale to
	        final int REQUIRED_SIZE = 70;

	        //Find the correct scale value. It should be the power of 2.
	        int widthTemp = bitmapFactoryOptions.outWidth;
	        int heightTemp = bitmapFactoryOptions.outHeight;
	        int scale=1;
	        while(true){
	            if((widthTemp / 2) <REQUIRED_SIZE || (heightTemp / 2) < REQUIRED_SIZE)
	                break;
	            widthTemp /= 2;
	            heightTemp /= 2;
	            scale *= 2;
	        }

	        //Decode with inSampleSize
	        bitmapFactoryOptions = new BitmapFactory.Options();
	        bitmapFactoryOptions.inSampleSize=scale;
	        return BitmapFactory.decodeStream(new FileInputStream(file), null, bitmapFactoryOptions);
	    } catch (FileNotFoundException e) {
	    	e.printStackTrace();
	    }
	    return null;
	}

}
