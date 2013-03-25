package com.lge.auctionsniper.test;

import com.jayway.android.robotium.solo.Solo;

public class ApplicationRunner {
	private AuctionSniperDriver driver;

	public ApplicationRunner() {
	}

	public void startBiddingIn(FakeAuctionServer auction, Solo solo) {
		driver = new AuctionSniperDriver(solo, 1000);
		driver.showsSniperStatus("Joining");
	}

	public void showsSniperHasLostAuction() {
		driver.showsSniperStatus("Lost");
	}

	public void stop() {
	}

}