package com.lge.auctionsniper;


class UiThreadSniperListener implements SniperListener {
	private SniperListener listener;
	public UiThreadSniperListener(SniperListener listener) {
		this.listener = listener; 
	}
	@Override
	public void sniperStateChanged(final SniperSnapshot sniperState) {
		AndroidUtilities.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				listener.sniperStateChanged(sniperState);
			}
		});
	}
}