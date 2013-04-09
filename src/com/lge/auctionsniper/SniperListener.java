package com.lge.auctionsniper;

public interface SniperListener {

	void sniperLost();

	void sniperStateChanged(SniperSnapshot sniperState);

	void sniperWinning();

	void sniperWon();
}
