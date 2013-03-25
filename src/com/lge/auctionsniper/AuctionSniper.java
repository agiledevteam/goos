package com.lge.auctionsniper;

public class AuctionSniper implements AuctionEventListener {

	private final SniperListener sniperListern;

	public AuctionSniper(SniperListener sniperListener) {
		this.sniperListern = sniperListener;
	}

	@Override
	public void auctionClosed() {
	}

	@Override
	public void currentPrice(int price, int increment) {
	}

}
