package com.lge.auctionsniper.test;

import com.jayway.android.robotium.solo.Solo;

public class ApplicationRunner {
	public static final String SNIPER_XMPP_ID = "sniper@localhost";
	private AuctionSniperDriver driver;

	public ApplicationRunner() {
	}

	public void startBiddingIn(FakeAuctionServer auction, Solo solo) {
		driver = new AuctionSniperDriver(solo, 1000);
		driver.clickJoinButton();
		driver.showsSniperStatus(R.string.status_joining);
	}

	public void showsSniperHasLostAuction() {
		driver.showsSniperStatus(R.string.status_lost);
	}

	public void stop() {
	}

}