import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.MediumTest;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.LocalBroadcastManager;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.mockwebserver.Dispatcher;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import org.dhis2.mobile.io.models.Field;
import org.dhis2.mobile.network.Response;
import org.dhis2.mobile.processors.MyProfileProcessor;
import org.dhis2.mobile.ui.fragments.MyProfileFragment;
import org.dhis2.mobile.utils.PrefUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import login.DummyData;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by george on 9/16/16.
 */
@RunWith(AndroidJUnit4.class)
@MediumTest
public class MyProfileProcessorTest {
    private Context context;
    private MockWebServer server;
    private Field field;
    private ArrayList<Field> fields;
    private static final String USER_DATA = "USER_DATA";
    private static final String URL = "url";



    @Before
    public void setUp() throws Exception {
        context = InstrumentationRegistry.getTargetContext();
        server = new MockWebServer();
        field = new Field();
        fields = new ArrayList<>();
        field.setLabel(DummyDataAndroidTest.FIELD_LABEL);
        field.setDataElement(DummyDataAndroidTest.FIELD_DATA_ELEMENT);
        fields.add(field);

        final Dispatcher dispatcher = new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest recordedRequest) throws InterruptedException {
                if(recordedRequest.getMethod().equals("GET")){
                    MockResponse response = new MockResponse();
                    response.addHeader("Content-Type", "application/json");
                    response.setResponseCode(HttpURLConnection.HTTP_OK);
                    response.setBody(DummyDataAndroidTest.USER_PROFILE);
                    return response;

                }else if(recordedRequest.getMethod().equals("POST")){
                    MockResponse response = new MockResponse();
                    response.addHeader("Content-Type", "application/json");
                    response.setResponseCode(HttpURLConnection.HTTP_OK);
                    response.setBody(DummyDataAndroidTest.GOOD_IMPORT_RESPONSE);
                    return response;
                }
                return null;
            }
        };

        server.setDispatcher(dispatcher);

        HttpUrl url = server.url("/");

        PrefUtils.setUrl(context, url.toString());


    }


    @Test
    public void testUploadProfileInfo() throws Exception {
        MyProfileProcessor.uploadProfileInfo(context, fields);

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                assertThat(intent.getExtras().getInt(Response.CODE), is(HttpURLConnection.HTTP_OK));
                assertThat(PrefUtils.getResourceState(context, PrefUtils.Resources.PROFILE_DETAILS), is(PrefUtils.State.UP_TO_DATE));
            }
        };

        LocalBroadcastManager.getInstance(context)
                .registerReceiver(receiver, new IntentFilter(MyProfileFragment.ON_UPLOAD_FINISHED_LISTENER_TAG));

    }

    @Test
    public void testUpdateProfileInfo() throws Exception {
        MyProfileProcessor.updateProfileInfo(context);
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                assertThat(intent.getExtras().getInt(Response.CODE), is(HttpURLConnection.HTTP_OK));
                assertThat(PrefUtils.getResourceState(context,PrefUtils.Resources.PROFILE_DETAILS), is(PrefUtils.State.UP_TO_DATE));
            }
        };

        LocalBroadcastManager.getInstance(context)
                .registerReceiver(receiver, new IntentFilter(MyProfileFragment.ON_UPDATE_FINISHED_LISTENER_TAG));

    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }

}