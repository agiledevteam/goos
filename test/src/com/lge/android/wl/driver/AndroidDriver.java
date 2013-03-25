package com.lge.android.wl.driver;

import com.jayway.android.robotium.solo.Solo;
import com.lge.android.wl.Selector;
import com.lge.android.wl.UIPollingProber;
import com.objogate.wl.Prober;


public class AndroidDriver<T> {
	protected final Solo solo;
	protected final Prober prober;
	protected final Selector<T> selector;

	public AndroidDriver(final Solo solo, int timeoutMillis) {
		this(solo, new UIPollingProber(solo, timeoutMillis, 100), new ActivitySelector<T>(solo));
	}

	public AndroidDriver(Solo solo, Prober prober, Selector<T> selector) {
		this.solo = solo;
		this.prober = prober;
		this.selector = selector;
	}
}
