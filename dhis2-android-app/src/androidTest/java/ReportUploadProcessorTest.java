import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.MediumTest;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import org.dhis2.mobile.io.holders.DatasetInfoHolder;
import org.dhis2.mobile.io.models.Field;
import org.dhis2.mobile.io.models.Group;
import org.dhis2.mobile.network.URLConstants;
import org.dhis2.mobile.processors.ReportUploadProcessor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Calendar;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by george on 9/15/16.
 */
@RunWith(AndroidJUnit4.class)
@MediumTest
public class ReportUploadProcessorTest {
    private final String FIELD_VALUE = "2";
    private Context context;
    private MockWebServer server;
    private Group group;
    private ArrayList<Group> groups;
    private Field field;
    private ArrayList<Field> fields;
    private DatasetInfoHolder datasetInfo;
    private static final String USER_DATA = "USER_DATA";
    private static final String URL = "url";


    @Before
    public void setup(){
        context = InstrumentationRegistry.getTargetContext();
        server = new MockWebServer();
        group = new Group();
        groups = new ArrayList<>();
        field = new Field();
        fields = new ArrayList<>();

        field.setLabel(DummyDataAndroidTest.FIELD_LABEL);
        field.setDataElement(DummyDataAndroidTest.FIELD_DATA_ELEMENT);
        field.setType(DummyDataAndroidTest.FIELD_TYPE);
        field.setValue(FIELD_VALUE);
        fields.add(field);

        group.setLabel(DummyDataAndroidTest.GROUP_LABEL);
        group.setFields(fields);
        groups.add(group);

        datasetInfo = new DatasetInfoHolder();
        datasetInfo.setPeriod(DummyDataAndroidTest.PERIOD);
        datasetInfo.setPeriodLabel(DummyDataAndroidTest.PERIOD_LABEL);
        datasetInfo.setOrgUnitId(DummyDataAndroidTest.ORG_UNIT_ID);
        datasetInfo.setOrgUnitLabel(DummyDataAndroidTest.ORG_UNIT_LABEL);
        datasetInfo.setFormId(DummyDataAndroidTest.FORM_ID);
        datasetInfo.setFormLabel(DummyDataAndroidTest.FORM_LABEL);


    }



    @Test
    public void testUpload() throws Exception {
        MockResponse response = new MockResponse();
        response.addHeader("Content-Type", "application/json");
        response.setResponseCode(200);
        response.setBody(DummyDataAndroidTest.GOOD_IMPORT_RESPONSE);

        server.enqueue(response);

        HttpUrl url = server.url("/");

        SharedPreferences.Editor userData = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE).edit();
        userData.putString(URL, url.toString());
        userData.commit();

        ReportUploadProcessor.upload(context, datasetInfo, groups);

        RecordedRequest request = server.takeRequest();
        assertThat(request.getRequestLine(), is("POST /"+ URLConstants.DATASET_UPLOAD_URL+" HTTP/1.1"));
        assertThat(request.getBody().readUtf8(), is(DummyDataAndroidTest.REPORT_UPLOAD_REQUEST_BODY));




    }

    @After
    public void tearDown() throws Exception {

        server.shutdown();

    }
}