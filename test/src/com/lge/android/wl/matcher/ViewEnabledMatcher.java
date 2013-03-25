package com.lge.android.wl.matcher;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import android.view.View;

public class ViewEnabledMatcher extends TypeSafeMatcher<View> {

	@Override
	public void describeTo(Description arg0) {
		arg0.appendText("enabled");
	}

	@Override
	protected boolean matchesSafely(View arg0) {
		return arg0.isEnabled();
	}
	
}