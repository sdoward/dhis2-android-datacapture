import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.LocalBroadcastManager;

import org.dhis2.mobile.network.Response;
import org.dhis2.mobile.processors.FormsDownloadProcessor;
import org.dhis2.mobile.ui.activities.DataEntryActivity;
import org.dhis2.mobile.ui.fragments.AggregateReportFragment;
import org.dhis2.mobile.utils.PrefUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;


/**
 * Created by george on 9/6/16.
 */
@RunWith(AndroidJUnit4.class)
public class FormsDownloadProcessorTest {
    private Context instrumentationCtx;




    @Before
    public void setUp() throws Exception {
        instrumentationCtx = InstrumentationRegistry.getTargetContext();
        WifiManager wifiManager = (WifiManager)this.instrumentationCtx.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(true);
    }




    @Test
    public void testUpdateOfDatasets() throws Exception {
        FormsDownloadProcessor.updateDatasets(instrumentationCtx);
        assertThat(PrefUtils.getResourceState(instrumentationCtx, PrefUtils.Resources.DATASETS), is(PrefUtils.State.UP_TO_DATE));
    }

    @Test
    public void testIntentBroadcast(){
        BroadcastReceiver onFormsUpdateListener;
        onFormsUpdateListener = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                assertThat(intent.getExtras().getInt(Response.CODE), is(200));
            }
        };
        LocalBroadcastManager.getInstance(instrumentationCtx)
                .registerReceiver(onFormsUpdateListener, new IntentFilter(AggregateReportFragment.TAG));

    }



}