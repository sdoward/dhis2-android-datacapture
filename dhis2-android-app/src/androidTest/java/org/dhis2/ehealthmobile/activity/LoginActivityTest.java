package org.dhis2.ehealthmobile.activity;

import android.support.test.rule.ActivityTestRule;

import com.google.gson.Gson;

import org.dhis2.ehealthMobile.R;
import org.dhis2.ehealthMobile.io.models.useraccount.UserAccount;
import org.dhis2.ehealthMobile.ui.activities.LoginActivity;
import org.dhis2.ehealthMobile.utils.TextFileUtils;
import org.dhis2.ehealthmobile.HttpClientInstrumentationTest;
import org.junit.Rule;
import org.junit.Test;

import java.net.HttpURLConnection;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;

public class LoginActivityTest extends HttpClientInstrumentationTest {

	private static final String USERNAME = "MyUsername";
	private static final String PASSWORD = "MyPassword";


	@Rule
	public ActivityTestRule<LoginActivity> rule = new ActivityTestRule<>(LoginActivity.class);

	protected void typeLoginData(String username, String password){

		typeTextInView(R.id.username, username);
		typeTextInView(R.id.password, password);
	}

	protected void failLogin(String username, String password, int responseCode, int toastMessage){

		setLoginUserResponse(responseCode, "");

		typeLoginData(username, password);

		clickViewWithId(R.id.login_button);
		onView(withText(toastMessage)).inRoot(withDecorView(not(is(rule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
	}


	@Test
	public void loginScreenShouldShowSomeViews(){

		checkViewDisplayed(R.id.dhis2_logo);
		checkViewDisplayed(R.id.password);
		checkViewDisplayed(R.id.username);
		checkViewDisplayed(R.id.login_button);

		checkViewNotDisplayed(R.id.progress_bar);
	}

	@Test
	public void loginScreenShouldRetainInputDataOnRotation(){

		rotateNatural();

		typeLoginData(USERNAME, PASSWORD);

		rotateLeft();
		rotateRight();
		rotateNatural();

		checkViewWithTextIsDisplayed(USERNAME);
		checkViewWithTextIsDisplayed(PASSWORD);
	}

	@Test
	public void loginShouldFailBecauseOfWrongCredentials() {
		failLogin(USERNAME, PASSWORD, HttpURLConnection.HTTP_UNAUTHORIZED, R.string.wrong_username_password);
	}


	@Test
	public void loginShouldFailBecauseOfUnexpectedReason(){
		failLogin(USERNAME, PASSWORD, Integer.MIN_VALUE, R.string.try_again);
	}

	@Test
	public void loginShouldSucceedAndNavigateToMenuActivity() throws InterruptedException {
		setLoginUserResponse(HttpURLConnection.HTTP_OK, loadJson("api_me_user-account"));

		typeLoginData(USERNAME, PASSWORD);

		clickViewWithId(R.id.login_button);
		onView(withId(R.id.drawer_layout));

		Thread.sleep(1000);
		String userAccountJson = TextFileUtils.readTextFile(rule.getActivity(),
				TextFileUtils.Directory.ROOT,
				TextFileUtils.FileNames.ACCOUNT_INFO.toString());

		UserAccount userAccount = new Gson().fromJson(userAccountJson, UserAccount.class);
		assertThat(userAccount.id).isEqualTo("abc123");
	}

	@Test
	public void loginButtonShouldBeEnabledWhenAllFieldsAreTyped(){

		typeTextInView(R.id.username, "");
		typeTextInView(R.id.password, "");

		onView(withId(R.id.login_button)).check(matches(not(isEnabled())));

		typeTextInView(R.id.username, "something");
		onView(withId(R.id.login_button)).check(matches(not(isEnabled())));

		typeTextInView(R.id.password, "something");
		onView(withId(R.id.login_button)).check(matches(isEnabled()));
	}
}
