package com.lge.auctionsniper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SniperListAdapter extends BaseAdapter {

	public static class ViewHolder {

		public TextView itemId;
		public TextView price;
		public TextView bid;
		public TextView status;

	}

	private String status;
	private Context context;
	private SniperSnapshot sniperStatus = new SniperSnapshot("", 0, 0,
			SniperState.JOINING);

	public SniperListAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return 1;
	}

	@Override
	public Object getItem(int position) {
		return status;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.sniper_list_item, parent,
					false);

			ViewHolder holder = new ViewHolder();
			holder.itemId = (TextView) convertView
					.findViewById(R.id.item_id_text);
			holder.price = (TextView) convertView.findViewById(R.id.price_text);
			holder.bid = (TextView) convertView.findViewById(R.id.bid_text);
			holder.status = (TextView) convertView
					.findViewById(R.id.status_text);
			convertView.setTag(holder);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		holder.status.setText(status);
		holder.itemId.setText(sniperStatus.itemId);
		holder.price.setText(Integer.toString(sniperStatus.lastPrice));
		holder.bid.setText(Integer.toString(sniperStatus.lastBid));
		return convertView;
	}

	public void setStatus(String status) {
		this.status = status;
		notifyDataSetChanged();
	}

	public void sniperStatusChanged(SniperSnapshot sniperStatus,
			String statusText) {
		this.sniperStatus = sniperStatus;
		this.status = statusText;
		notifyDataSetChanged();
	}
}
