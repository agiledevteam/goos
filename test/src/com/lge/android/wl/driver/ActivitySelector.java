package com.lge.android.wl.driver;

import org.hamcrest.Description;

import com.jayway.android.robotium.solo.Solo;
import com.lge.android.wl.Selector;

public class ActivitySelector<T> implements Selector<T> {

	private final Solo solo;

	public ActivitySelector(Solo solo) {
		this.solo = solo;
	}

	@Override
	public void describeFailureTo(Description arg0) {
	}

	@Override
	public boolean isSatisfied() {
		return true;
	}

	@Override
	public void probe() {
	}

	@Override
	public void describeTo(Description description) {
	}

	@SuppressWarnings("unchecked")
	@Override
	public T view() {
		return (T) solo.getCurrentActivity();
	}

}
