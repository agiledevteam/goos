package com.lge.auctionsniper;

public class AuctionSniper implements AuctionEventListener {
	private boolean isWinning = false;
	private final Auction auction;
	private final SniperListener sniperListener;
	private String itemId;

	public AuctionSniper(String itemId, Auction auction,
			SniperListener sniperListener) {
		this.itemId = itemId;
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
			int bid = price + increment;
			auction.bid(bid);
			sniperListener.sniperStateChanged(new SniperSnapshot(itemId, price, bid,
					SniperState.BIDDING));
		}
	}

}
