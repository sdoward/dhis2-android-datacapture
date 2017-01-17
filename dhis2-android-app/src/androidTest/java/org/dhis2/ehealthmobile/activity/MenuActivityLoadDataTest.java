package org.dhis2.ehealthmobile.activity;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import org.dhis2.ehealthMobile.R;
import org.dhis2.ehealthMobile.io.models.useraccount.UserAccount;
import org.dhis2.ehealthMobile.ui.activities.MenuActivity;
import org.dhis2.ehealthmobile.BaseInstrumentationTest;
import org.dhis2.ehealthmobile.util.SwipeRefreshLayoutIsRefreshingMatcher;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class MenuActivityLoadDataTest extends BaseInstrumentationTest{

	@Rule
	public ActivityTestRule<MenuActivity> rule = new ActivityTestRule<>(MenuActivity.class, true, false);

	String username = String.valueOf(new Random().nextLong());
	String smsNumber = String.valueOf(randomLong());
	UserAccount userAccount = new UserAccount.UserAccountBuilder()
			.setUsername(String.valueOf(randomLong()))
			.setFirstName(String.valueOf(randomLong()))
			.setSurname(String.valueOf(randomLong()))
			.setId(String.valueOf(randomLong()))
			.setEmail(String.valueOf(randomLong()))
			.createUserAccount();

	@Override
	public void setup() {
		super.setup();
		enqueueJsonResponse("api_dataStore_android_config", 10000);
		rule.launchActivity(new Intent());
	}

	@Test
	public void shouldShowRefreshLoaderWhileDownloading() throws InterruptedException {
		onView(withId(R.id.swipe_refresh_layout_aggregate_report)).check(matches(isDisplayed()));

		Thread.sleep(10000);
		onView(withId(R.id.swipe_refresh_layout_aggregate_report)).check(matches(new SwipeRefreshLayoutIsRefreshingMatcher(true)));
	}
}
