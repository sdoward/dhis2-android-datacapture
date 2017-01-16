package org.dhis2.ehealthmobile;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.dhis2.ehealthMobile.BuildConfig;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 22)
public abstract class BaseRoboelectricTest {

	protected Context getContext(){
		return RuntimeEnvironment.application;
	}

	protected TextView textViewWithText(View view, String text) {
		if (view instanceof TextView) {
			String t = ((TextView) view).getText().toString();
			return t.equals(text) ? (TextView) view : null;
		} else if (view instanceof ViewGroup) {
			ViewGroup group = (ViewGroup) view;
			for (int i = 0; i < group.getChildCount(); i++) {
				View child = group.getChildAt(i);
				TextView childTextView = textViewWithText(child, text);
				if (childTextView != null)
					return childTextView;
			}
		}
		return null;
	}

}
