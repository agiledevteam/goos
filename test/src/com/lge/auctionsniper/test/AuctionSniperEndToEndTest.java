package com.lge.auctionsniper.test;

import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;
import com.lge.auctionsniper.MainActivity;

public class AuctionSniperEndToEndTest extends
		ActivityInstrumentationTestCase2<MainActivity> {
	private FakeAuctionServer auction = new FakeAuctionServer("item-54321");
	private FakeAuctionServer auction2 = new FakeAuctionServer("item-65432");
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
		application.startBiddingIn(solo, auction);
		auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
		auction.announceClosed();
		application.showsSniperHasLostAuction(auction, 0, 0);
	}

	public void testSniperMakesAHigherBidButLoses() throws Exception {
		auction.startSellingItem();

		application.startBiddingIn(solo, auction);
		auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);

		auction.reportPrice(1000, 98, "other bidder");
		application.hasShownSniperIsBidding(auction, 1000, 1098);

		auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);
		auction.announceClosed();
		application.showsSniperHasLostAuction(auction, 1000, 1098);
	}

	public void testSniperWinsAnAuctionByBiddingHigher() throws Exception {
		auction.startSellingItem();
		
		application.startBiddingIn(solo, auction);
		auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
		
		auction.reportPrice(1000, 98, "other bidder");
		application.hasShownSniperIsBidding(auction, 1000, 1098); // last price, last bid
		
		auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);
		
		auction.reportPrice(1098, 97, ApplicationRunner.SNIPER_XMPP_ID);
		application.hasShownSniperIsWinning(auction, 1098); // winning price
		
		auction.announceClosed();
		application.showsSniperHasWonAuction(auction, 1098); // last price (win)
	}

	public void testSniperBidsMultipleItems() throws Exception {
		auction.startSellingItem();
		auction2.startSellingItem();
		
		application.startBiddingIn(solo, auction, auction2);
		auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
		auction2.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
		
		auction.reportPrice(1000, 98, "other bidder");
		application.hasShownSniperIsBidding(auction, 1000, 1098); // last price, last bid
		auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);

		auction2.reportPrice(500, 22, "other bidder");
		application.hasShownSniperIsBidding(auction2, 500, 522); // last price, last bid
		auction2.hasReceivedBid(522, ApplicationRunner.SNIPER_XMPP_ID);
		
		auction.reportPrice(1098, 97, ApplicationRunner.SNIPER_XMPP_ID);
		application.hasShownSniperIsWinning(auction, 1098); // winning price

		auction2.reportPrice(522, 21, ApplicationRunner.SNIPER_XMPP_ID);
		application.hasShownSniperIsWinning(auction2, 522); // winning price
		
		auction.announceClosed();
		auction2.announceClosed();
		application.showsSniperHasWonAuction(auction, 1098); // last price (win)
		application.showsSniperHasWonAuction(auction2, 522); // last price (win)
	}

	@Override
	public void tearDown() throws Exception {
		auction.stop();
		auction2.stop();
		application.stop();
		server.stop();
		super.tearDown();
	}
}