package com.lge.android.wl.driver;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import android.app.Activity;
import android.widget.TextView;

import com.objogate.wl.Query;

public class TextViewDriver extends ViewDriver<TextView> {

	public TextViewDriver(AndroidDriver<Activity> parentDriver, int resId) {
		super(parentDriver, resId);
	}

	public void hasText(Matcher<String> equalTo) {
		has(text(), equalTo);
	}

	private Query<? super TextView, String> text() {
		return new Query<TextView, String>() {

			@Override
			public void describeTo(Description desc) {
				desc.appendText("text");
			}

			@Override
			public String query(TextView view) {
				return view.getText().toString();
			}
		};
	}

	
}
