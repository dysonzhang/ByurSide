package com.ysls.imhere.utils;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

/**
 * @author
 * 
 */
public class AsyncImageLoader {

	/**
	 *
	 */
	private HashMap<String, SoftReference<Drawable>> imageCache;

	public AsyncImageLoader() {
		imageCache = new HashMap<String, SoftReference<Drawable>>();
	}

	public Drawable loadDrawable(final String imageUrl,
			final ImageView imageView, final ImageCallback imagecallback) {
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			Drawable drawable = softReference.get();
			if (drawable != null) {
				return drawable;
			}
		}
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				imagecallback.imageLoaded((Drawable) msg.obj, imageView,
						imageUrl);
			}
		};
		new Thread() {
			public void run() {
				try {
					Drawable drawable = loadImageFromUrl(imageUrl);
					imageCache.put(imageUrl, new SoftReference<Drawable>(
							drawable));
					Message message = handler.obtainMessage(0, drawable);
					handler.sendMessage(message);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}.start();

		return null;
	}

	/**
	 * 
	 * HashMap<String, SoftReference<Drawable>> imageCache;
	 */
	public void clean() {
		imageCache.clear();
	}

	public static Drawable loadImageFromUrl(String url) throws IOException {
		if (url != null && !"".equals(url)) {
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				File f = new File(Environment.getExternalStorageDirectory()
						+ "/IMhere/" + MD5.encode(url));
				if (f.exists()) {
					FileInputStream fis = new FileInputStream(f);
					Drawable d = Drawable.createFromStream(fis, "src");
					if (d == null) {
						Log.i("Image", "d--> is null");
						f.delete();

					}

					return d;
				}
				URL m = new URL(url);
				InputStream i = (InputStream) m.getContent();
				DataInputStream in = new DataInputStream(i);
				FileOutputStream out = new FileOutputStream(f);
				byte[] buffer = new byte[1024];
				int byteread = 0;
				while ((byteread = in.read(buffer)) != -1) {
					out.write(buffer, 0, byteread);
				}
				out.close();
				in.close();
				i.close();
				buffer = null;

				return loadImageFromUrl(url);
			} else {
				URL m = new URL(url);
				InputStream i = (InputStream) m.getContent();
				Drawable d = Drawable.createFromStream(i, "src");
				i.close();
				if (d == null) {
					Log.i("Image", "d---> is null--");
				}
				return d;
			}
		}
		return null;

	}

	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;

		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

}