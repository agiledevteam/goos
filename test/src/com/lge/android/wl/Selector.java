package com.lge.android.wl;

import com.objogate.wl.Probe;

public interface Selector<T> extends Probe {
	T view();
}