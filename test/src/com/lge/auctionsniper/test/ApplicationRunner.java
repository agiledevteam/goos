package com.lge.auctionsniper.test;

import static com.lge.auctionsniper.test.FakeAuctionServer.XMPP_HOSTNAME;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;
import com.lge.auctionsniper.MainActivity;
import com.lge.auctionsniper.R;

public class ApplicationRunner {
	public static final String SNIPER_ID = "sniper";
	public static final String SNIPER_PASSWORD = "sniper";
	private AuctionSniperDriver driver;
	private ActivityInstrumentationTestCase2<MainActivity> inst;

	public ApplicationRunner(ActivityInstrumentationTestCase2<MainActivity> inst) {
		this.inst = inst;
	}

	public void startBiddingIn(FakeAuctionServer auction) {
		Intent intent = new Intent();
		intent.putExtra("hostname", XMPP_HOSTNAME);
		intent.putExtra("sniper_id", SNIPER_ID);
		intent.putExtra("sniper_password", SNIPER_PASSWORD);
		intent.putExtra("item_id", auction.getItemId());
		inst.setActivityIntent(intent);

		// start activity here
		driver = new AuctionSniperDriver(new Solo(inst.getInstrumentation(),
				inst.getActivity()));
		driver.showsSniperStatus(R.string.status_joining);
	}

	public void showsSniperHasLostAuction() {
		driver.showsSniperStatus(R.string.status_lost);
	}

	public void stop() {
	}

}