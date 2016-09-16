package login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.LocalBroadcastManager;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import org.dhis2.mobile.network.Response;
import org.dhis2.mobile.processors.LoginProcessor;
import org.dhis2.mobile.ui.fragments.AggregateReportFragment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.HttpURLConnection;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by george on 9/15/16.
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class LoginProcessor404Test {
    private Context context;
    private MockWebServer server;
    public static final String USERNAME = "Scooby Doo";
    public static final String CREDS = "Scooby Snacks 4 life";

    @Before
    public void setup(){
        context = InstrumentationRegistry.getTargetContext();
        server = new MockWebServer();
    }

    @Test
    public void shouldReturn404(){
        MockResponse response = new MockResponse();
        response.addHeader("Content-Type", "application/json");
        response.setResponseCode(404);
        response.setBody("{\"message\": \"Not found\"}");

        server.enqueue(response);

        HttpUrl url = server.url("/");

        LoginProcessor.loginUser(context, url.toString(), CREDS
                , USERNAME);
        BroadcastReceiver onFormsUpdateListener;
        onFormsUpdateListener = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                assertThat(intent.getExtras().getInt(Response.CODE), is(HttpURLConnection.HTTP_NOT_FOUND));
            }
        };
        LocalBroadcastManager.getInstance(context)
                .registerReceiver(onFormsUpdateListener, new IntentFilter(AggregateReportFragment.TAG));


    }
}
