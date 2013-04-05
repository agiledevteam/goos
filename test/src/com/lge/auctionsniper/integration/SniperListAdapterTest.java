package com.lge.auctionsniper.integration;

import android.content.Context;
import android.test.AndroidTestCase;
import android.view.View;

import com.lge.auctionsniper.SniperListAdapter;
import com.lge.auctionsniper.SniperListAdapter.ViewHolder;

public class SniperListAdapterTest extends AndroidTestCase {
	private Context context;
	private SniperListAdapter adapter;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		context = getContext();
		adapter = new SniperListAdapter(context);
	}

	public void testViewHolderContainsDetailViews() throws Exception {
		View itemView = adapter.getView(0, null, null);
		SniperListAdapter.ViewHolder holder = (ViewHolder) itemView.getTag();
		assertNotNull(holder);
		assertNotNull(holder.itemId);
		assertNotNull(holder.price);
		assertNotNull(holder.bid);
		assertNotNull(holder.status);
	}
}
