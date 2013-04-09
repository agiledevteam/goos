package com.lge.auctionsniper;

public enum SniperState {
	JOINING {
		@Override
		public SniperState auctionClosed() {
			return LOST;
		}
	}, BIDDING {
		@Override
		public SniperState auctionClosed() {
			return LOST;
		}
	}, WINNING {
		@Override
		public SniperState auctionClosed() {
			return WON;
		}
	}, WON, LOST;

	public SniperState auctionClosed() {
		throw new IllegalStateException("auction has been closed.");
	}
}
