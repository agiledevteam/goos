package com.lge.android.wl.probe;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import com.objogate.wl.Probe;
import com.lge.android.wl.Selector;

public class ViewAssertionProbe<T> implements Probe {

	private Matcher<? super T> assertion;
	private Selector<T> selector;
	private boolean assertionMet = false;

	public ViewAssertionProbe(Selector<T> selector, Matcher<? super T> assertion) {
		this.assertion = assertion;
		this.selector = selector;
	}

	@Override
	public void describeTo(Description description) {
		description.appendDescriptionOf(selector)
				.appendText("\nand check that it is ")
				.appendDescriptionOf(assertion);
	}

	@Override
	public void describeFailureTo(Description description) {
		if (selector.isSatisfied()) {
			selector.describeTo(description);
			assertion.describeMismatch(selector.view(), description);
		} else {
			selector.describeFailureTo(description);
		}
	}

	@Override
	public boolean isSatisfied() {
		return assertionMet;
	}

	@Override
	public void probe() {
		selector.probe();
		assertionMet = selector.isSatisfied()
				&& assertion.matches(selector.view());
	}

}
