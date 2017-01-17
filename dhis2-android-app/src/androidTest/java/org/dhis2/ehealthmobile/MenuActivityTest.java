package org.dhis2.ehealthmobile;

import android.content.Intent;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;

import com.google.gson.Gson;

import org.dhis2.ehealthMobile.R;
import org.dhis2.ehealthMobile.io.models.useraccount.UserAccount;
import org.dhis2.ehealthMobile.ui.activities.MenuActivity;
import org.dhis2.ehealthMobile.utils.PrefUtils;
import org.dhis2.ehealthMobile.utils.TextFileUtils;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class MenuActivityTest extends BaseInstrumentationTest {

	@Rule
	public ActivityTestRule<MenuActivity> rule = new ActivityTestRule<>(MenuActivity.class, true, false);

	@Test
	public void shouldShowUi(){
		rule.launchActivity(new Intent());

		onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
		onView(withId(R.id.drawer_layout)).check(matches(isDisplayed()));
		onView(withId(R.id.content_frame)).check(matches(isDisplayed()));

		onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
		onView(withId(R.id.navigation_view)).check(matches(isDisplayed()));
	}

	@Test
	public void navigationViewShouldDisplayUsernameAndEmail(){

		// to be sure that we are showing data from here and not old stuff
		String username = String.valueOf(new Random().nextLong());
		String email = String.valueOf(new Random().nextLong());

		PrefUtils.initAppData(getContext(), "creds", username, "http://www.asd.com");
		UserAccount userAccount = new UserAccount();
		userAccount.email = email;
		userAccount.firstName = "MyFirstName";
		userAccount.surname = "MyLastName";
		TextFileUtils.writeTextFile(getContext(), TextFileUtils.Directory.ROOT,
				TextFileUtils.FileNames.ACCOUNT_INFO, new Gson().toJson(userAccount));

		rule.launchActivity(new Intent());

		onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
		onView(withId(R.id.navigation_view)).check(matches(isDisplayed()));

		checkViewWithTextIsDisplayed(username);
		checkViewWithTextIsDisplayed(email);
	}
}
