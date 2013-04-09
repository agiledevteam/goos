package com.lge.auctionsniper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SniperListAdapter extends BaseAdapter {

	public class ViewHolder {
		public TextView itemId;
		public TextView lastPrice;
		public TextView lastBid;
		public TextView status;
	}

	private final Context context;
	private String status = "";
	private SniperSnapshot sniperState = new SniperSnapshot("", 0, 0, SniperState.JOINING);

	public SniperListAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return 1;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.sniper_list_item, parent, false);
			ViewHolder holder = new ViewHolder();
			holder.itemId = (TextView) convertView.findViewById(R.id.itemId_text);
			holder.lastPrice = (TextView) convertView.findViewById(R.id.lastPrice_text);
			holder.lastBid = (TextView) convertView.findViewById(R.id.lastBid_text);
			holder.status = (TextView) convertView.findViewById(R.id.status_text);
			convertView.setTag(holder);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		holder.itemId.setText(sniperState.itemId);
		holder.lastPrice.setText(String.valueOf(sniperState.price));
		holder.lastBid.setText(String.valueOf(sniperState.bid));
		holder.status.setText(status);
		return convertView;
	}

	private String getStateText(SniperState state) {
		switch (state) {
		case JOINING:
			return context.getString(R.string.status_joining);
		case BIDDING:
			return context.getString(R.string.status_bidding);
		case WINNING:
			return context.getString(R.string.status_winning);
		case WON:
			return context.getString(R.string.status_won);
		case LOST:
			return context.getString(R.string.status_lost);
		default:
			throw new IllegalArgumentException("Unknown state: " + state);
		}
	}

	public void setStatus(String status) {
		this.status = status;
		notifyDataSetChanged();
	}

	public void sniperStateChanged(SniperSnapshot sniperState) {
		this.sniperState = sniperState;
		this.status = getStateText(sniperState.state);
		notifyDataSetChanged();
	}
}
