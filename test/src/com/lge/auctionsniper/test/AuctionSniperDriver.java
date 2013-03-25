package com.lge.auctionsniper.test;

import static org.hamcrest.CoreMatchers.equalTo;
import android.app.Activity;

import com.jayway.android.robotium.solo.Solo;
import com.lge.android.wl.driver.AndroidDriver;
import com.lge.android.wl.driver.TextViewDriver;
import com.lge.auctionsniper.R;

public class AuctionSniperDriver extends AndroidDriver<Activity> {
	public AuctionSniperDriver(Solo solo, int timeout) {
		super(solo, timeout);
	}

	public void showsSniperStatus(String statusText) {
		new TextViewDriver(this, R.id.sniper_status).hasText(equalTo(statusText));
	}
}