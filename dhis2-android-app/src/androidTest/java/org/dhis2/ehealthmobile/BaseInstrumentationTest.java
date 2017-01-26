package org.dhis2.ehealthmobile;

import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.dhis2.ehealthMobile.utils.PrefUtils;
import org.dhis2.ehealthMobile.utils.TextFileUtils;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

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



	@Before
	public void setup() {
		device = UiDevice.getInstance(getInstrumentation());
		clearData();
	}

	@After
	public void tearDown() {
		clearData();
	}

	private void clearData(){
		// TODO: remove all stored data from shared preferences and files
		TextFileUtils.removeFile(getInstrumentation().getTargetContext(), TextFileUtils.Directory.ROOT, TextFileUtils.FileNames.ORG_UNITS_WITH_DATASETS);
		PrefUtils.eraseData(getInstrumentation().getTargetContext());
	}

	protected long randomLong() {
		return random.nextLong();
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
		checkViewWithTextIsDisplayed(InstrumentationRegistry.getTargetContext().getString(stringId));
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
			InputStream inputStream = InstrumentationRegistry.getContext().getAssets().open(filename);
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

	protected Matcher<View> containsString(final String targetString){
		return new BoundedMatcher<View, View>(View.class) {
			@Override
			public void describeTo(Description description) {
				description.appendText("view should contain a TextView with text \""+targetString+"\"");
			}

			@Override
			protected boolean matchesSafely(View view) {
				if(view instanceof TextView){
					return ((TextView) view).getText().toString().equals(targetString);
				}else if(view instanceof ViewGroup){
					ViewGroup viewGroup = (ViewGroup) view;
					int childCount = viewGroup.getChildCount();
					for(int i=0;i<childCount;i++){
						boolean found = matchesSafely(viewGroup.getChildAt(i));
						if(found )
							return found;
					}
				}

				return false;
			}
		};
	}

}