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
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import org.dhis2.mobile.io.holders.DatasetInfoHolder;
import org.dhis2.mobile.io.models.Form;
import org.dhis2.mobile.network.Response;
import org.dhis2.mobile.processors.ReportDownloadProcessor;
import org.dhis2.mobile.ui.activities.DataEntryActivity;
import org.dhis2.mobile.utils.PrefUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.HttpURLConnection;

import static org.hamcrest.CoreMatchers.instanceOf;
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
    private MockWebServer server;
    private DatasetInfoHolder datasetInfo;
    private static final String USER_DATA = "USER_DATA";
    private static final String URL = "url";


    @Before
    public void setup(){
        context = InstrumentationRegistry.getTargetContext();
        server = new MockWebServer();
        datasetInfo = new DatasetInfoHolder();
        datasetInfo.setPeriod(DummyDataAndroidTest.PERIOD);
        datasetInfo.setPeriodLabel(DummyDataAndroidTest.PERIOD_LABEL);
        datasetInfo.setOrgUnitId(DummyDataAndroidTest.ORG_UNIT_ID);
        datasetInfo.setOrgUnitLabel(DummyDataAndroidTest.ORG_UNIT_LABEL);
        datasetInfo.setFormId(DummyDataAndroidTest.FORM_ID);
        datasetInfo.setFormLabel(DummyDataAndroidTest.FORM_LABEL);
    }

    @Test
    public void testDownload(){
        MockResponse response = new MockResponse();
        response.addHeader("Content-Type", "application/json");
        response.setResponseCode(200);
        response.setBody(DummyDataAndroidTest.GOOD_REPORTS_DOWNLOAD_RESPONSE);

        server.enqueue(response);

        HttpUrl url = server.url("/");

        PrefUtils.setUrl(context, url.toString());

        ReportDownloadProcessor.download(context, datasetInfo);

        BroadcastReceiver receiver;
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                assertThat(intent.getExtras().getInt(Response.CODE), is(HttpURLConnection.HTTP_OK));
                assertThat(intent.getExtras().getParcelable(Response.BODY), is(not(null)));
                assertThat(intent.getExtras().getParcelable(Response.BODY), instanceOf(Form.class));
                Form form = intent.getExtras().getParcelable(Response.BODY);
                assert form != null;
                //test the contents of the parcelable
                assertThat(form.getLabel(), is(DummyDataAndroidTest.FORM_LABEL));

            }
        };
        LocalBroadcastManager.getInstance(context)
                .registerReceiver(receiver, new IntentFilter(DataEntryActivity.TAG));
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }
}