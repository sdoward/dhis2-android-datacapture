import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.LocalBroadcastManager;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import org.dhis2.mobile.network.Response;
import org.dhis2.mobile.processors.FormsDownloadProcessor;
import org.dhis2.mobile.ui.activities.DataEntryActivity;
import org.dhis2.mobile.ui.fragments.AggregateReportFragment;
import org.dhis2.mobile.utils.PrefUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;


/**
 * Created by george on 9/6/16.
 */
@RunWith(AndroidJUnit4.class)
public class FormsDownloadProcessorTest {
    private Context context;
    private MockWebServer server;
    private static final String USER_DATA = "USER_DATA";
    private static final String URL = "url";


    @Before
    public void setUp() throws Exception {
        context = InstrumentationRegistry.getTargetContext();
        server = new MockWebServer();

    }

    @Test
    public void testUpdateDatasets() throws Exception {
        MockResponse response = new MockResponse();
        response.addHeader("Content-Type", "application/json");
        response.setBody(DummyDataAndroidTest.ASSIGNED_DATA_SETS_RESPONSE);
        response.setResponseCode(200);

        server.enqueue(response);

        HttpUrl url = server.url("");

        SharedPreferences.Editor userData = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE).edit();
        userData.putString(URL, url.toString());
        userData.commit();


        FormsDownloadProcessor.updateDatasets(context);

        //setEnabled whether preferences got updated
        assertThat(PrefUtils.getResourceState(context, PrefUtils.Resources.DATASETS), is(PrefUtils.State.UP_TO_DATE));

        //test the request path
        RecordedRequest request = server.takeRequest();
        assertThat("/api/me/assignedDataSets", is(request.getPath()));


        BroadcastReceiver onFormsUpdateListener;
        onFormsUpdateListener = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //setEnabled that response code is 200 aand that broadcast was sent
                assertThat(intent.getExtras().getInt(Response.CODE), is(200));
            }
        };
        LocalBroadcastManager.getInstance(context)
                .registerReceiver(onFormsUpdateListener, new IntentFilter(AggregateReportFragment.TAG));

        try {
            server.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @After
    public void teardown() throws IOException {
        server.shutdown();
    }
}