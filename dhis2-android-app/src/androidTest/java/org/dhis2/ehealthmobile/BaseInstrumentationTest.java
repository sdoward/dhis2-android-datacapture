package org.dhis2.ehealthmobile;

import android.content.Context;
import android.os.RemoteException;
import android.support.test.espresso.Espresso;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import org.dhis2.ehealthMobile.utils.PrefUtils;
import org.dhis2.ehealthMobile.utils.TextFileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public abstract class BaseInstrumentationTest {

	private static final Random random = new Random();

	protected UiDevice device;
	private MockWebServer server;

	@Before
	public void setup() {
		device = UiDevice.getInstance(getInstrumentation());
		server = new MockWebServer();
		clearData();
	}

	@After
	public void tearDown(){
		clearData();
	}

	private void clearData(){
		// TODO: remove all stored data from shared preferences and files
		TextFileUtils.removeFile(getContext(), TextFileUtils.Directory.ROOT, TextFileUtils.FileNames.ORG_UNITS_WITH_DATASETS);
		PrefUtils.eraseData(getContext());
	}

	protected long randomLong() {
		return random.nextLong();
	}

	protected Context getContext() {
		return getInstrumentation().getTargetContext();
	}

	protected String serverUrl(String path) {
		return server.url(path).toString();
	}

	protected void enqueueJsonResponse(String jsonfile, long delayMs) {
		enqueueResponse(new MockResponse()
				.setResponseCode(HttpURLConnection.HTTP_OK)
				.setBody(loadJson(jsonfile))
				.addHeader("Content-Type", "application/json")
				.setBodyDelay(delayMs, TimeUnit.MILLISECONDS));
	}

	protected void enqueueJsonResponse(String filename) {
		enqueueJsonResponse(HttpURLConnection.HTTP_OK, loadJson(filename));
	}

	protected void enqueueJsonResponse(int code, String body) {
		enqueueResponse(new MockResponse()
				.setResponseCode(code)
				.setBody(body)
				.addHeader("Content-Type", "application/json"));
	}


	protected void enqueueResponse(MockResponse response) {
		server.enqueue(response);
	}

	protected void checkViewDisplayed(int id) {
		onView(withId(id)).check(matches(isDisplayed()));
	}

	protected void checkViewNotDisplayed(int id) {
		onView(withId(id)).check(matches(not(isDisplayed())));
	}

	protected void typeTextInView(int viewId, String text) {
		onView(withId(viewId)).perform(typeText(text));
		Espresso.closeSoftKeyboard();
	}

	protected void checkViewWithTextIsDisplayed(int stringId) {
		checkViewWithTextIsDisplayed(getContext().getString(stringId));
	}

	protected void checkViewWithTextIsDisplayed(String text) {
		onView(withText(text)).check(matches(isDisplayed()));
	}

	protected void clickViewWithId(int id) {
		onView(withId(id)).perform(click());
	}

	protected void clickViewWithText(int stringId){
		onView(withText(stringId)).perform(click());
	}

	protected void clickViewWithText(String text){
		onView(withText(text)).perform(click());
	}

	protected void rotateLeft() {
		try {
			device.setOrientationLeft();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	protected void rotateRight() {
		try {
			device.setOrientationRight();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	protected void rotateNatural() {
		try {
			device.setOrientationNatural();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	protected String loadJson(String filename) {
		if (!filename.endsWith(".json"))
			filename = filename + ".json";

		return readRawTextFile(String.format("json/%s", filename));
	}

	protected String readRawTextFile(String filename) {

		try {
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filename);//getContext().getResources().openRawResource(id);
			BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
			StringBuilder builder = new StringBuilder();
			String line;
			while ((line = r.readLine()) != null) {
				builder.append(line).append('\n');
			}

			return builder.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}