package com.lge.auctionsniper.test;

import com.jayway.android.robotium.solo.Solo;
import com.lge.auctionsniper.R;

public class ApplicationRunner {
	public static final String SNIPER_XMPP_ID = "sniper@localhost";
	private AuctionSniperDriver driver;

	public ApplicationRunner() {
	}

	public void startBiddingIn(Solo solo, FakeAuctionServer... auctions) {
		driver = new AuctionSniperDriver(solo, 1000);
		// TODO : remove joining itemId with hard-coded 
		driver.clickJoinButton();
		for (FakeAuctionServer auction : auctions) {
			driver.showsSniperStatus(auction.getItemId(), 0, 0, R.string.status_joining);
		}
	}

	public void stop() {
	}

	public void hasShownSniperIsBidding(FakeAuctionServer auction,
			int lastPrice, int lastBid) {
		driver.showsSniperStatus(auction.getItemId(), lastPrice, lastBid,
				R.string.status_bidding);
	}

	public void hasShownSniperIsWinning(FakeAuctionServer auction, int price) {
		driver.showsSniperStatus(auction.getItemId(), price, price, R.string.status_winning);
	}

	public void showsSniperHasWonAuction(FakeAuctionServer auction, int price) {
		driver.showsSniperStatus(auction.getItemId(), price, price, R.string.status_won);
	}

	public void showsSniperHasLostAuction(FakeAuctionServer auction, 
			int lastPrice, int lastBid) {
		driver.showsSniperStatus(auction.getItemId(), lastPrice, lastBid,
				R.string.status_lost);
	}
}