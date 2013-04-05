package com.lge.auctionsniper.integration;

import android.content.Context;
import android.database.DataSetObserver;
import android.test.AndroidTestCase;
import android.view.View;
import android.widget.LinearLayout;

import com.lge.auctionsniper.R;
import com.lge.auctionsniper.SniperListAdapter;
import com.lge.auctionsniper.SniperListAdapter.ViewHolder;
import com.lge.auctionsniper.SniperState;

public class SniperListAdapterTest extends AndroidTestCase {
	private static final String ITEM_ID = "item-id";
	private Context context;
	private SniperListAdapter adapter;
	private LinearLayout parent;
	protected boolean onChanged;
	private DataSetObserver observer = new DataSetObserver() {
		public void onChanged() {
			onChanged = true;
		}
	};

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		context = getContext();
		adapter = new SniperListAdapter(context);
		adapter.registerDataSetObserver(observer);
		parent = new LinearLayout(context);
	}

	public void testViewHolderContainsDetailViews() throws Exception {
		View itemView = adapter.getView(0, null, parent);
		SniperListAdapter.ViewHolder holder = (ViewHolder) itemView.getTag();
		assertNotNull(holder);
		assertNotNull(holder.itemId);
		assertNotNull(holder.price);
		assertNotNull(holder.bid);
		assertNotNull(holder.status);
	}

	public void testViewShowsPriceDetails() throws Exception {
		String bidding = context.getString(R.string.status_bidding);
		adapter.sniperStatusChanged(new SniperState(ITEM_ID, 1000, 1200), bidding);

		assertTrue(onChanged);
		
		View itemView = adapter.getView(0, null, parent);
		SniperListAdapter.ViewHolder holder = (ViewHolder) itemView.getTag();
		assertEquals(ITEM_ID, holder.itemId.getText().toString());
		assertEquals("1000", holder.price.getText().toString());
		assertEquals("1200", holder.bid.getText().toString());
		assertEquals(bidding, holder.status.getText().toString());
	}
}
