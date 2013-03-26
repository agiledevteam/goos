package com.lge.auctionsniper.unittest;

import junit.framework.TestCase;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;
import org.jmock.Expectations;
import org.jmock.Mockery;

import com.lge.auctionsniper.AuctionEventListener;
import com.lge.auctionsniper.AuctionEventListener.PriceSource;
import com.lge.auctionsniper.AuctionMessageTranslator;

public class AuctionMessageTranslatorTest extends TestCase {
	private final String SNIPER_ID = "sniper";
	private final String SNIPER_ID_WITH_DOMAIN = SNIPER_ID + "@localhost";
	private final Mockery context = new Mockery();
	private final AuctionEventListener listener = context
			.mock(AuctionEventListener.class);
	private final AuctionMessageTranslator translator = new AuctionMessageTranslator(
			SNIPER_ID_WITH_DOMAIN, listener);
	private final Chat UNUSED_CHAT = null;

	@Override
	protected void tearDown() throws Exception {
		context.assertIsSatisfied(); // jMock requires this
		super.tearDown();
	}

	public void testNotifiesAuctionClosedWhenCloseMessageReceived()
			throws Exception {
		context.checking(new Expectations() {
			{
				oneOf(listener).auctionClosed();
			}
		});
		Message message = new Message();
		message.setBody("SOLVersion: 1.1; Event: CLOSE;");
		translator.processMessage(UNUSED_CHAT, message);
	}

	public void testNotifiesBidDetailsWhenCurrentPriceMessageReceivedFromSniper()
			throws Exception {
		context.checking(new Expectations() {
			{
				exactly(1).of(listener).currentPrice(192, 7,
						PriceSource.FromSniper);
			}
		});
		Message message = new Message();
		message.setBody("SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: "
				+ SNIPER_ID + ";");
		translator.processMessage(UNUSED_CHAT, message);
	}

	public void testNotifiesBidDetailsWhenCurrentPriceMessageReceivedFromOtherBidder()
			throws Exception {
		context.checking(new Expectations() {
			{
				exactly(1).of(listener).currentPrice(192, 7,
						PriceSource.FromOtherBidder);
			}
		});
		Message message = new Message();
		message.setBody("SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: Someone else;");
		translator.processMessage(UNUSED_CHAT, message);
	}
}
