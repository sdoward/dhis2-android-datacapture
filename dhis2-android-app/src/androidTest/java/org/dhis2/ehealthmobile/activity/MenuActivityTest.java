package org.dhis2.ehealthmobile.activity;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;

import com.google.gson.Gson;

import org.dhis2.ehealthMobile.R;
import org.dhis2.ehealthMobile.io.models.OrganizationUnit;
import org.dhis2.ehealthMobile.io.models.useraccount.UserAccount;
import org.dhis2.ehealthMobile.network.Response;
import org.dhis2.ehealthMobile.ui.activities.MenuActivity;
import org.dhis2.ehealthMobile.utils.PrefUtils;
import org.dhis2.ehealthMobile.utils.TextFileUtils;
import org.dhis2.ehealthmobile.HttpClientInstrumentationTest;
import org.junit.Rule;
import org.junit.Test;

import java.net.HttpURLConnection;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.core.AllOf.allOf;

public class MenuActivityTest extends HttpClientInstrumentationTest {

	@Rule
	public ActivityTestRule<MenuActivity> rule = new ActivityTestRule<>(MenuActivity.class, true, false);

	String username = String.valueOf(randomLong());
	String smsNumber = String.valueOf(randomLong());
	UserAccount userAccount = new UserAccount.UserAccountBuilder()
			.setUsername(String.valueOf(randomLong()))
			.setFirstName(String.valueOf(randomLong()))
			.setSurname(String.valueOf(randomLong()))
			.setId(String.valueOf(randomLong()))
			.setEmail(String.valueOf(randomLong()))
			.createUserAccount();

	String orgUnitBontheId = "iQgaTATK59f";
	String orgUnitBontheLabel = String.valueOf(randomLong());

	@Override
	public void setup() {
		super.setup();

		TextFileUtils.writeTextFile(InstrumentationRegistry.getTargetContext(), TextFileUtils.Directory.ROOT,
				TextFileUtils.FileNames.ACCOUNT_INFO.toString(), new Gson().toJson(userAccount));

		PrefUtils.initAppData(getContext(), "creds", username, "http://www.something.com");
		setSmsNumber(smsNumber);

		rule.launchActivity(new Intent());
	}

	protected Context getContext() {
		return InstrumentationRegistry.getInstrumentation().getTargetContext();
	}

	@Override
	public Response getDatasets() {

		String original = "\"id\": \"iQgaTATK59f\",\n" +
				"      \"label\": \"Bonthe\",";
		String replacement = String.format("\"id\": \"iQgaTATK59f\",\n" +
				"      \"label\": \"%s\",", orgUnitBontheLabel);

		String body = loadJson("api_me_assignedDataSets").replace(original, replacement);
		return new Response(HttpURLConnection.HTTP_OK, body);
	}

	@Test
	public void shouldShowUi(){

		onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
		onView(withId(R.id.drawer_layout)).check(matches(isDisplayed()));
		onView(withId(R.id.content_frame)).check(matches(isDisplayed()));

		onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
		onView(withId(R.id.navigation_view)).check(matches(isDisplayed()));
	}

	@Test
	public void navigationViewShouldDisplayUsernameAndEmail(){

		onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
		onView(withId(R.id.navigation_view)).check(matches(isDisplayed()));

		checkViewWithTextIsDisplayed(username);
		checkViewWithTextIsDisplayed(userAccount.email);
	}

	@Test
	public void navigationMenuShouldContainSomeElements(){

		onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
		onView(allOf(withText(R.string.aggregate_reporting), isDescendantOfA(withId(R.id.navigation_view)))).check(matches(isDisplayed()));
		onView(allOf(withText(R.string.profile), isDescendantOfA(withId(R.id.navigation_view)))).check(matches(isDisplayed()));
		onView(allOf(withText(R.string.log_out), isDescendantOfA(withId(R.id.navigation_view)))).check(matches(isDisplayed()));

	}

