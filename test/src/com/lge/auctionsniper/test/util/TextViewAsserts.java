package com.lge.auctionsniper.test.util;

import junit.framework.Assert;
import android.widget.TextView;

import com.jayway.android.robotium.solo.Solo;

public class TextViewAsserts {

	private Solo solo;
	private int resId;

	public TextViewAsserts(Solo solo, int resId) {
		this.solo = solo;
		this.resId = resId;
	}

	public void hasText(String expected) {
		if (!solo.waitForText(expected, 1, 2000)) {
			TextView tv = (TextView) solo.getView(resId);
			Assert.assertNotNull("Target view is not found.", tv);
			Assert.assertEquals(expected, tv.getText().toString());
		}
	}

}
