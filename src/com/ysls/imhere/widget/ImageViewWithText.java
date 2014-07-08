package com.ysls.imhere.widget;

import com.ysls.imhere.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImageViewWithText extends LinearLayout {
	ImageView imageView;

	public ImageViewWithText(Context context) {
		this(context, null);
	}

	public ImageViewWithText(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.menu_itemlayout, this,
				true);
		int textId = -1;
		int imageId = -1;
		imageView = (ImageView) findViewById(R.id.imageView);
		TextView textView = (TextView) findViewById(R.id.textView);
		textId = attrs.getAttributeResourceValue(null, "Text", 0);
		imageId = attrs.getAttributeResourceValue(null, "ImageSrc", 0);
		if (textId > 0) {
			textView.setText(context.getResources().getText(textId));
			textView.setTextColor(context.getResources().getColor(R.color.gray_font));
		} else {
		}
		if (imageId > 0) {
			imageView.setImageDrawable(context.getResources().getDrawable(
					imageId));
		}
	}

	public ImageView getImageButton() {
		return imageView;
	}

}
