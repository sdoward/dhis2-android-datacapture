package login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.MediumTest;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.LocalBroadcastManager;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import org.dhis2.mobile.network.Response;
import org.dhis2.mobile.processors.LoginProcessor;
import org.dhis2.mobile.ui.fragments.AggregateReportFragment;
import org.dhis2.mobile.utils.PrefUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.HttpURLConnection;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by george on 9/7/16.
 */
@RunWith(AndroidJUnit4.class)
@MediumTest
public class LoginProcessorTest {
    public static final String USERNAME = "Scooby Doo";
    public static final String CREDS = "Scooby Snacks 4 life";
    private Context context;
    private MockWebServer server;

    @Before
    public void setup(){
        context = InstrumentationRegistry.getTargetContext();
        server = new MockWebServer();
    }

    @Test
    public void shouldLogin() throws InterruptedException {
        MockResponse response = new MockResponse();
        response.addHeader("Content-Type", "application/json");
        response.setResponseCode(200);
        response.setBody(DummyData.GOOD_LOGIN_RESPONSE);


        server.enqueue(response);

        HttpUrl url = server.url("/");

        LoginProcessor.loginUser(context, url.toString(), CREDS
                , USERNAME);
        BroadcastReceiver onFormsUpdateListener;
        onFormsUpdateListener = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                assertThat(intent.getExtras().getInt(Response.CODE), is(HttpURLConnection.HTTP_OK));
            }
        };
        LocalBroadcastManager.getInstance(context)
                .registerReceiver(onFormsUpdateListener, new IntentFilter(AggregateReportFragment.TAG));


    }
    @After
    public void teardown() throws IOException {
        server.shutdown();
    }
}