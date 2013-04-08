package com.lge.auctionsniper.test;

import static org.hamcrest.Matchers.equalTo;
import android.app.Activity;

import com.jayway.android.robotium.solo.Solo;
import com.lge.android.wl.driver.AndroidDriver;
import com.lge.android.wl.driver.TextViewDriver;
import com.lge.auctionsniper.R;

public class AuctionSniperDriver extends AndroidDriver<Activity> {
	public AuctionSniperDriver(Solo solo, int timeout) {
		super(solo, timeout);
	}

	public void showsSniperStatus(int resId) {
		final String statusText = solo.getString(resId);
		new TextViewDriver(this, R.id.sniper_status).hasText(equalTo(statusText));
	}

	public void clickJoinButton() {
		solo.clickOnButton(0);
	}
}