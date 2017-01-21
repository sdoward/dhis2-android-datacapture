package org.dhis2.ehealthmobile.activity;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import org.dhis2.ehealthMobile.R;
import org.dhis2.ehealthMobile.io.holders.DatasetInfoHolder;
import org.dhis2.ehealthMobile.ui.activities.DataEntryActivity;
import org.dhis2.ehealthMobile.utils.TextFileUtils;
import org.dhis2.ehealthmobile.BaseInstrumentationTest;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;

public class DataEntryActivityTest extends BaseInstrumentationTest {

	@Rule
	public ActivityTestRule<DataEntryActivity> rule = new ActivityTestRule<>(DataEntryActivity.class, true, false);

	String username = String.valueOf(randomLong());

	@Override
	public void setup(){
		super.setup();

		String formId = "rq0LNr72Ndo";
		TextFileUtils.writeTextFile(getContext(), TextFileUtils.Directory.DATASETS, formId, loadJson("api_dataSets_rq0LNr72Ndo_form"));

		//PrefUtils.initAppData(getContext(), "creds", username, serverUrl(""));

		//enqueueJsonResponse("api_dataStore_android_config");

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	//	enqueueJsonResponse("api_dataSets_rq0LNr72Ndo_form");

		DatasetInfoHolder datasetInfoHolder = new DatasetInfoHolder();
		datasetInfoHolder.setPeriod("123");
		datasetInfoHolder.setPeriodLabel("123");
		datasetInfoHolder.setFormId(formId);

		Intent intent = new Intent();
		intent.putExtra(DataEntryActivity.KEY_DATASET_INFO_HOLDER, datasetInfoHolder);

		rule.launchActivity(intent);
	}

	@Test
	public void shouldShowUi() {
		onView(withId(R.id.coordinator_layout_data_entry)).check(matches(isDisplayed()));
	}

	@Test
	public void listViewShouldContainsCertainElements(){

		onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(0).check(matches(containsString("Acute Flaccid Paralysis")));
		onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(9).check(matches(containsString("Dengue Fever")));
		onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(44).check(matches(containsString("Tuberculosis")));

	}

	@Test
	public void additionalDiseaseFragmentShouldShowList() throws InterruptedException {
		clickViewWithText(R.string.add_disease_title);
		onView(withId(R.id.additional_diseases_listview)).check(matches(isDisplayed()));

	}
}
