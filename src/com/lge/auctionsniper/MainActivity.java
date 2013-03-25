package com.lge.auctionsniper;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

    private Chat notToBeGCd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	public void join(String host, String username, String password,
			String itemId) throws XMPPException {
		XMPPConnection connection;
		connection = connectTo(host, username, password);
		final Chat chat = connection.getChatManager().createChat(auctionId(itemId, connection), new MessageListener() {

			@Override
			public void processMessage(Chat aChat, Message message) {
				TextView textView = (TextView) findViewById(R.id.sniper_status);
				textView.setText(R.string.status_lost);
			}
		});
		this.notToBeGCd = chat;
		chat.sendMessage(new Message());
	}

	private String auctionId(String itemId, XMPPConnection connection) {
		return String.format("auction-%s@%s",itemId, connection.getServiceName());
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
