package com.lge.auctionsniper.test;

import android.test.ActivityInstrumentationTestCase2;

import com.lge.auctionsniper.MainActivity;

public class AuctionSniperEndToEndTest extends
		ActivityInstrumentationTestCase2<MainActivity> {
	private FakeAuctionServer auction = new FakeAuctionServer("item-54321");
	private ApplicationRunner application = new ApplicationRunner(this);

	public AuctionSniperEndToEndTest() {
		super(MainActivity.class);
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
	}

	public void testSniperJoinsAuctionUntilAuctionCloses() throws Exception {
		auction.startSellingItem();
		application.startBiddingIn(auction);
		auction.hasReceivedJoinRequestFromSniper();
		auction.announceClosed();
		application.showsSniperHasLostAuction();
	}

	@Override
	public void tearDown() throws Exception {
		auction.stop();
		application.stop();
		super.tearDown();
	}
}