package com.lge.auctionsniper;

public class AuctionSniper implements AuctionEventListener {

	private final Auction auction;
	private final SniperListener sniperListener;

	public AuctionSniper(Auction auction, SniperListener sniperListener) {
		this.auction = auction;
		this.sniperListener = sniperListener;
	}

	@Override
	public void auctionClosed() {
		sniperListener.sniperLost();
	}

	@Override
	public void currentPrice(int price, int increment, PriceSource priceSource) {
		if(priceSource == PriceSource.FromSniper){
			sniperListener.sniperWinning();
		}else{
			auction.bid(price + increment);
			sniperListener.sniperBidding();
		}
	}

}
