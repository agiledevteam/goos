package com.lge.auctionsniper;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;

public class XMPPAuction implements Auction {
	private final Chat chat;
	public static final String JOIN_COMMAND_FORMAT = "SOLVersion: 1.1; Command: JOIN;";
	public static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: BID; Price: %d;";

	XMPPAuction(Chat chat) {
		this.chat = chat;
	}

	@Override
	public void bid(int amount) {
		try {
			chat.sendMessage(String.format(XMPPAuction.BID_COMMAND_FORMAT, amount));
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void join() {
		try {
			chat.sendMessage(XMPPAuction.JOIN_COMMAND_FORMAT);
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}
}