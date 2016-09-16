import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import org.dhis2.mobile.processors.FormsDownloadProcessor;
import org.dhis2.mobile.utils.PrefUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by george on 9/14/16.
 */
public class FormsDownloadProcessor404Test {
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
    public void shouldNotUpdateDatasets(){

        MockResponse response = new MockResponse();
        response.setResponseCode(404);
        response.setBody("{\"message\": \"error\"}");
        response.addHeader("Content-Type", "application/json");

        server.enqueue(response);

        HttpUrl url = server.url("");

        SharedPreferences.Editor userData = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE).edit();
        userData.putString(URL, url.toString());
        userData.commit();

        FormsDownloadProcessor.updateDatasets(context);
        assertThat(PrefUtils.getResourceState(context, PrefUtils.Resources.DATASETS), is(PrefUtils.State.ATTEMPT_TO_REFRESH_IS_MADE));

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
