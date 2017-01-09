package org.dhis2.ehealthMobile.processors;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import org.dhis2.ehealthMobile.io.json.JsonHandler;
import org.dhis2.ehealthMobile.network.Response;
import org.dhis2.ehealthMobile.ui.fragments.AggregateReportFragment;
import org.dhis2.ehealthMobile.utils.PrefUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import java.net.HttpURLConnection;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by george on 1/5/17.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class FormsDownloadProcessorTest {
    private MockWebServer server;

    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();
    }

    @Test
    public void shouldUpdateDatasets() throws InterruptedException {
        final Context applicationContext = ShadowApplication.getInstance().getApplicationContext();
        MockResponse serverResponse = new MockResponse();
        serverResponse.setResponseCode(HttpURLConnection.HTTP_OK);
        serverResponse.setBody(DummyData.ASSIGNED_DATA_SETS_RESPONSE);
        serverResponse.addHeader("Content-Type", "application/json");

        server.enqueue(serverResponse);
        HttpUrl url = server.url("/");

        PrefUtils.initAppData(applicationContext, "", "", url.toString());
        final boolean[] isReceiverCalled = new boolean[1];

        BroadcastReceiver onFormsUpdateListener;
        onFormsUpdateListener = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //setEnabled that response code is 200 and that broadcast was sent
                try {
                    RecordedRequest request = server.takeRequest();
                    assertThat(DummyData.DATASETS_URL, is(request.getPath()));
                    assertNotNull(request.getHeader("Authorization"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                assertThat(intent.getExtras().getInt(Response.CODE), is(200));
                assertThat(intent.getExtras().getInt(JsonHandler.PARSING_STATUS_CODE), is(JsonHandler.PARSING_OK_CODE));
                assertThat(PrefUtils.getResourceState(applicationContext, PrefUtils.Resources.DATASETS), is(PrefUtils.State.UP_TO_DATE));
                isReceiverCalled[0] = true;
            }
        };
        LocalBroadcastManager.getInstance(applicationContext)
                .registerReceiver(onFormsUpdateListener, new IntentFilter(AggregateReportFragment.TAG));

        FormsDownloadProcessor.updateDatasets(applicationContext);
        assertTrue(isReceiverCalled[0]);
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }

}