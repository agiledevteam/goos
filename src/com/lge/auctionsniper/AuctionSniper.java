package com.lge.auctionsniper;

public class AuctionSniper implements AuctionEventListener {
	private boolean isWinning = false;
	private final Auction auction;
	private final SniperListener sniperListener;

	public AuctionSniper(Auction auction, SniperListener sniperListener) {
		this.auction = auction;
		this.sniperListener = sniperListener;
	}

	@Override
	public void auctionClosed() {
		if (isWinning)
			sniperListener.sniperWon();
		else
			sniperListener.sniperLost();
	}

	@Override
	public void currentPrice(int price, int increment, PriceSource priceSource) {
		isWinning = priceSource == PriceSource.FromSniper;
		if (isWinning) {
			sniperListener.sniperWinning();
		} else {
			auction.bid(price + increment);
			sniperListener.sniperBidding();
		}
	}

}
