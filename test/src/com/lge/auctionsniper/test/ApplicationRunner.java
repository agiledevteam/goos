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

	public void showsSniperHasLostAuction(int lastPrice, int lastBid) {
		driver.showsSniperStatus(itemId, lastPrice, lastBid,
				R.string.status_lost);
	}

	public void hasShownSniperIsBidding(int lastPrice, int lastBid) {
		driver.showsSniperStatus(itemId, lastPrice, lastBid,
				R.string.status_bidding);
	}

	public void hasShownSniperIsWinning(int winningBid) {
		driver.showsSniperStatus(itemId, winningBid, winningBid,
				R.string.status_bidding);
	}

	public void showsSniperHasWonAuction(int lastPrice) {
		driver.showsSniperStatus(itemId, lastPrice, lastPrice,
				R.string.status_bidding);
	}
}