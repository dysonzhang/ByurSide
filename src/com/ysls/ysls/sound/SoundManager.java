package com.ysls.ysls.sound;

import java.util.ArrayList;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundManager implements
		android.media.MediaPlayer.OnCompletionListener {

	private ArrayList playerList;
	public static boolean isTurnOnSound = true;

	public SoundManager() {
		super();
		playerList = new ArrayList();
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		mp.release();
		playerList.remove(mp);
	}

	public void play(Context mContext, int resId) {
		MediaPlayer mp = MediaPlayer.create(mContext, resId);
		if (mp != null && isTurnOnSound) {
			mp.setOnCompletionListener(this);
			playerList.add(mp);
			mp.start();
		}
	}

	public void play(Context context, String s) {
		String s1 = s.split("\\.")[0];
		int i = context.getResources().getIdentifier(s1, "raw", "com.ysls.bt");
		if (i != 0)
			play(context, i);
	}
}
