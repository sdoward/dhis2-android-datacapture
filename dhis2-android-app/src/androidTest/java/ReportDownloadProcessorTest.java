import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.MediumTest;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.LocalBroadcastManager;

import org.dhis2.mobile.io.holders.DatasetInfoHolder;
import org.dhis2.mobile.network.Response;
import org.dhis2.mobile.processors.ReportDownloadProcessor;
import org.dhis2.mobile.processors.ReportUploadProcessor;
import org.dhis2.mobile.ui.activities.DataEntryActivity;
import org.dhis2.mobile.ui.fragments.AggregateReportFragment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.HttpURLConnection;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;

/**
 * Created by george on 9/8/16.
 */
@RunWith(AndroidJUnit4.class)
@MediumTest
public class ReportDownloadProcessorTest {
    private Context context;
    private DatasetInfoHolder datasetInfo;


    @Before
    public void setUp() throws Exception {
        context = InstrumentationRegistry.getTargetContext();
        WifiManager wifiManager = (WifiManager) this.context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(true);
        datasetInfo = new DatasetInfoHolder();
        datasetInfo.setPeriod(DummyDataAndroidTest.PERIOD);
        datasetInfo.setPeriodLabel(DummyDataAndroidTest.PERIOD_LABEL);
        datasetInfo.setOrgUnitId(DummyDataAndroidTest.ORG_UNIT_ID);
        datasetInfo.setOrgUnitLabel(DummyDataAndroidTest.ORG_UNIT_LABEL);
        datasetInfo.setFormId(DummyDataAndroidTest.FORM_ID);
        datasetInfo.setFormLabel(DummyDataAndroidTest.FORM_LABEL);

    }

    @Test
    public void testDownload() throws Exception {
        ReportDownloadProcessor.download(context, datasetInfo);
        BroadcastReceiver receiver;
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                assertThat(intent.getExtras().getInt(Response.CODE), is(HttpURLConnection.HTTP_OK));
                assertThat(intent.getExtras().getParcelable(Response.BODY), is(not(null)));

            }
        };
        LocalBroadcastManager.getInstance(context)
                .registerReceiver(receiver, new IntentFilter(DataEntryActivity.TAG));

    }
}