package com.lge.auctionsniper.test;

import com.jayway.android.robotium.solo.Solo;
import com.lge.auctionsniper.R;
import com.lge.auctionsniper.test.util.TextViewAsserts;

public class AuctionSniperDriver {
	private Solo solo;

	public AuctionSniperDriver(Solo solo) {
		this.solo = solo;
	}

	public void showsSniperStatus(int resId) {
		final String statusText = solo.getString(resId);
		new TextViewAsserts(solo, R.id.sniper_status).hasText(statusText);
	}

	public void clickJoinButton() {
		solo.clickOnButton(0);
	}
}