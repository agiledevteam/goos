package com.lge.auctionsniper.unittest;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;

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
	private final States	sniperState = context.states("sniper");

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
				allowing(sniperListener).sniperBidding(); then(sniperState.is("Bidding"));
				atLeast(1).of(sniperListener).sniperLost(); when(sniperState.is("Bidding"));
			}
		});
		sniper.currentPrice(123, 45, PriceSource.FromOtherBidder);
		sniper.auctionClosed();
	}
	
	public void testReportsWonIfAuctionClosesWhenWinning() throws Exception {
		context.checking(new Expectations() {
			{
				ignoring(auction);
				allowing(sniperListener).sniperWinning(); then(sniperState.is("Winning"));
				atLeast(1).of(sniperListener).sniperWon(); when(sniperState.is("Winning"));
			}
		});
		sniper.currentPrice(123, 45, PriceSource.FromSniper);
		sniper.auctionClosed();
	}
	
	public void testReportsIsWinningWhenCurrentPriceComesFromSniper() throws Exception {
		context.checking(new Expectations() {
			{
				atLeast(1).of(sniperListener).sniperWinning();
			}
		});
		sniper.currentPrice(123, 45, PriceSource.FromSniper);
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
