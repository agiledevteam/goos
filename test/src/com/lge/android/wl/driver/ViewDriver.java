package com.lge.android.wl.driver;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jayway.android.robotium.solo.Solo;
import com.lge.android.wl.UIPollingProber;
import com.lge.android.wl.matcher.ViewEnabledMatcher;
import com.lge.android.wl.matcher.ViewForcusedMatcher;
import com.lge.android.wl.matcher.ViewVisibleMatcher;
import com.lge.android.wl.probe.ViewAssertionProbe;
import com.lge.android.wl.probe.ViewPropertyAssertionProbe;
import com.lge.android.wl.probe.ViewSetEnabledProbe;
import com.objogate.wl.Probe;
import com.objogate.wl.Query;


public class ViewDriver<T extends View> extends AndroidDriver<T> {

	public ViewDriver(final Solo solo, int timeoutMillis, int resId) {
		super(solo, new UIPollingProber(solo, timeoutMillis, 100),
				new ViewSelector<T>(solo, resId));
	}

	@SuppressWarnings("rawtypes")
	public ViewDriver(AndroidDriver parentDriver, Class<T> viewClass, final int resId) {
		super(parentDriver.solo, parentDriver.prober, new ViewSelector<T>(
				parentDriver.solo, viewClass, resId));
	}

	public void is(Matcher<? super T> criteria) {
		check(new ViewAssertionProbe<T>(selector, criteria));
	}

	public <P> void has(Query<? super T, P> query, Matcher<? super P> criteria) {
		check(new ViewPropertyAssertionProbe<T, P>(selector, query, criteria));
	}

	public void check(Probe probe) {
		prober.check(probe);
	}

	protected View getView(int resId) {
		return solo.getView(resId);
	}

	public static Matcher<View> enabled() {
		return new ViewEnabledMatcher();
	}

	public static Matcher<View> visible() {
		return new ViewVisibleMatcher();
	}

	public static Matcher<View> forcused() {
		return new ViewForcusedMatcher();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setEnabled(boolean enabled) {
		check(new ViewSetEnabledProbe(selector, enabled));
	}

	public static <T extends View> Query<T, Iterable<String>> allText() {
		return new Query<T, Iterable<String>>() {

			@Override
			public void describeTo(Description arg0) {
				arg0.appendText(" all text ");
			}

			@Override
			public List<String> query(T view) {
				List<String> text = new ArrayList<String>();
				for (View subView : getViews(view)) {
					if (subView instanceof TextView) {
						text.add(((TextView) subView).getText().toString());
					}
				}
				return text;
			}
		};
	}

	public static List<View> getViews(View parent) {
		final List<View> views = new ArrayList<View>();

		views.add(parent);

		if (parent instanceof ViewGroup) {
			addChildren(views, (ViewGroup) parent);
		}

		return views;
	}

	private static void addChildren(List<View> views, ViewGroup viewGroup) {
		for (int i = 0; i < viewGroup.getChildCount(); i++) {
			final View child = viewGroup.getChildAt(i);

			views.add(child);

			if (child instanceof ViewGroup) {
				addChildren(views, (ViewGroup) child);
			}
		}
	}

}