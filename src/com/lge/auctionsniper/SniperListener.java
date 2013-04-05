package com.lge.auctionsniper;

public interface SniperListener {

	void sniperLost();

	void sniperBidding(SniperState sniperState);

	void sniperWinning();

	void sniperWon();
}
