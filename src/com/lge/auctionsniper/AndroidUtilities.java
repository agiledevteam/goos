package com.lge.auctionsniper;

import android.os.Handler;
import android.os.Looper;

class AndroidUtilities {
	private static Handler handler = new Handler(Looper.getMainLooper());

	public static void runOnUiThread(Runnable runnable) {
		if (isOnUiThread()) {
			runnable.run();
		} else {
			handler.post(runnable);
		}
	}

	private static boolean isOnUiThread() {
		return Looper.myLooper() == Looper.getMainLooper();
	}
}