package com.lge.auctionsniper.unittest;

import org.jmock.Expectations;
import org.jmock.Mockery;

import com.lge.auctionsniper.AuctionSniper;
import com.lge.auctionsniper.SniperListener;

import junit.framework.TestCase;

public class AuctionSniperTest extends TestCase {
	private final Mockery context = new Mockery();
	private final SniperListener sniperListener = context
			.mock(SniperListener.class);
	private final AuctionSniper sniper = new AuctionSniper(sniperListener);

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
}
