package com.lge.auctionsniper.test;

import static com.lge.android.wl.driver.ViewDriver.allText;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.allOf;
import android.app.Activity;

import com.jayway.android.robotium.solo.Solo;
import com.lge.android.wl.driver.AndroidDriver;
import com.lge.android.wl.driver.ListViewDriver;
import com.lge.auctionsniper.R;

public class AuctionSniperDriver extends AndroidDriver<Activity> {
	public AuctionSniperDriver(Solo solo, int timeout) {
		super(solo, timeout);
	}

	public void clickJoinButton() {
		solo.clickOnButton(0);
	}

	public void showsSniperStatus(String itemId, int lastPrice, int lastBid,
			int statusResId) {
		final String statusText = solo.getString(statusResId);
		new ListViewDriver(this, R.id.sniper_list).hasItem(
				allText(),
				allOf(hasItem(statusText), hasItem(itemId),
						hasItem(String.valueOf(lastPrice)),
						hasItem(String.valueOf(lastBid))));
	}
}