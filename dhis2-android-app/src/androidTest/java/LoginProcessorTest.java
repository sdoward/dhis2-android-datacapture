import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.MediumTest;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.LocalBroadcastManager;

import org.dhis2.mobile.network.Response;
import org.dhis2.mobile.processors.LoginProcessor;
import org.dhis2.mobile.ui.fragments.AggregateReportFragment;
import org.dhis2.mobile.utils.PrefUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.HttpURLConnection;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by george on 9/7/16.
 */
@RunWith(AndroidJUnit4.class)
@MediumTest
public class LoginProcessorTest {
    private Context instrumentationCtx;

    @Before
    public void setUp() throws Exception {
        instrumentationCtx = InstrumentationRegistry.getTargetContext();
        WifiManager wifiManager = (WifiManager)this.instrumentationCtx.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(true);


    }

    @Test
    public void testShouldNotLoginWithBadUrl() throws Exception {
        LoginProcessor.loginUser(instrumentationCtx, "badUrl","","");
        BroadcastReceiver onFormsUpdateListener;
        onFormsUpdateListener = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                assertThat(intent.getExtras().getInt(Response.CODE), is(HttpURLConnection.HTTP_NOT_FOUND));
            }
        };
        LocalBroadcastManager.getInstance(instrumentationCtx)
                .registerReceiver(onFormsUpdateListener, new IntentFilter(AggregateReportFragment.TAG));

    }
    @Test
    public void testLogin() throws Exception {
        LoginProcessor.loginUser(instrumentationCtx, PrefUtils.getServerURL(instrumentationCtx),PrefUtils.getCredentials(instrumentationCtx)
                ,PrefUtils.getUserName(instrumentationCtx));
        BroadcastReceiver onFormsUpdateListener;
        onFormsUpdateListener = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                assertThat(intent.getExtras().getInt(Response.CODE), is(HttpURLConnection.HTTP_OK));
            }
        };
        LocalBroadcastManager.getInstance(instrumentationCtx)
                .registerReceiver(onFormsUpdateListener, new IntentFilter(AggregateReportFragment.TAG));

    }

    @Test
    public void shouldNotLoginWithInvalidCreds(){
        LoginProcessor.loginUser(instrumentationCtx, PrefUtils.getServerURL(instrumentationCtx)
                ,"",PrefUtils.getUserName(instrumentationCtx));
        BroadcastReceiver receiver;
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                assertThat(intent.getExtras().getInt(Response.CODE), is(HttpURLConnection.HTTP_UNAUTHORIZED));
            }
        };
        LocalBroadcastManager.getInstance(instrumentationCtx)
                .registerReceiver(receiver, new IntentFilter(AggregateReportFragment.TAG));
    }
}