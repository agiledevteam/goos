package com.lge.auctionsniper.unittest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import java.util.ArrayList;
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
	final SimpleXMPPUser item0 = new SimpleXMPPUser("auction-item-54321", "auction");
	final SimpleXMPPUser item1 = new SimpleXMPPUser("auction-item-65432", "auction");

	private XmppServer server = new XmppServer();

	@Override
	protected void setUp() throws Exception {
		server.start();

		sniper.login();
		item0.login();
		item1.login();

		sniper.createChat("auction-item-54321@localhost/" + AUCTION_RESOURCE);
		sniper.createChat("auction-item-65432@localhost/" + AUCTION_RESOURCE);

		waitChat(item0);
		waitChat(item1);

		super.setUp();
	}

	public void testSniperRecievesFromItems() throws Exception {
		ExecutorService executor = Executors.newFixedThreadPool(10);

		Future<?> future = executor.submit(new Runnable() {
			@Override
			public void run() {
				try {
					item0.send(0, "MESSAGE FROM ITEM0");
					item0.send(0, "MESSAGE2 FROM ITEM0");
					item1.send(0, "MESSAGE FROM ITEM1");
					item1.send(0,"MESSAGE2 FROM ITEM1");
				} catch (XMPPException e) {
					Log.d("xmppserver",
							String.format("Exception :%s", e.getMessage()));
				} catch (InterruptedException e) {
					Log.d("xmppserver",
							String.format("Exception :%s", e.getMessage()));
				}
			}
		});

		future.get();
		waitForAWhile();
		waitForAWhile();
		waitForAWhile();
		waitForAWhile();

		
		assertThat(sniper.messageLoged, containsString("MESSAGE FROM ITEM0"));
		assertThat(sniper.messageLoged, containsString("MESSAGE2 FROM ITEM0"));
		assertThat(sniper.messageLoged, containsString("MESSAGE FROM ITEM1"));
		assertThat(sniper.messageLoged, containsString("MESSAGE2 FROM ITEM1"));
	}

	public void testAllItemsRecievesFromSniper() throws Exception {
		ExecutorService executor = Executors.newFixedThreadPool(10);
		Future<?> future = executor.submit(new Runnable() {
			@Override
			public void run() {
				try {
					sniper.send(0, "MESSAGE TO ITEM0");
					sniper.send(0, "MESSAGE2 TO ITEM0");
					sniper.send(1, "MESSAGE TO ITEM1");
					sniper.send(1, "MESSAGE2 TO ITEM1");
				} catch (XMPPException e) {
					Log.d("xmppserver",
							String.format("Exception :%s", e.getMessage()));
				} catch (InterruptedException e) {
					Log.d("xmppserver",
							String.format("Exception :%s", e.getMessage()));
				}
			}
		});

		future.get();
		waitForAWhile();
		waitForAWhile();
		waitForAWhile();
		waitForAWhile();

		assertThat(item0.messageLoged + item1.messageLoged, containsString("MESSAGE TO ITEM0"));
		assertThat(item0.messageLoged + item1.messageLoged, containsString("MESSAGE2 TO ITEM0"));
		assertThat(item0.messageLoged + item1.messageLoged, containsString("MESSAGE TO ITEM1"));
		assertThat(item0.messageLoged + item1.messageLoged, containsString("MESSAGE2 TO ITEM1"));
	}	

	private void waitChat(SimpleXMPPUser user) throws InterruptedException {
		int timeout = 500000;
		while(user.chatCreated() == false) {
			if (--timeout < 0) {
				break;
			}
			waitForAWhile();
		}
	}
	
	private void waitForAWhile() throws InterruptedException {
		Thread.sleep(25);
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
		private ArrayList<Chat> chats = new ArrayList<Chat>();

		public SimpleXMPPUser(String id, String password) {
			this.id = id;
			this.password = password;

			connection = new XMPPConnection(new ConnectionConfiguration(
					XMPP_HOSTNAME, 5222));
		}

		public boolean chatCreated() {
			return chats.size() > 0;
		}

		public void send(int i, String message) throws XMPPException, InterruptedException {
			chats.get(i).sendMessage(message);
//			waitForAWhile();
		}

		public void close() {
			connection.disconnect();
		}

		public void login() throws XMPPException, InterruptedException {
			connection.connect();
			connection.login(id, password, AUCTION_RESOURCE);
			waitForAWhile();
			connection.getChatManager().addChatListener(new ChatManagerListener() {
				
				@Override
				public void chatCreated(Chat chat, boolean arg1) {
					if (arg1 == false) {
						chat.addMessageListener(new MessageListener() {
							
							@Override
							public void processMessage(Chat arg0, Message arg1) {
								messageLoged += arg1.getBody();
								Log.d("xmppserver", String.format(
										"received %s -> %s : %s",
										arg0.getParticipant(), id, arg1.getBody()));
							}
						});
						chats.add(chat);
						Log.d("xmppserver",
								String.format("chat with %s created thread(%s):%s",
										chat.getParticipant(), chat.getThreadID(),
										chat.toString()));
					}
				}
			});
			messageLoged = "";
		}

		public void createChat(String username) throws XMPPException, InterruptedException {
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
			chats .add(chat);
			Log.d("xmppserver",
					String.format("chat with %s created thread(%s):%s",
							chat.getParticipant(), chat.getThreadID(),
							chat.toString()));
			waitForAWhile();
		}
	}
}
