package com.lge.android.wl.driver;

import org.hamcrest.Description;

import android.view.View;

import com.jayway.android.robotium.solo.Solo;
import com.lge.android.wl.Selector;

public class ViewSelector<T> implements Selector<T> {

	private final Solo solo;
	private final int resId;
	private Class<T> viewClass;
	private T found;

	public ViewSelector(Solo solo, int resId) {
		this(solo, null, resId);
	}

	public ViewSelector(Solo solo, Class<T> viewClass, int resId) {
		this.solo = solo;
		this.viewClass = viewClass;
		this.resId = resId;
	}

	@Override
	public void describeFailureTo(Description description) {
		description.appendText(getName() + " is not found.");
	}

	@Override
	public boolean isSatisfied() {
		return found != null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void probe() {
		View v = solo.getView(resId);
		if (v == null)
			return;
		if (viewClass != null && !v.getClass().equals(viewClass))
			return;
		found = (T) v;

	}

	@Override
	public void describeTo(Description description) {
		description.appendText(getName());
	}

	private String getName() {
		if (viewClass != null)
			return viewClass.getSimpleName() + "<" + resId + ">";
		else
			return "View <" + resId + ">";
	}

	@Override
	public T view() {
		return found;
	}

}
