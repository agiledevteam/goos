package com.lge.auctionsniper.unittest;

import static org.hamcrest.Matchers.equalTo;
import junit.framework.TestCase;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;

import com.lge.auctionsniper.Auction;
import com.lge.auctionsniper.AuctionEventListener.PriceSource;
import com.lge.auctionsniper.AuctionSniper;
import com.lge.auctionsniper.SniperListener;
import com.lge.auctionsniper.SniperSnapshot;
import com.lge.auctionsniper.SniperState;
import static com.lge.auctionsniper.SniperState.*;

public class AuctionSniperTest extends TestCase {
	protected static final String ITEM_ID = "item-id";
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
				atLeast(1).of(sniperListener).sniperStateChanged(
						new SniperSnapshot(ITEM_ID, 0, 0, LOST));
			}
		});
		sniper.auctionClosed();
	}

	public void testReportsLostIfAuctionClosesWhenBidding() throws Exception {
		final int price = 123;
		final int increment = 45;
		final int bid = price + increment;
		context.checking(new Expectations() {
			{
				ignoring(auction);
				allowing(sniperListener).sniperStateChanged(
						with(aSniperThatIs(SniperState.BIDDING)));
				then(sniperState.is("bidding"));
				atLeast(1).of(sniperListener).sniperStateChanged(
						new SniperSnapshot(ITEM_ID, price, bid, LOST));
				when(sniperState.is("bidding"));
			}
		});
		sniper.currentPrice(price, increment, PriceSource.FromOtherBidder);
		sniper.auctionClosed();
	}

	protected Matcher<SniperSnapshot> aSniperThatIs(SniperState state) {
		return new FeatureMatcher<SniperSnapshot, SniperState>(equalTo(state),
				"state ", "was ") {
			@Override
			protected SniperState featureValueOf(SniperSnapshot snaptshot) {
				return snaptshot.state;
			}
		};
	}

	public void testBidsHigherAndReportsBiddingWhenNewPriceArrives()
			throws Exception {
		final int price = 1001;
		final int increment = 25;
		context.checking(new Expectations() {
			{
				final int bid = price + increment;
				one(auction).bid(bid);
				atLeast(1).of(sniperListener).sniperStateChanged(
						new SniperSnapshot(ITEM_ID, price, bid,
								SniperState.BIDDING));
			}
		});
		sniper.currentPrice(price, increment, PriceSource.FromOtherBidder);
	}

	public void testReportsIsWinningWhenCurrentPriceComesFromSniper() {
		final int price = 123;
		context.checking(new Expectations() {
			{
				atLeast(1).of(sniperListener).sniperStateChanged(
						new SniperSnapshot(ITEM_ID, price, price, WINNING));
			}
		});
		sniper.currentPrice(price, 45, PriceSource.FromSniper);
	}

	public void testReportsWonIfAuctionClosesWhenWinning() {
		final int price = 123;
		context.checking(new Expectations() {
			{
				ignoring(auction);
				allowing(sniperListener).sniperStateChanged(
						with(aSniperThatIs(WINNING)));
				then(sniperState.is("winning"));
				atLeast(1).of(sniperListener).sniperStateChanged(
						new SniperSnapshot(ITEM_ID, price, price, WON));
				when(sniperState.is("winning"));
			}
		});
		sniper.currentPrice(price, 45, PriceSource.FromSniper);
		sniper.auctionClosed();
	}
}
