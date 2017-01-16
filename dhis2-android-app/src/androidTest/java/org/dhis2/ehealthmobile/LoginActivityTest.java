package org.dhis2.ehealthmobile;

import android.support.test.rule.ActivityTestRule;

import com.squareup.okhttp.mockwebserver.MockWebServer;

import org.dhis2.ehealthMobile.R;
import org.dhis2.ehealthMobile.ui.activities.LoginActivity;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class LoginActivityTest extends BaseInstrumentationTest{

	MockWebServer server;

	@Rule
	public ActivityTestRule<LoginActivity> rule = new ActivityTestRule<>(LoginActivity.class);

	@Before
	public void setup(){
		super.setup();
		server = new MockWebServer();
	}


	@Test
	public void loginShouldFail(){
		checkViewDisplayed(R.id.dhis2_logo);
		checkViewDisplayed(R.id.password);
		checkViewDisplayed(R.id.username);
		checkViewDisplayed(R.id.server_url);
		checkViewDisplayed(R.id.login_button);
		checkViewNotDisplayed(R.id.progress_bar);
	}
}
