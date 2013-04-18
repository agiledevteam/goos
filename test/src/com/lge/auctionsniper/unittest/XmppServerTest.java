package com.lge.auctionsniper.unittest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import junit.framework.TestCase;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import com.lge.auctionsniper.test.XmppServer;

import android.util.Log;

public class XmppServerTest extends TestCase {

	public static final String XMPP_HOSTNAME = "localhost";
	public static final String AUCTION_RESOURCE = "Auction";

	final SimpleXMPPUser sniper = new SimpleXMPPUser("sniper", "sniper");
	final XMPPConnection item0 = new XMPPConnection(
			new ConnectionConfiguration(XMPP_HOSTNAME, 5222));
	final XMPPConnection item1 = new XMPPConnection(
			new ConnectionConfiguration(XMPP_HOSTNAME, 5222));
	SimpleChatManagerListener chat0 = new SimpleChatManagerListener();
	SimpleChatManagerListener chat1 = new SimpleChatManagerListener();
	private ExecutorService executor = Executors.newFixedThreadPool(10);

	private XmppServer server = new XmppServer();

	@Override
	protected void setUp() throws Exception {
		server.start();
		super.setUp();
	}

	public void testReceivesAllMessages() throws Exception {
		sniper.login();
		login(item0, "auction-item-54321", "auction");
		login(item1, "auction-item-65432", "auction");

		item0.getChatManager().addChatListener(chat0);
		item1.getChatManager().addChatListener(chat1);

		sniper.createChat("auction-item-54321@localhost/" + AUCTION_RESOURCE);
		sniper.createChat("auction-item-65432@localhost/" + AUCTION_RESOURCE);

		Future<?> future = executor.submit(new Runnable() {
			@Override
			public void run() {
				try {
					chat0.sendMessage("MESSAGE FROM ITEM0");
					chat0.sendMessage("MESSAGE2 FROM ITEM0");
					chat1.sendMessage("MESSAGE FROM ITEM1");
					chat1.sendMessage("MESSAGE2 FROM ITEM1");
				} catch (XMPPException e) {
					Log.d("xmppserver",
							String.format("Exception :%s", e.getMessage()));
				}
			}
		});

		future.get();

		assertThat(sniper.messageLoged, containsString("MESSAGE FROM ITEM0"));
		assertThat(sniper.messageLoged, containsString("MESSAGE2 FROM ITEM0"));
		assertThat(sniper.messageLoged, containsString("MESSAGE FROM ITEM1"));
		assertThat(sniper.messageLoged, containsString("MESSAGE2 FROM ITEM1"));

		// sniper.close();
		// item0.disconnect();
		// item1.disconnect();
	}

	private void login(XMPPConnection connection, String id, String password)
			throws XMPPException {
		connection.connect();
		connection.login(id, password, AUCTION_RESOURCE);
	}

	@Override
	protected void tearDown() throws Exception {
		server.stop();
		super.tearDown();
	}

	public class SimpleXMPPUser {

		private String id;
		private String password;
		private XMPPConnection connection;
		public String messageLoged = "";

		public SimpleXMPPUser(String id, String password) {
			this.id = id;
			this.password = password;

			connection = new XMPPConnection(new ConnectionConfiguration(
					XMPP_HOSTNAME, 5222));
		}

		public void close() {
			connection.disconnect();
		}

		public void login() throws XMPPException {
			connection.connect();
			connection.login(id, password, AUCTION_RESOURCE);
		}

		public void createChat(String username) throws XMPPException {
			Chat chat = connection.getChatManager().createChat(username,
					new MessageListener() {

						@Override
						public void processMessage(Chat arg0, Message arg1) {
							messageLoged += arg1.getBody();
							Log.d("xmppserver", String.format(
									"received %s -> %s : %s",
									arg0.getParticipant(), id, arg1.getBody()));
						}
					});
			chat.sendMessage("HELLO " + username);
			Log.d("xmppserver",
					String.format("chat with %s created thread(%s):%s",
							chat.getParticipant(), chat.getThreadID(),
							chat.toString()));
		}
	}

	class SimpleChatManagerListener implements ChatManagerListener {
		private Chat currentChat;

		@Override
		public void chatCreated(Chat chat, boolean arg1) {
			currentChat = chat;
			Log.d("xmppserver",
					String.format("chat with %s created thread(%s):%s",
							chat.getParticipant(), chat.getThreadID(),
							chat.toString()));
		}

		public void sendMessage(String message) throws XMPPException {
			currentChat.sendMessage(message);
		}
	}
}
