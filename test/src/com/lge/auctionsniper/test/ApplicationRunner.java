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
		driver.showsSniperStatus(itemId, 0, 0, R.string.status_joining);
	}

	public void stop() {
	}

	public void hasShownSniperIsBidding(int lastPrice, int lastBid) {
		driver.showsSniperStatus(itemId, lastPrice, lastBid,
				R.string.status_bidding);
	}

	public void hasShownSniperIsWinning(int price) {
		driver.showsSniperStatus(itemId, price, price, R.string.status_winning);
	}

	public void showsSniperHasWonAuction(int price) {
		driver.showsSniperStatus(itemId, price, price, R.string.status_won);
	}

	public void showsSniperHasLostAuction(int lastPrice, int lastBid) {
		driver.showsSniperStatus(itemId, lastPrice, lastBid,
				R.string.status_lost);
	}
}