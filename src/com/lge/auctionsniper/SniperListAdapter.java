package com.lge.auctionsniper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SniperListAdapter extends BaseAdapter {

	private Context context;
	private String status = "";

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
			convertView = new TextView(context);
		}
		((TextView)convertView).setText(status );
		return convertView;
	}
	
	public void setStatus(String status) {
		this.status = status;
		notifyDataSetChanged();
	}

}
