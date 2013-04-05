package com.lge.auctionsniper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SniperListAdapter extends BaseAdapter {

	private String status;
	private Context context;

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
		TextView view = new TextView(context);
		view.setText(status);
		return view;
	}

	public void setStatus(String status) {
		this.status = status;
		notifyDataSetChanged();
	}
}
