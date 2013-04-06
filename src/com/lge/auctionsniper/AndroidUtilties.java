package com.lge.auctionsniper;

import android.os.Handler;
import android.os.Looper;

public class AndroidUtilties {

	private static Handler sHandler = new Handler(Looper.getMainLooper());

	public static void runOnUiThread(Runnable runnable) {
		if (isOnUiThread()) {
			runnable.run();
		} else {
			sHandler.post(runnable);
		}
	}

	public static boolean isOnUiThread() {
		return Looper.myLooper() == Looper.getMainLooper();
	}

}
