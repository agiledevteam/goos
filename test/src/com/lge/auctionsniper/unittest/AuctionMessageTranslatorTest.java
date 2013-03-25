package com.lge.auctionsniper.unittest;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;
import org.jmock.Expectations;
import org.jmock.Mockery;

import com.lge.auctionsniper.AuctionEventListener;
import com.lge.auctionsniper.AuctionMessageTranslator;

import junit.framework.TestCase;

public class AuctionMessageTranslatorTest extends TestCase {
	private final Mockery context = new Mockery();
	private final AuctionEventListener listener = context
			.mock(AuctionEventListener.class);
	private final AuctionMessageTranslator translator = new AuctionMessageTranslator(
			listener);
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

	public void testNotifiesBidDetailsWhenCurrentPriceMessageReceived()
			throws Exception {
		context.checking(new Expectations() {
			{
				exactly(1).of(listener).currentPrice(192, 7);
			}
		});
		Message message = new Message();
		message.setBody("SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: Someone else;");
		translator.processMessage(UNUSED_CHAT, message);
	}
}
