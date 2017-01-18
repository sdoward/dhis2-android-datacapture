package org.dhis2.ehealthmobile.activity;

import android.content.Intent;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;

import com.google.gson.Gson;

import org.dhis2.ehealthMobile.R;
import org.dhis2.ehealthMobile.io.models.OrganizationUnit;
import org.dhis2.ehealthMobile.io.models.useraccount.UserAccount;
import org.dhis2.ehealthMobile.ui.activities.MenuActivity;
import org.dhis2.ehealthMobile.utils.PrefUtils;
import org.dhis2.ehealthMobile.utils.TextFileUtils;
import org.dhis2.ehealthmobile.BaseInstrumentationTest;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.AllOf.allOf;

public class MenuActivityTest extends BaseInstrumentationTest {

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

	String configFileFormId = String.valueOf(randomLong());
	String orgUnitBontheId = "iQgaTATK59f";
	String orgUnitBontheLabel = String.valueOf(randomLong());

	@Override
	public void setup() {
		super.setup();

		TextFileUtils.writeTextFile(getContext(), TextFileUtils.Directory.ROOT,
				TextFileUtils.FileNames.ACCOUNT_INFO, new Gson().toJson(userAccount));

		PrefUtils.initAppData(getContext(), "creds", username, serverUrl(""));

		enqueueJsonResponse(200, String.format("{\"smsNumber\": \"%s\"}", smsNumber));

		String configFile = loadJson("api_dataStore_android_config").replace("rq0LNr72Ndo", configFileFormId);
		enqueueJsonResponse(200, configFile);


		String original = "\"id\": \"iQgaTATK59f\",\n" +
				"      \"label\": \"Bonthe\",";
		String replacement = String.format("\"id\": \"iQgaTATK59f\",\n" +
				"      \"label\": \"%s\",", orgUnitBontheLabel);

		String datasetsFile = loadJson("api_me_assignedDataSets").replace(original, replacement);


		enqueueJsonResponse(200, datasetsFile);

		for (int i = 0; i < 10; i++)
			enqueueJsonResponse(200, "{}");

		rule.launchActivity(new Intent());
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

		try {
			String userAccountDetails = TextFileUtils.readTextFile(getContext(),
					TextFileUtils.Directory.ROOT,
					TextFileUtils.FileNames.ACCOUNT_INFO);
			fail("TextFileUtils.readTextFile throws an exception if the file doesn't exists");
		}catch(IllegalArgumentException e){
			assertThat(e).isNotNull();
		}

		assertThat(PrefUtils.getUserName(getContext())).isNullOrEmpty();

		// TODO: why delete the server url? typing it's so annoying...
		assertThat(PrefUtils.getServerURL(getContext())).isNullOrEmpty();
	}

	@Test
	public void shouldHaveDownloadedSmsNumber() throws InterruptedException {
		onView(withId(R.id.swipe_refresh_layout_aggregate_report)).check(matches(isDisplayed()));
		Thread.sleep(1000);

		assertThat(PrefUtils.getSmsNumber(getContext())).isEqualTo(smsNumber);
	}

	@Test
	public void shouldHaveDownloadedConfigFile(){
		onView(withId(R.id.swipe_refresh_layout_aggregate_report)).check(matches(isDisplayed()));

		assertThat(PrefUtils.getDiseaseConfigs(getContext(), configFileFormId).length()).isGreaterThan(1);
		assertThat(PrefUtils.getCompulsoryDiseases(getContext(), configFileFormId).length()).isGreaterThan(1);
	}

	@Test
	public void shouldHaveDownloadedAssignedDataSets() throws InterruptedException {
		onView(withId(R.id.swipe_refresh_layout_aggregate_report)).check(matches(isDisplayed()));

		String orgUnitsWithDatasets = TextFileUtils.readTextFile(getContext(), TextFileUtils.Directory.ROOT, TextFileUtils.FileNames.ORG_UNITS_WITH_DATASETS);
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
	public void shouldOpenProfileScreen() {
		onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
		onView(allOf(withText(R.string.profile), isDescendantOfA(withId(R.id.navigation_view)))).perform(click());

		onView(withId(R.id.swipe_refresh_layout_my_profile)).check(matches(isDisplayed()));
		// TODO: the list of fields shown here is empty...?

	}
}
