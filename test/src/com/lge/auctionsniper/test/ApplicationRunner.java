package com.lge.auctionsniper.test;

import com.jayway.android.robotium.solo.Solo;
import com.lge.auctionsniper.R;

public class ApplicationRunner {
	public static final String SNIPER_XMPP_ID = "sniper@localhost";
	private AuctionSniperDriver driver;
	private String itemId;

	public ApplicationRunner() {
	}

	public void startBiddingIn(FakeAuctionServer auction, Solo solo) {
		itemId = auction.getItemId();
		driver = new AuctionSniperDriver(solo, 1000);
		driver.clickJoinButton();
		driver.showsSniperStatus(R.string.status_joining);
	}

	public void showsSniperHasLostAuction() {
		driver.showsSniperStatus(R.string.status_lost);
	}

	public void hasShownSniperIsBidding() {
		driver.showsSniperStatus(R.string.status_bidding);
	}

	public void hasShownSniperIsWinning() {
		driver.showsSniperStatus(R.string.status_winning);
	}

	public void showsSniperHasWonAuction() {
		driver.showsSniperStatus(R.string.status_won);
	}

	public void stop() {
	}
}