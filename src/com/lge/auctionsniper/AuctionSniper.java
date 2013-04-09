package com.lge.auctionsniper;

public class AuctionSniper implements AuctionEventListener {
	private final Auction auction;
	private final SniperListener sniperListener;
	private SniperSnapshot snapshot;

	public AuctionSniper(String itemId, Auction auction,
			SniperListener sniperListener) {
		this.auction = auction;
		this.sniperListener = sniperListener;
		this.snapshot = SniperSnapshot.joining(itemId);
	}

	@Override
	public void auctionClosed() {
		snapshot = snapshot.auctionClosed();
		sniperListener.sniperStateChanged(snapshot);
	}

	@Override
	public void currentPrice(int price, int increment, PriceSource priceSource) {
		if (priceSource == PriceSource.FromSniper) {
			snapshot = snapshot.winning(price);
		} else {
			int bid = price + increment;
			auction.bid(bid);
			snapshot = snapshot.bidding(price, bid);
		}
		sniperListener.sniperStateChanged(snapshot);
	}

}
