package com.lge.auctionsniper;

public enum SniperState {
	JOINING {
		@Override
		public SniperState closed() {
			return LOST;
		}
	}, BIDDING {
		@Override
		public SniperState closed() {
			return LOST;
		}
	}, WINNING {
		@Override
		public SniperState closed() {
			return WON;
		}
	}, WON, LOST;

	public SniperState closed() {
		throw new IllegalStateException("auction has been closed.");
	}
}
