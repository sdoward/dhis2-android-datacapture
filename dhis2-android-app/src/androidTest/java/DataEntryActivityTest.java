import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewAssertion;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import org.dhis2.mobile.R;
import org.dhis2.mobile.io.holders.DatasetInfoHolder;
import org.dhis2.mobile.ui.activities.DataEntryActivity;
import org.dhis2.mobile.utils.PrefUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.HttpURLConnection;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasFocus;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by george on 9/19/16.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class DataEntryActivityTest {
    private Context context;
    private MockWebServer server;
    private DatasetInfoHolder datasetInfo;
    private String urlSetByUser;

    @Rule
    public ActivityTestRule<DataEntryActivity> mActivityRule = new ActivityTestRule<DataEntryActivity>(DataEntryActivity.class, false, true){
        @Override
        protected Intent getActivityIntent() {
            context = InstrumentationRegistry.getTargetContext();
            datasetInfo = new DatasetInfoHolder();
            datasetInfo.setPeriod(DummyDataAndroidTest.PERIOD);
            datasetInfo.setPeriodLabel(DummyDataAndroidTest.PERIOD_LABEL);
            datasetInfo.setOrgUnitId(DummyDataAndroidTest.REPORT_ORG_ID);
            datasetInfo.setOrgUnitLabel(DummyDataAndroidTest.ORG_UNIT_LABEL);
            datasetInfo.setFormId(DummyDataAndroidTest.REPORT_FORM_ID);
            datasetInfo.setFormLabel(DummyDataAndroidTest.FORM_LABEL);
            Intent intent = new Intent(context, DataEntryActivity.class);
            intent.putExtra(DatasetInfoHolder.TAG, datasetInfo);

            return intent;
        }

        @Override
        protected void beforeActivityLaunched() {
            super.beforeActivityLaunched();
            server = new MockWebServer();

            urlSetByUser = PrefUtils.getServerURL(context);

            HttpUrl url = server.url("/");
            PrefUtils.setUrl(context, url.toString());

        }
    };


    @Before
    public void setUp() throws Exception {
         mActivityRule.getActivity();
        context = InstrumentationRegistry.getTargetContext();

        server.setBodyLimit(25000);

        server.enqueue(new MockResponse().addHeader("Content-Type", "application/json")
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(DummyDataAndroidTest.GOOD_REPORTS_DOWNLOAD_RESPONSE));


    }

    @Test
    public void checkListViewIsDisplayed(){
        onView(withId(R.id.list_of_fields)).check(matches(not(isDisplayed())));
    }

    @Test
    public void enterValue(){
        SystemClock.sleep(5000);
        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(0).onChildView(withId(R.id.edit_integer_pos_row))
                .perform(typeText("0"))
                .perform(pressImeActionButton());

    }

    /**
     * Validation tests
     */

    @Test
    public void deathsGreaterThanCasesValidationAndAcceptValueForUnderFives(){
        SystemClock.sleep(5000);

        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(0).onChildView(withId(R.id.edit_integer_pos_row))
                .perform(typeText("0"))
                .perform(pressImeActionButton());

        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(0).onChildView(withId(R.id.edit_integer_pos_row_2))
                .perform(replaceText("2"))
                .perform(pressImeActionButton());

        onView(withText("Validation")).check(matches(isDisplayed()));
        onView(withText("Yes")).perform(click());

        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(0).onChildView(withId(R.id.edit_integer_pos_row_2))
                .check(matches(withText("2")));

    }

    @Test
    public void deathsGreaterThanCasesValidationAndAcceptValueForOverFives(){
        SystemClock.sleep(5000);

        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(0).onChildView(withId(R.id.edit_integer_pos_row_3))
                .perform(typeText("0"))
                .perform(pressImeActionButton());

        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(0).onChildView(withId(R.id.edit_integer_pos_row_4))
                .perform(replaceText("2"))
                .perform(pressImeActionButton());

        onView(withText("Validation")).check(matches(isDisplayed()));
        onView(withText("Yes")).perform(click());

        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(0).onChildView(withId(R.id.edit_integer_pos_row_4))
                .check(matches(withText("2")));

    }

    @Test
    public void removeValueAfterDeathsGreaterThanCasesValidationForUnderFives(){
        SystemClock.sleep(5000);
        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(0).onChildView(withId(R.id.edit_integer_pos_row))
                .perform(typeText("0"))
                .perform(pressImeActionButton());

        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(0).onChildView(withId(R.id.edit_integer_pos_row_2))
                .perform(replaceText("2"))
                .perform(pressImeActionButton());

        onView(withText("Validation")).check(matches(isDisplayed()));
        onView(withText("Delete Value")).perform(click());

        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(0).onChildView(withId(R.id.edit_integer_pos_row_2))
                .check(matches(not(withText("2"))));

    }

    @Test
    public void removeValueAfterDeathsGreaterThanCasesValidationForOverFives(){
        SystemClock.sleep(5000);
        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(0).onChildView(withId(R.id.edit_integer_pos_row_3))
                .perform(typeText("0"))
                .perform(pressImeActionButton());

        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(0).onChildView(withId(R.id.edit_integer_pos_row_4))
                .perform(replaceText("2"))
                .perform(pressImeActionButton());

        onView(withText("Validation")).check(matches(isDisplayed()));
        onView(withText("Delete Value")).perform(click());

        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(0).onChildView(withId(R.id.edit_integer_pos_row_4))
                .check(matches(not(withText("2"))));

    }

    @Test
    public void shouldShowCriticalDiseaseValidationAndDeleteValue(){
        SystemClock.sleep(5000);
        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(5).onChildView(withId(R.id.edit_integer_pos_row))
                .perform(typeText("1"))
                .perform(pressImeActionButton());


        onView(withText("Validation")).check(matches(isDisplayed()));
        onView(withText("Delete Value")).perform(click());

        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(5).onChildView(withId(R.id.edit_integer_pos_row))
                .check(matches(not(withText("1"))));
    }

    @Test
    public void shouldShowCriticalDiseaseValidationAndAcceptValue(){
        SystemClock.sleep(5000);
        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(5).onChildView(withId(R.id.edit_integer_pos_row))
                .perform(typeText("1"))
                .perform(pressImeActionButton());


        onView(withText("Validation")).check(matches(isDisplayed()));
        onView(withText("Yes")).perform(click());

        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(5).onChildView(withId(R.id.edit_integer_pos_row))
                .check(matches(withText("1")));
    }

    @Test
    public void shouldShowCriticalDiseaseValidationForFieldsAndAcceptValue(){
        SystemClock.sleep(5000);
        //first field
        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(5).onChildView(withId(R.id.edit_integer_pos_row))
                .perform(typeText("1"))
                .perform(pressImeActionButton());


        onView(withText("Validation")).check(matches(isDisplayed()));
        onView(withText("Yes")).perform(click());

        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(5).onChildView(withId(R.id.edit_integer_pos_row))
                .check(matches(withText("1")));


        //Second field
        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(5).onChildView(withId(R.id.edit_integer_pos_row_2))
                .perform(replaceText("1"))
                .perform(pressImeActionButton());


        onView(withText("Validation")).check(matches(isDisplayed()));
        onView(withText("Yes")).perform(click());

        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(5).onChildView(withId(R.id.edit_integer_pos_row_2))
                .check(matches(withText("1")));

        //third
        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(5).onChildView(withId(R.id.edit_integer_pos_row_3))
                .perform(replaceText("1"))
                .perform(pressImeActionButton());


        onView(withText("Validation")).check(matches(isDisplayed()));
        onView(withText("Yes")).perform(click());

        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(5).onChildView(withId(R.id.edit_integer_pos_row_3))
                .check(matches(withText("1")));

        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(5).onChildView(withId(R.id.edit_integer_pos_row_4))
                .check(matches(hasFocus()));


    }

    /**
     * Auto Zero tests
     */

    @Test
    public void valueEnteredInFirstFieldShouldAutomaticallyAddZeroForOtherFields(){
        SystemClock.sleep(5000);
        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(0).onChildView(withId(R.id.edit_integer_pos_row))
                .perform(typeText("1"))
                .perform(pressImeActionButton());

        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(0).onChildView(withId(R.id.edit_integer_pos_row_2))
            .check(matches(withText("0")));

        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(0).onChildView(withId(R.id.edit_integer_pos_row_3))
                .check(matches(withText("0")));

        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(0).onChildView(withId(R.id.edit_integer_pos_row_4))
                .check(matches(withText("0")));
    }

    @Test
    public void valueEnteredInSecondFieldShouldAutomaticallyAddZeroForOtherFields(){
        SystemClock.sleep(5000);
        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(0).onChildView(withId(R.id.edit_integer_pos_row_2))
                .perform(typeText("1"))
                .perform(pressImeActionButton());

        onView(withText("Validation")).check(matches(isDisplayed()));
        onView(withText("Yes")).perform(click());

        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(0).onChildView(withId(R.id.edit_integer_pos_row))
                .check(matches(withText("0")));

        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(0).onChildView(withId(R.id.edit_integer_pos_row_3))
                .check(matches(withText("0")));

        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(0).onChildView(withId(R.id.edit_integer_pos_row_4))
                .check(matches(withText("0")));
    }

    @Test
    public void valueEnteredInThirdFieldShouldAutomaticallyAddZeroForOtherFields(){
        SystemClock.sleep(5000);
        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(0).onChildView(withId(R.id.edit_integer_pos_row_3))
                .perform(typeText("1"))
                .perform(pressImeActionButton());

        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(0).onChildView(withId(R.id.edit_integer_pos_row))
                .check(matches(withText("0")));

        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(0).onChildView(withId(R.id.edit_integer_pos_row_2))
                .check(matches(withText("0")));

        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(0).onChildView(withId(R.id.edit_integer_pos_row_4))
                .check(matches(withText("0")));
    }

    @Test
    public void valueEnteredInFourthFieldShouldAutomaticallyAddZeroForOtherFields(){
        SystemClock.sleep(5000);
        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(0).onChildView(withId(R.id.edit_integer_pos_row_4))
                .perform(typeText("1"))
                .perform(pressImeActionButton());

        onView(withText("Validation")).check(matches(isDisplayed()));
        onView(withText("Yes")).perform(click());

        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(0).onChildView(withId(R.id.edit_integer_pos_row))
                .check(matches(withText("0")));

        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(0).onChildView(withId(R.id.edit_integer_pos_row_2))
                .check(matches(withText("0")));

        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(0).onChildView(withId(R.id.edit_integer_pos_row_3))
                .check(matches(withText("0")));
    }

    @Test
    public void shouldNotAddZeroUntilValueIsEntered(){
        SystemClock.sleep(5000);
        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(0).onChildView(withId(R.id.edit_integer_pos_row))
                .perform(click())
                .perform(pressImeActionButton())
                .perform(pressImeActionButton())
                .perform(pressImeActionButton());


        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(0).onChildView(withId(R.id.edit_integer_pos_row))
                .check(matches(withText("")));
        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(0).onChildView(withId(R.id.edit_integer_pos_row_2))
                .check(matches(withText("")));
        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(0).onChildView(withId(R.id.edit_integer_pos_row_3))
                .check(matches(withText("")));
        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(0).onChildView(withId(R.id.edit_integer_pos_row_4))
                .check(matches(withText("")));




    }

    /**
        Disabled fields tests
     **/

    @Test
    public void checkDisabledFieldsForNeonatalTetanus(){
        SystemClock.sleep(5000);

        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(7).onChildView(withId(R.id.edit_integer_pos_row_3))
                .check(matches(not(isEnabled())));

        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(7).onChildView(withId(R.id.edit_integer_pos_row_4))
                .check(matches(not(isEnabled())));

    }
    @Test
    public void checkDisabledFieldsForDiarrhoea(){
        SystemClock.sleep(5000);

        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(8).onChildView(withId(R.id.edit_integer_pos_row_3))
                .check(matches(not(isEnabled())));

        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(8).onChildView(withId(R.id.edit_integer_pos_row_4))
                .check(matches(not(isEnabled())));

    }

    @Test
    public void checkDisabledFieldsForSevereMalnutrition(){
        SystemClock.sleep(5000);

        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(9).onChildView(withId(R.id.edit_integer_pos_row_3))
                .check(matches(not(isEnabled())));

        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(9).onChildView(withId(R.id.edit_integer_pos_row_4))
                .check(matches(not(isEnabled())));

    }

    @Test
    public void checkDisabledFieldsForMaternalDeaths(){
        SystemClock.sleep(5000);

        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(10).onChildView(withId(R.id.edit_integer_pos_row))
                .check(matches(not(isEnabled())));

        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(10).onChildView(withId(R.id.edit_integer_pos_row_2))
                .check(matches(not(isEnabled())));

        onData(anything()).inAdapterView(withId(R.id.list_of_fields)).atPosition(10).onChildView(withId(R.id.edit_integer_pos_row_3))
                .check(matches(not(isEnabled())));
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
        PrefUtils.setUrl(context, urlSetByUser);
    }
}