package com.lge.auctionsniper;

import java.util.HashMap;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import com.lge.auctionsniper.AuctionEventListener.PriceSource;
public class AuctionMessageTranslator implements MessageListener {

	private AuctionEventListener listener;
	private String sniperId;

	public AuctionMessageTranslator(String sniperId, AuctionEventListener listener) {
		this.listener = listener;
		this.sniperId = sniperId;
	}

	@Override
	public void processMessage(Chat chat, Message message) {
		HashMap<String, String> event = unpackEventFrom(message);
		String type = event.get("Event");
		if ("CLOSE".equals(type)) {
			listener.auctionClosed();
		} else if ("PRICE".equals(type)) {
			listener.currentPrice(Integer.parseInt(event.get("CurrentPrice")),
					Integer.parseInt(event.get("Increment")), isFrom(event.get("Bidder")));
		}
	}
	private PriceSource isFrom(String bidderId) {
		String tempId = bidderId.split("@")[0];
		if(sniperId.compareTo(tempId) == 0) return PriceSource.FromSniper;
		else return PriceSource.FromOtherBidder;
	}

	private HashMap<String, String> unpackEventFrom(Message message) {
		HashMap<String, String> event = new HashMap<String, String>();
		for (String element: message.getBody().split(";")) {
			String[] pair = element.split(":");
			event.put(pair[0].trim(), pair[1].trim());
		}
		return event;
	}

}
