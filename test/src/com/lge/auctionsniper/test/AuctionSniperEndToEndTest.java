package com.lge.auctionsniper.test;

import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;
import com.lge.auctionsniper.MainActivity;

public class AuctionSniperEndToEndTest extends
		ActivityInstrumentationTestCase2<MainActivity> {
	private FakeAuctionServer auction = new FakeAuctionServer("item-54321");
	private ApplicationRunner application = new ApplicationRunner();
	private XmppServer server = new XmppServer();
	private Solo solo;

	public AuctionSniperEndToEndTest() {
		super(MainActivity.class);
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		server.start();
		solo = new Solo(getInstrumentation(), getActivity());
	}

	public void testSniperJoinsAuctionUntilAuctionCloses() throws Exception {
		auction.startSellingItem();
		application.startBiddingIn(auction, solo);
		auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
		auction.announceClosed();
		application.showsSniperHasLostAuction(0, 0);
	}

	public void testSniperMakesAHigherBidButLoses() throws Exception {
		auction.startSellingItem();

		application.startBiddingIn(auction, solo);
		auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);

		auction.reportPrice(1000, 98, "other bidder");
		application.hasShownSniperIsBidding(1000, 1098);

		auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);
		auction.announceClosed();
		application.showsSniperHasLostAuction(1000, 1098);
	}

	public void testSniperWinsAnAuctionByBiddingHigher() throws Exception {
		auction.startSellingItem();
		
		application.startBiddingIn(auction, solo);
		auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
		
		auction.reportPrice(1000, 98, "other bidder");
		application.hasShownSniperIsBidding(1000, 1098); // last price, last bid
		
		auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);
		
		auction.reportPrice(1098, 97, ApplicationRunner.SNIPER_XMPP_ID);
		application.hasShownSniperIsWinning(1098); // winning price
		
		auction.announceClosed();
		application.showsSniperHasWonAuction(1098); // last price (win)
	}

	@Override
	public void tearDown() throws Exception {
		auction.stop();
		application.stop();
		server.stop();
		super.tearDown();
	}
}