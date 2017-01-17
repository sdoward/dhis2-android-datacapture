package org.dhis2.ehealthmobile.util;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import org.hamcrest.Description;

public class SwipeRefreshLayoutIsRefreshingMatcher extends BoundedMatcher<View, SwipeRefreshLayout> {

	private boolean shouldBeRefreshing;

	public SwipeRefreshLayoutIsRefreshingMatcher(boolean shouldBeRefreshing) {
		super(SwipeRefreshLayout.class);
		this.shouldBeRefreshing = shouldBeRefreshing;
	}

	@Override
	protected boolean matchesSafely(SwipeRefreshLayout item) {
		return item.isRefreshing() == shouldBeRefreshing;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText(String.format("selected SwipeRefreshLayout should%s be refreshing", (shouldBeRefreshing ? "" : " not")));

	}
}