	@Test
	public void clickOnLogOutShouldDeleteUserDataAndShowLoginScreen(){

		onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
		onView(allOf(withText(R.string.log_out), isDescendantOfA(withId(R.id.navigation_view)))).perform(click());

		onView(withId(R.id.login_button)).check(matches(isDisplayed()));

		String userAccountDetails = TextFileUtils.readTextFile(getContext(),
				TextFileUtils.Directory.ROOT,
				TextFileUtils.FileNames.ACCOUNT_INFO.toString());
		assertThat(userAccountDetails).isNullOrEmpty();

		assertThat(PrefUtils.getUserName(getContext())).isNullOrEmpty();

		assertThat(PrefUtils.getServerURL(getContext())).isNullOrEmpty();
	}

	@Test
	public void shouldHaveDownloadedSmsNumber() throws InterruptedException {
		onView(withId(R.id.swipe_refresh_layout_aggregate_report)).check(matches(isDisplayed()));
		assertThat(PrefUtils.getSmsNumber(getContext())).isEqualTo(smsNumber);
	}

	@Test
	public void shouldHaveDownloadedConfigFile(){
		onView(withId(R.id.swipe_refresh_layout_aggregate_report)).check(matches(isDisplayed()));

		assertThat(PrefUtils.getConfigFile(InstrumentationRegistry.getTargetContext())).isNotEmpty();
	}

	@Test
	public void shouldHaveDownloadedAssignedDataSets() throws InterruptedException {
		onView(withId(R.id.swipe_refresh_layout_aggregate_report)).check(matches(isDisplayed()));

		String orgUnitsWithDatasets = TextFileUtils.readTextFile(getContext(), TextFileUtils.Directory.ROOT, TextFileUtils.FileNames.ORG_UNITS_WITH_DATASETS.toString());
		OrganizationUnit[] units = new Gson().fromJson(orgUnitsWithDatasets, OrganizationUnit[].class);

		boolean hasTestOrganizationUnitId = false;
		for(OrganizationUnit unit : units){
			if(unit.getId().equals(orgUnitBontheId)){
				hasTestOrganizationUnitId = unit.getLabel().equals(orgUnitBontheLabel);
				break;
			}
		}

		assertThat(hasTestOrganizationUnitId).isTrue();
	}

	@Test
	public void shouldOpenProfileScreen() throws InterruptedException {
		onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
		onView(allOf(withText(R.string.profile), isDescendantOfA(withId(R.id.navigation_view)))).perform(click());

		onView(withId(R.id.swipe_refresh_layout_my_profile)).check(matches(isDisplayed()));
		// TODO: MyProfileFragment is creating FieldAdapter twice and the second time is empty
	}

	@Test
	public void shouldSwitchBetweenAggregateReportAndProfile(){
		onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
		onView(allOf(withText(R.string.profile), isDescendantOfA(withId(R.id.navigation_view)))).perform(click());
		onView(withId(R.id.swipe_refresh_layout_my_profile)).check(matches(isDisplayed()));

		onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
		onView(allOf(withText(R.string.aggregate_reporting), isDescendantOfA(withId(R.id.navigation_view)))).perform(click());
		onView(withId(R.id.swipe_refresh_layout_aggregate_report)).check(matches(isDisplayed()));

		onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
		onView(allOf(withText(R.string.profile), isDescendantOfA(withId(R.id.navigation_view)))).perform(click());
		onView(withId(R.id.swipe_refresh_layout_my_profile)).check(matches(isDisplayed()));

		onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
		onView(allOf(withText(R.string.aggregate_reporting), isDescendantOfA(withId(R.id.navigation_view)))).perform(click());
		onView(withId(R.id.swipe_refresh_layout_aggregate_report)).check(matches(isDisplayed()));

	}

	@Test
	public void shouldOpenDataEntryActivity() throws InterruptedException {
		clickViewWithText(R.string.choose_data_set);

		onView(withId(R.id.listview_picker_items)).check(matches(isDisplayed()));
		clickViewWithText("IDSR Weekly Disease Report(WDR)");
		clickViewWithText(R.string.choose_period);
		onData(anything()).inAdapterView(withId(R.id.dates_listview)).atPosition(0).perform(click());

		clickViewWithText(R.string.open_form);
		onView(withId(R.id.coordinator_layout_data_entry)).check(matches(isDisplayed()));

		Thread.sleep(10000);

	}
}