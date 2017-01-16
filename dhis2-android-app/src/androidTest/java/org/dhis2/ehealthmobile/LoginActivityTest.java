package org.dhis2.ehealthmobile;

import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import org.dhis2.ehealthMobile.R;
import org.dhis2.ehealthMobile.ui.activities.LoginActivity;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.net.HttpURLConnection;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;

public class LoginActivityTest extends BaseInstrumentationTest{

	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";

	MockWebServer server;

	@Rule
	public ActivityTestRule<LoginActivity> rule = new ActivityTestRule<>(LoginActivity.class);

	@Before
	public void setup(){
		super.setup();
		server = new MockWebServer();
	}

	protected void typeLoginData(String url, String username, String password){

		typeTextInView(R.id.server_url, url);
		Espresso.closeSoftKeyboard();
		typeTextInView(R.id.username, username);
		Espresso.closeSoftKeyboard();
		typeTextInView(R.id.password, password);
		Espresso.closeSoftKeyboard();
	}

	protected void failLogin(String url, String username, String password, int responseCode, int toastMessage){

		server.enqueue(new MockResponse()
				.setResponseCode(responseCode)
				.setBody("")
				.addHeader("Content-Type", "application/json"));

		typeLoginData(url, username, password);

		clickView(R.id.login_button);
		onView(withText(toastMessage)).inRoot(withDecorView(not(is(rule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
	}


	@Test
	public void loginScreenShouldShowSomeViews(){

		checkViewDisplayed(R.id.dhis2_logo);
		checkViewDisplayed(R.id.password);
		checkViewDisplayed(R.id.username);
		checkViewDisplayed(R.id.server_url);
		checkViewDisplayed(R.id.login_button);

		checkViewNotDisplayed(R.id.progress_bar);
	}

	@Test
	public void loginScreenShouldRetainInputDataOnRotation(){

		rotateNatural();

		typeLoginData(server.url("").toString(), USERNAME, PASSWORD);

		rotateLeft();
		rotateRigt();
		rotateNatural();

		checkViewWithTextIsDisplayed(server.url("").toString());
		checkViewWithTextIsDisplayed(USERNAME);
		checkViewWithTextIsDisplayed(PASSWORD);
	}

	@Test
	public void loginShouldFailBecauseOfWrongCredentials() {
		failLogin(server.url("foo/bar").toString(), USERNAME, PASSWORD,HttpURLConnection.HTTP_UNAUTHORIZED, R.string.wrong_username_password);
	}

	@Test
	public void loginShouldFailBecauseOfWrongUrl() {
		failLogin("i'm not an url", USERNAME, PASSWORD, HttpURLConnection.HTTP_NOT_FOUND, R.string.wrong_url);
	}

	@Test
	public void loginShouldFailBecauseOfUnexpectedReason(){
		failLogin(server.url("").toString(), USERNAME, PASSWORD, Integer.MIN_VALUE, R.string.try_again);
	}
}
