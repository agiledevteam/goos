package com.lge.auctionsniper;

import java.util.ArrayList;

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

public class MainActivity extends Activity {

	private ArrayList<Chat> notToBeGCd = new ArrayList<Chat>();
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
				final String [] itemIds = {"item-54321", "item-65432"};
				// for now, we hard-coded the connection information
				// this will be replaced with user input.
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							XMPPConnection connection = connectTo("localhost",
									"sniper", "sniper");
							for (String itemId : itemIds) {
								joinAuction(connection, itemId);
							}
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

	private void joinAuction(XMPPConnection connection, String itemId)
			throws XMPPException {
		safelyAddItemToView(SniperSnapshot.joining(itemId));
		final Chat chat = connection.getChatManager().createChat(
				auctionId(itemId, connection), null);
		notToBeGCd.add(chat);

		Auction auction = new XMPPAuction(chat);
		chat.addMessageListener(new AuctionMessageTranslator(connection
				.getUser(), new AuctionSniper(itemId, auction,
				new UiThreadSniperListener(adapter))));
		auction.join();
	}

	private void safelyAddItemToView(final SniperSnapshot snapshot) {
		AndroidUtilities.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				adapter.addSniper(snapshot);
			}
		});
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
}
