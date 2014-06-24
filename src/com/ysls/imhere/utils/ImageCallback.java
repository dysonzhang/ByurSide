package com.ysls.imhere.utils;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * ImageCallback
 * 
 * @author
 * 
 */
public interface ImageCallback {
	public void imageLoaded(Drawable imageDrawable, ImageView imageView,
			String imageUrl);
}
