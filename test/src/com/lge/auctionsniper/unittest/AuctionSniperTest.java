package com.lge.auctionsniper.unittest;

import junit.framework.TestCase;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;

import com.lge.auctionsniper.Auction;
import com.lge.auctionsniper.AuctionEventListener.PriceSource;
import com.lge.auctionsniper.AuctionSniper;
import com.lge.auctionsniper.SniperListener;
import com.lge.auctionsniper.SniperSnapshot;
import com.lge.auctionsniper.SniperState;

public class AuctionSniperTest extends TestCase {
	protected static final String ITEM_ID = "item";
	private final Mockery context = new Mockery();
	private final SniperListener sniperListener = context
			.mock(SniperListener.class);
	private final Auction auction = context.mock(Auction.class);
	private final AuctionSniper sniper = new AuctionSniper(ITEM_ID, auction,
			sniperListener);

	private final States sniperState = context.states("sniper");

	@Override
	protected void tearDown() throws Exception {
		context.assertIsSatisfied();
		super.tearDown();
	}

	public void testReportsLostIfAuctionClosesImmediately() throws Exception {
		context.checking(new Expectations() {
			{
				atLeast(1).of(sniperListener).sniperLost();
			}
		});
		sniper.auctionClosed();
	}

	public void testReportsLostIfAuctionClosesWhenBidding() throws Exception {
		context.checking(new Expectations() {
			{
				ignoring(auction);
				allowing(sniperListener).sniperBidding(
						with(any(SniperSnapshot.class)));
				then(sniperState.is("bidding"));
				atLeast(1).of(sniperListener).sniperLost();
				when(sniperState.is("bidding"));
			}
		});
		sniper.currentPrice(123, 45, PriceSource.FromOtherBidder);
		sniper.auctionClosed();
	}

	public void testBidsHigherAndReportsBiddingWhenNewPriceArrives()
			throws Exception {
		final int price = 1001;
		final int increment = 25;
		final int bid = price + increment;
		context.checking(new Expectations() {
			{
				one(auction).bid(price + increment);
				atLeast(1).of(sniperListener).sniperBidding(
						new SniperSnapshot(ITEM_ID, price, bid,
								SniperState.BIDDING));
			}
		});
		sniper.currentPrice(price, increment, PriceSource.FromOtherBidder);
	}

	public void testReportsIsWinningWhenCurrentPriceComesFromSniper() {
		context.checking(new Expectations() {
			{
				atLeast(1).of(sniperListener).sniperWinning();
			}
		});
		sniper.currentPrice(123, 45, PriceSource.FromSniper);
	}

	public void testReportsWonIfAuctionClosesWhenWinning() {
		context.checking(new Expectations() {
			{
				ignoring(auction);
				allowing(sniperListener).sniperWinning();
				then(sniperState.is("winning"));
				atLeast(1).of(sniperListener).sniperWon();
				when(sniperState.is("winning"));
			}
		});
		sniper.currentPrice(123, 45, PriceSource.FromSniper);
		sniper.auctionClosed();
	}
}
