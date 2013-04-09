package com.lge.auctionsniper;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity implements SniperListener {

	private Chat notToBeGCd;
	private SniperListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		adapter = new SniperListAdapter(this);
		ListView list = (ListView) findViewById(R.id.sniper_list);
		list.setAdapter(adapter);
		
		Button button = (Button) findViewById(R.id.join_button);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final String itemId = "item-54321";
				sniperStateChanged(SniperSnapshot.joining(itemId));
				// for now, we hard-coded the connection information
				// this will be replaced with user input.
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							joinAuction("localhost", "sniper", "sniper",
									itemId);
						} catch (XMPPException e) {
							e.printStackTrace();
						}
					}

				}).start();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private void joinAuction(String host, String username, String password,
			String itemId) throws XMPPException {
		XMPPConnection connection = connectTo(host, username, password);
		final Chat chat = connection.getChatManager().createChat(
				auctionId(itemId, connection), null);
		this.notToBeGCd = chat;

		Auction auction = new XMPPAuction(chat);
		chat.addMessageListener(new AuctionMessageTranslator(connection
				.getUser(), new AuctionSniper(itemId, auction, this)));
		auction.join();
	}

	private String auctionId(String itemId, XMPPConnection connection) {
		return String.format("auction-%s@%s", itemId,
				connection.getServiceName());
	}

	private XMPPConnection connectTo(String host, String username,
			String password) throws XMPPException {
		ConnectionConfiguration config = new ConnectionConfiguration(host, 5222);
		XMPPConnection connection = new XMPPConnection(config);
		connection.connect();
		connection.login(username, password);
		return connection;
	}

	@Override
	public void sniperStateChanged(final SniperSnapshot sniperState) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				adapter.sniperStateChanged(sniperState);
			}
		});
	}
}
