package org.dhis2.ehealthmobile.activity;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import org.dhis2.ehealthMobile.R;
import org.dhis2.ehealthMobile.io.holders.DatasetInfoHolder;
import org.dhis2.ehealthMobile.ui.activities.DataEntryActivity;
import org.dhis2.ehealthmobile.BaseInstrumentationTest;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class DataEntryActivityTest extends BaseInstrumentationTest {

	@Rule
	public ActivityTestRule<DataEntryActivity> rule = new ActivityTestRule<>(DataEntryActivity.class, true, false);

	@Override
	public void setup() {
		super.setup();

		DatasetInfoHolder datasetInfoHolder = new DatasetInfoHolder();
		datasetInfoHolder.setPeriod("123");
		datasetInfoHolder.setPeriodLabel("123");

		Intent intent = new Intent();
		intent.putExtra(DataEntryActivity.KEY_DATASET_INFO_HOLDER, datasetInfoHolder);

		rule.launchActivity(intent);
	}


	@Test
	public void shouldShowUi(){
		onView(withId(R.id.coordinator_layout_data_entry)).check(matches(isDisplayed()));
	}
}
