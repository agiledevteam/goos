package com.lge.auctionsniper.integration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
import android.database.DataSetObserver;
import android.test.AndroidTestCase;
import android.view.View;
import android.widget.LinearLayout;

import com.lge.auctionsniper.R;
import com.lge.auctionsniper.SniperListAdapter;
import com.lge.auctionsniper.SniperState;
import com.lge.auctionsniper.SniperListAdapter.ViewHolder;
import com.lge.auctionsniper.SniperSnapshot;

public class SniperListAdapterTest extends AndroidTestCase {
	private static final String ITEM_ID = "item-id";
	private SniperListAdapter adapter;
	private LinearLayout parent;
	private String methodCalls = "";
	private DataSetObserver observer = new DataSetObserver() {
		public void onChanged() {
			methodCalls = methodCalls + "onChanged ";
		}
	};

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		adapter = new SniperListAdapter(getContext());
		parent = new LinearLayout(getContext());
		adapter.registerDataSetObserver(observer);
		methodCalls = "";
	}

	public void testMakesDetailView() throws Exception {
		adapter.addSniper(SniperSnapshot.joining("item123"));
		View view = adapter.getView(0, null, parent);

		SniperListAdapter.ViewHolder holder = (ViewHolder) view.getTag();
		assertThat(holder, notNullValue());

		assertThat(holder.itemId, notNullValue());
		assertThat(holder.lastPrice, notNullValue());
		assertThat(holder.lastBid, notNullValue());
		assertThat(holder.status, notNullValue());
	}

	public void testShowsDetails() throws Exception {
		int price = 1000;
		int bid = 1098;

		adapter.addSniper(SniperSnapshot.joining(ITEM_ID));
		adapter.sniperStateChanged(new SniperSnapshot(ITEM_ID, price, bid, SniperState.BIDDING));

		View view = adapter.getView(0, null, parent);
		SniperListAdapter.ViewHolder holder = (ViewHolder) view.getTag();

		assertEquals(holder.itemId.getText(), ITEM_ID);
		assertEquals(holder.lastPrice.getText(), String.valueOf(price));
		assertEquals(holder.lastBid.getText(), String.valueOf(bid));
		assertEquals(holder.status.getText(),
				getContext().getString(R.string.status_bidding));
	}

	public void testNotifiesToListViewWhenSniperStateChanged() throws Exception {
		adapter.addSniper(SniperSnapshot.joining(ITEM_ID));
		int price = 1000;
		int bid = 1098;
		adapter.sniperStateChanged(new SniperSnapshot(ITEM_ID, price, bid, SniperState.BIDDING));

		assertThat(methodCalls, containsString("onChanged "));
	}
	
	public void testNotifiesToListViewWhenSniperAdded() throws Exception {
		assertEquals(0, adapter.getCount());
		adapter.addSniper(SniperSnapshot.joining("item123"));
		assertEquals(1, adapter.getCount());
		
		assertThat(methodCalls, containsString("onChanged "));
	}
	
	public void testHoldsSniperInAdditionOrder() throws Exception {
		adapter.addSniper(SniperSnapshot.joining("item 0"));
		adapter.addSniper(SniperSnapshot.joining("item 1"));
		
		assertRowMatchesItemId(0, "item 0");
		assertRowMatchesItemId(1, "item 1");
	}
	
	public void testUpdateCorrectRowForSniper() throws Exception {
		SniperSnapshot joining0 = SniperSnapshot.joining("item 0");
		SniperSnapshot joining1 = SniperSnapshot.joining("item 1");
		adapter.addSniper(joining0);
		adapter.addSniper(joining1);
		
		SniperSnapshot bidding1 = joining1.bidding(1000, 1098);
		adapter.sniperStateChanged(bidding1);
		
		assertRowMatchesSnapshot(0, joining0);
		assertRowMatchesSnapshot(1, bidding1);
	}

	public void testThrowsExceptionIfNoExistingSniperForAnUpdate() throws Exception {
		adapter.addSniper(SniperSnapshot.joining("item 0"));
		try {
			adapter.sniperStateChanged(SniperSnapshot.joining("notExistsItem"));
			fail("throw exception if no existing sniper for an update");
		} catch (IllegalStateException e) {
			
		}
	}
	private void assertRowMatchesSnapshot(int row, SniperSnapshot snapshot) {
		assertEquals(snapshot, adapter.getItem(row));
	}

	private void assertRowMatchesItemId(int row, String itemId) {
		View view = adapter.getView(row, null, parent);
		SniperListAdapter.ViewHolder holder = (ViewHolder) view.getTag();

		assertEquals(holder.itemId.getText(), itemId);
	}
	
}
