package org.dhis2.ehealthMobile.processors;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.mockwebserver.Dispatcher;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import org.dhis2.ehealthMobile.io.models.Field;
import org.dhis2.ehealthMobile.network.Response;
import org.dhis2.ehealthMobile.ui.fragments.MyProfileFragment;
import org.dhis2.ehealthMobile.utils.PrefUtils;
import org.dhis2.ehealthMobile.utils.TextFileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by george on 1/6/17.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class MyProfileProcessorTest {
    private Context applicationContext = ShadowApplication.getInstance().getApplicationContext();
    private MockWebServer server;
    private ArrayList<Field> fields;
    private HttpUrl url;
    private final String userDataFromFields = "{\"KPMZxGkiqiQ\":\"value\"}";

    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();
        Field field = new Field();
        fields = new ArrayList<>();
        field.setDataElement(DummyData.FIELD_DATA_ELEMENT);
        field.setValue(DummyData.FIELD_VALUE);
        fields.add(field);

        final Dispatcher dispatcher = new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest recordedRequest) throws InterruptedException {
                MockResponse response = new MockResponse();
                response.addHeader("Content-Type", "application/json");
                response.setResponseCode(HttpURLConnection.HTTP_OK);
                //We want to simulate network delay so we can test the refreshing state.
                response.throttleBody(1024, 1, TimeUnit.SECONDS);
                assertThat(PrefUtils.getResourceState(applicationContext, PrefUtils.Resources.PROFILE_DETAILS), is(PrefUtils.State.REFRESHING));

                if(recordedRequest.getMethod().equals("GET")){
                    response.setBody(DummyData.USER_PROFILE);
                }else if(recordedRequest.getMethod().equals("POST")){
                    response.setBody(DummyData.GOOD_IMPORT_RESPONSE);
                }

                if(!recordedRequest.getPath().equals(DummyData.API_USER_ACCOUNT_URL)){
                    return new MockResponse().setResponseCode(404);
                }

                return response;
            }
        };

        server.setDispatcher(dispatcher);
        url = server.url("/");
        PrefUtils.initAppData(applicationContext, "", "", url.toString());
    }

    @Test
    public void shouldUpdateProfileInfo(){
        final boolean[] isReceiverCalled = new boolean[1];
        BroadcastReceiver profileReceiver;
        profileReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String profileInfo = TextFileUtils.readTextFile(context,
                        TextFileUtils.Directory.ROOT,
                        TextFileUtils.FileNames.ACCOUNT_INFO);
                RecordedRequest request;

                try {
                    request = server.takeRequest();
                    assertNotNull(request.getHeader("Authorization"));
                    assertEquals(DummyData.API_USER_ACCOUNT_URL, request.getPath());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                assertThat(intent.getExtras().getInt(Response.CODE), is(HttpURLConnection.HTTP_OK));
                assertThat(PrefUtils.getResourceState(applicationContext, PrefUtils.Resources.PROFILE_DETAILS), is(PrefUtils.State.UP_TO_DATE));
                assertEquals(DummyData.USER_PROFILE, profileInfo.trim());
                isReceiverCalled[0] = true;
            }
        };
        LocalBroadcastManager.getInstance(applicationContext)
                .registerReceiver(profileReceiver, new IntentFilter(MyProfileFragment.ON_UPDATE_FINISHED_LISTENER_TAG));

        MyProfileProcessor.updateProfileInfo(applicationContext);
        assertTrue(true);
    }

    @Test
    public void shouldUploadProfileInfo(){
        final boolean[] isReceiverCalled = new boolean[1];
        BroadcastReceiver profileReceiver;
        profileReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String profileInfo = TextFileUtils.readTextFile(context,
                        TextFileUtils.Directory.ROOT,
                        TextFileUtils.FileNames.ACCOUNT_INFO);
                RecordedRequest request;

                try {
                    request = server.takeRequest();
                    assertNotNull(request.getHeader("Authorization"));
                    assertEquals(DummyData.API_USER_ACCOUNT_URL, request.getPath());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                assertThat(intent.getExtras().getInt(Response.CODE), is(HttpURLConnection.HTTP_OK));
                assertThat(PrefUtils.getResourceState(applicationContext, PrefUtils.Resources.PROFILE_DETAILS), is(PrefUtils.State.UP_TO_DATE));
                assertEquals(userDataFromFields, profileInfo.trim());
                isReceiverCalled[0] = true;
            }
        };

        LocalBroadcastManager.getInstance(applicationContext)
                .registerReceiver(profileReceiver, new IntentFilter(MyProfileFragment.ON_UPLOAD_FINISHED_LISTENER_TAG));

        MyProfileProcessor.uploadProfileInfo(applicationContext, fields);
        assertTrue(isReceiverCalled[0]);
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }

}