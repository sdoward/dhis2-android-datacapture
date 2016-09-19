import android.app.Activity;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Gravity;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.mockwebserver.Dispatcher;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import org.dhis2.mobile.R;
import org.dhis2.mobile.network.URLConstants;
import org.dhis2.mobile.ui.activities.MenuActivity;
import org.dhis2.mobile.utils.PrefUtils;
import org.dhis2.mobile.utils.TextFileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.HttpURLConnection;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.NavigationViewActions.navigateTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.not;


/**
 * Created by george on 9/8/16.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MenuActivityTests {
    private Context context;
    private MockWebServer server;
    private String CHOOSE_DATA_SET;
    private String CHOOSE_PERIOD;
    private Activity activity;
    private static final String USER_DATA = "USER_DATA";
    private static final String URL = "url";
    private String urlSetByUser;


    @Rule
    public ActivityTestRule<MenuActivity> mActivityRule = new ActivityTestRule(MenuActivity.class);

    @Before
    public void setup(){
        context = InstrumentationRegistry.getTargetContext();
        activity = mActivityRule.getActivity();
        CHOOSE_DATA_SET =  activity.getString(R.string.choose_data_set);
        CHOOSE_PERIOD = activity.getString(R.string.choose_period);

        server = new MockWebServer();


        final Dispatcher dispatcher = new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest recordedRequest) throws InterruptedException {
                if(recordedRequest.getPath().equals(URLConstants.API_USER_ACCOUNT_URL)) {
                    return new MockResponse().addHeader("Content-Type", "application/json")
                            .setResponseCode(HttpURLConnection.HTTP_OK)
                            .setBody(DummyDataAndroidTest.USER_PROFILE);
                }else if(recordedRequest.getPath().equals(URLConstants.DATASETS_URL)){
                    return new MockResponse().addHeader("Content-Type", "application/json")
                            .setResponseCode(HttpURLConnection.HTTP_OK)
                            .setBody(DummyDataAndroidTest.ASSIGNED_DATA_SETS_RESPONSE);
                }else if(recordedRequest.getPath().equals(URLConstants.DATASET_VALUES_URL+"/"+DummyDataAndroidTest.REPORT_FORM_ID+"/"
                        + URLConstants.FORM_PARAM + DummyDataAndroidTest.REPORT_ORG_ID
                        + URLConstants.PERIOD_PARAM + DummyDataAndroidTest.PERIOD)){
                    return new MockResponse().addHeader("Content-Type", "application/json")
                            .setResponseCode(HttpURLConnection.HTTP_OK)
                            .setBody(DummyDataAndroidTest.GOOD_REPORTS_DOWNLOAD_RESPONSE);
                }
                return null;
            }
        };

        server.setDispatcher(dispatcher);

        HttpUrl url = server.url("/");

        urlSetByUser = PrefUtils.getServerURL(context);

        PrefUtils.setUrl(context, url.toString());
        TextFileUtils.writeTextFile(context, TextFileUtils.Directory.ROOT,
                TextFileUtils.FileNames.ACCOUNT_INFO, DummyDataAndroidTest.USER_PROFILE);

    }

    @Test
    public void checkIfMenuIsDisplayed(){
        onView(withId(R.id.recyclerview_pickers_one)).check(matches(isDisplayed()));
    }

    @Test
    public void shouldOpenNavigation(){
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.START))).perform(open());
    }
    @Test
    public void checkIfLetterAvatarIsDisplayed(){
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.START))).perform(open());
        onView(withId(R.id.side_nav_photo)).check(matches(isDisplayed()));
    }
    @Test
    public void checkIfUsernameIsDisplayed(){
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.START))).perform(open());
        onView(withId(R.id.side_nav_username)).check(matches(isDisplayed()));
    }
    @Test
    public void checkIfEmailIsDisplayed(){
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.START))).perform(open());
        onView(withId(R.id.side_nav_email)).check(matches(isDisplayed()));
    }

    @Test
    public void shouldOpenProfileFragment(){
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.START))).perform(open());
        onView(withId(R.id.navigation_view)).perform(navigateTo(R.id.drawer_item_profile));
        onView(withId(R.id.list_of_fields)).check(matches(isDisplayed()));
    }
    @Test
    public void shouldShowChooseDataset(){
        onView(withId(R.id.user_data_entry)).check(matches(not(isDisplayed())));
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.START))).perform(open());
        onView(withId(R.id.navigation_view)).perform(navigateTo(R.id.drawer_item_aggregate_report));
        onView(withText(CHOOSE_DATA_SET
        )).perform(click());
        onData(anything()).inAdapterView(withId(R.id.listview_picker_items)).atPosition(0).perform(click());

    }
    @Test
    public void shouldMakePeriodPickerVisible(){
        onView(withId(R.id.user_data_entry)).check(matches(not(isDisplayed())));
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.START))).perform(open());
        onView(withId(R.id.navigation_view)).perform(navigateTo(R.id.drawer_item_aggregate_report));
        onView(withText(CHOOSE_DATA_SET)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.listview_picker_items)).atPosition(0).perform(click());
        onView(withText(CHOOSE_PERIOD)).check(matches(isDisplayed()));

    }

    @Test
    public void shouldChooseAPeriod(){
        onView(withId(R.id.user_data_entry)).check(matches(not(isDisplayed())));
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.START))).perform(open());
        onView(withId(R.id.navigation_view)).perform(navigateTo(R.id.drawer_item_aggregate_report));
        onView(withText(CHOOSE_DATA_SET)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.listview_picker_items)).atPosition(0).perform(click());
        onView(withText(CHOOSE_PERIOD)).check(matches(isDisplayed()));
        onView(withText(CHOOSE_PERIOD)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.dates_listview)).atPosition(0).perform(click());

    }

    @Test
    public void shouldMakeDataEntryButtonVisible(){
        onView(withId(R.id.user_data_entry)).check(matches(not(isDisplayed())));
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.START))).perform(open());
        onView(withId(R.id.navigation_view)).perform(navigateTo(R.id.drawer_item_aggregate_report));
        onView(withText(CHOOSE_DATA_SET)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.listview_picker_items)).atPosition(0).perform(click());
        onView(withText(CHOOSE_PERIOD)).check(matches(isDisplayed()));
        onView(withText(CHOOSE_PERIOD)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.dates_listview)).atPosition(0).perform(click());
        onView(withId(R.id.user_data_entry)).check(matches((isDisplayed())));
    }

    @Test
    public void shouldOpenValidDataEntryActivity(){
        onView(withId(R.id.user_data_entry)).check(matches(not(isDisplayed())));
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.START))).perform(open());
        onView(withId(R.id.navigation_view)).perform(navigateTo(R.id.drawer_item_aggregate_report));
        onView(withText(CHOOSE_DATA_SET)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.listview_picker_items)).atPosition(0).perform(click());
        onView(withText(CHOOSE_PERIOD)).check(matches(isDisplayed()));
        onView(withText(CHOOSE_PERIOD)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.dates_listview)).atPosition(0).perform(click());
        onView(withId(R.id.user_data_entry)).check(matches((isDisplayed())));
        onView(withId(R.id.user_data_entry)).perform(click());
        onView(withId(R.id.data_entry_frame)).check(matches(isDisplayed()));


    }

    @Test
    public void shouldRemoveDatasetPicker(){
        onView(withId(R.id.recyclerview_pickers_one))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,MyViewAction.clickChildViewWithId(R.id.imageview_cancel)));

    }

    @Test
    public void shouldRemovePeriodPicker(){
        onView(withId(R.id.recyclerview_pickers_one)).check(matches(isDisplayed()));
        onView(withId(R.id.recyclerview_pickers_one)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onData(anything()).inAdapterView(withId(R.id.listview_picker_items)).atPosition(0).perform(click());
        onView(withText(CHOOSE_PERIOD)).check(matches(isDisplayed()));
        onView(withId(R.id.recyclerview_pickers_one))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1,MyViewAction.clickChildViewWithId(R.id.imageview_cancel)));
    }

    @After
    public void teardown() throws IOException {
        server.shutdown();
        PrefUtils.setUrl(context, urlSetByUser);
    }




}