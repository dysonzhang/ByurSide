
package com.ysls.imhere.widget;

import com.ysls.imhere.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

/**
 * @author dyson
 *
 */
public class ImgLoadView extends FrameLayout
{

	private ImageView iv;
	private ProgressBar pb;

	public ImgLoadView(Context context)
	{
		super(context);
	}

	public ImgLoadView(Context context, AttributeSet attributeset)
	{
		super(context, attributeset);
		init(context);
	}

	private void init(Context context)
	{
		LayoutInflater.from(context).inflate(R.layout.imgload, this, true);
		pb = (ProgressBar)findViewById(R.id.imgload_pb);
//		pb.setVisibility(View.VISIBLE);
		iv = (ImageView)findViewById(R.id.imgload_iv);
	}

	public void setImg(Bitmap bitmap)
	{
		iv.setImageBitmap(bitmap);
		iv.setVisibility(View.VISIBLE);
//		pb.setVisibility(View.GONE);
	}
	
	public void setDrawable(Drawable drawable)
	{
		iv.setBackgroundDrawable(drawable);
		iv.setVisibility(View.VISIBLE);
		pb.setVisibility(View.GONE);
	}
	
	public void showProgress(){
		pb.setVisibility(View.VISIBLE);
	}
	
}
