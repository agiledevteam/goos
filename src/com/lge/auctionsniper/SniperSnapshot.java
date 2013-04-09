package com.lge.auctionsniper;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class SniperSnapshot {

	public final String itemId;
	public final int price;
	public final int bid;
	public final SniperState state;

	public SniperSnapshot(String itemId, int price, int bid, SniperState state) {
		this.itemId = itemId;
		this.price = price;
		this.bid = bid;
		this.state = state;
	}

	@Override
	public boolean equals(Object o) {
		return EqualsBuilder.reflectionEquals(this, o);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
