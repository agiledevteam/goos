package com.lge.auctionsniper;

public interface SniperListener {

	void sniperLost();

	void sniperBidding(SniperSnapshot sniperState);

	void sniperWinning();

	void sniperWon();
}
