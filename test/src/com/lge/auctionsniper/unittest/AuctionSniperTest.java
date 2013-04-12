package com.lge.auctionsniper.unittest;

import org.jmock.Expectations;
import org.jmock.Mockery;

import com.lge.auctionsniper.Auction;
import com.lge.auctionsniper.AuctionEventListener.PriceSource;
import com.lge.auctionsniper.AuctionSniper;
import com.lge.auctionsniper.SniperListener;

import junit.framework.TestCase;

public class AuctionSniperTest extends TestCase {
	private final Mockery context = new Mockery();
	private final SniperListener sniperListener = context
			.mock(SniperListener.class);
	private final Auction auction = context.mock(Auction.class);
	private final AuctionSniper sniper = new AuctionSniper(auction,
			sniperListener);

	@Override
	protected void tearDown() throws Exception {
		context.assertIsSatisfied();
		super.tearDown();
	}

	public void testReportsLostWhenAuctionCloses() throws Exception {
		context.checking(new Expectations() {
			{
				one(sniperListener).sniperLost();
			}
		});
		sniper.auctionClosed();
	}

	public void testBidsHigherAndReportsBiddingWhenNewPriceArrives()
			throws Exception {
		final int price = 1001;
		final int increment = 25;
		context.checking(new Expectations() {
			{
				one(auction).bid(price + increment);
				atLeast(1).of(sniperListener).sniperBidding();
			}
		});
		sniper.currentPrice(price, increment, PriceSource.FromOtherBidder);
	}
}
