import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.google.gson.Gson;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import org.dhis2.mobile.io.holders.DatasetInfoHolder;
import org.dhis2.mobile.network.URLConstants;
import org.dhis2.mobile.processors.OfflineDataProcessor;
import org.dhis2.mobile.utils.PrefUtils;
import org.dhis2.mobile.utils.TextFileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by george on 9/15/16.
 */
public class OfflineDataProcessorTest {
    private Context context;
    private MockWebServer server;
    private DatasetInfoHolder datasetInfo;
    private String key;
    private final String data = "The rain in spain stays mainly in the plane";


    @Before
    public void setUp() throws Exception {
        context = InstrumentationRegistry.getTargetContext();
        server = new MockWebServer();

        datasetInfo = new DatasetInfoHolder();
        datasetInfo.setPeriod(DummyDataAndroidTest.PERIOD);
        datasetInfo.setPeriodLabel(DummyDataAndroidTest.PERIOD_LABEL);
        datasetInfo.setOrgUnitId(DummyDataAndroidTest.ORG_UNIT_ID);
        datasetInfo.setOrgUnitLabel(DummyDataAndroidTest.ORG_UNIT_LABEL);
        datasetInfo.setFormId(DummyDataAndroidTest.FORM_ID);
        datasetInfo.setFormLabel(DummyDataAndroidTest.FORM_LABEL);

        key = DatasetInfoHolder.buildKey(datasetInfo);

    }

    @Test
    public void testUpload() throws Exception {
        MockResponse reportsResponse = new MockResponse();
        reportsResponse.addHeader("Content-Type", "application/json");
        reportsResponse.setResponseCode(200);
        reportsResponse.setBody(DummyDataAndroidTest.GOOD_IMPORT_RESPONSE);





        server.enqueue(reportsResponse);

        server.start();

        HttpUrl url = server.url("/");

        PrefUtils.setUrl(context, url.toString());

        //write dummy data for upload
        TextFileUtils.writeTextFile(context, TextFileUtils.Directory.OFFLINE_DATASETS, key, data);

        Gson gson = new Gson();
        String jsonReportInfo = gson.toJson(datasetInfo);
        PrefUtils.saveOfflineReportInfo(context, key, jsonReportInfo);

        OfflineDataProcessor.upload(context);

        RecordedRequest reportRequest = server.takeRequest();
        assertThat(reportRequest.getRequestLine(), is("POST /"+ URLConstants.DATASET_UPLOAD_URL+" HTTP/1.1"));
        assertThat(reportRequest.getBody().readUtf8(), is(data+"\n"));


    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }
}