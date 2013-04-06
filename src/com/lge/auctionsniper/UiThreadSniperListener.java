package com.lge.auctionsniper;

public class UiThreadSniperListener implements SniperListener {

	private final SniperListener listener;

	public UiThreadSniperListener(SniperListener listener) {
		this.listener = listener;
	}

	@Override
	public void sniperStateChanged(final SniperSnapshot state) {
		AndroidUtilties.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				listener.sniperStateChanged(state);
			}
		});
	}
}
