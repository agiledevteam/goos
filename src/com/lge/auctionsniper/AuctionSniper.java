package com.lge.auctionsniper;

import static com.lge.auctionsniper.SniperState.BIDDING;
import static com.lge.auctionsniper.SniperState.JOINING;
import static com.lge.auctionsniper.SniperState.LOST;
import static com.lge.auctionsniper.SniperState.WINNING;
import static com.lge.auctionsniper.SniperState.WON;

public class AuctionSniper implements AuctionEventListener {
	private boolean isWinning = false;
	private final Auction auction;
	private final SniperListener sniperListener;
	private String itemId;
	private SniperSnapshot snapshot;

	public AuctionSniper(String itemId, Auction auction,
			SniperListener sniperListener) {
		this.itemId = itemId;
		this.auction = auction;
		this.sniperListener = sniperListener;
		this.snapshot = new SniperSnapshot(itemId, 0, 0, JOINING);
	}

	@Override
	public void auctionClosed() {
		if (isWinning) {
			snapshot = new SniperSnapshot(itemId, snapshot.price, snapshot.bid,
					WON);
		} else {
			snapshot = new SniperSnapshot(itemId, snapshot.price, snapshot.bid,
					LOST);
		}
		sniperListener.sniperStateChanged(snapshot);
	}

	@Override
	public void currentPrice(int price, int increment, PriceSource priceSource) {
		isWinning = priceSource == PriceSource.FromSniper;
		if (isWinning) {
			snapshot = new SniperSnapshot(itemId, price, price, WINNING);
		} else {
			int bid = price + increment;
			auction.bid(bid);
			snapshot = new SniperSnapshot(itemId, price, bid, BIDDING);
		}
		sniperListener.sniperStateChanged(snapshot);
	}

}
