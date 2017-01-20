package org.dhis2.ehealthMobile.processors;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import org.dhis2.ehealthMobile.network.HTTPClient;
import org.dhis2.ehealthMobile.network.Response;
import org.dhis2.ehealthMobile.ui.activities.LoginActivity;
import org.dhis2.ehealthMobile.utils.PrefUtils;
import org.dhis2.ehealthMobile.utils.TextFileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by george on 1/5/17.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest= Config.NONE)
public class LoginProcessorTest {
    private Context mockedContext = ShadowApplication.getInstance().getApplicationContext();
    private MockWebServer server;
    private final String username = "user";
    private final String credentials = "credentials";

    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();
    }

    @Test
    public void shouldLoginUser() throws InterruptedException {
        MockResponse serverResponse = new MockResponse();
        serverResponse.setResponseCode(HttpURLConnection.HTTP_OK);
        serverResponse.setBody(readRawTextFile("json/login_success.json"));
        serverResponse.addHeader("Content-Type", "application/json");

        server.enqueue(serverResponse);
        final HttpUrl url = server.url("");

        final boolean[] isReceiverCalled = new boolean[1];

        BroadcastReceiver onUserLoginListener;
        onUserLoginListener = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final RecordedRequest request;
                String accountInfo = TextFileUtils.readTextFile(context,
                        TextFileUtils.Directory.ROOT,
                        TextFileUtils.FileNames.ACCOUNT_INFO.toString());

                try {
                    request = server.takeRequest();
                    assertNotNull(request.getHeader("Authorization"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                assertThat(intent.getExtras().getInt(Response.CODE), is(200));
                assertEquals(url.toString(), PrefUtils.getServerURL(mockedContext));
                assertEquals(username, PrefUtils.getUserName(mockedContext));
                assertEquals(credentials, PrefUtils.getCredentials(mockedContext));
                isReceiverCalled[0] = true;
            }
        };
        LocalBroadcastManager.getInstance(mockedContext)
                .registerReceiver(onUserLoginListener, new IntentFilter(LoginActivity.TAG));

        LoginProcessor.loginUser(HTTPClient.getInstance(RuntimeEnvironment.application), mockedContext, url.toString(), credentials, username);
        assertTrue(isReceiverCalled[0]);

    }

	protected String readRawTextFile(String filename) {

		try {
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filename);
			BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
			StringBuilder builder = new StringBuilder();
			String line;
			while ((line = r.readLine()) != null) {
				builder.append(line).append('\n');
			}

			return builder.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

    @Test
    public void shouldFailIfNoContextIsProvided(){
        LoginProcessor.loginUser(HTTPClient.getInstance(RuntimeEnvironment.application), null, "", credentials, username);
        assertNull(PrefUtils.getUserName(mockedContext));
        assertNull(PrefUtils.getCredentials(mockedContext));
    }

    @Test
    public void shouldFailIfNoCredentialsAreProvided(){
        LoginProcessor.loginUser(HTTPClient.getInstance(RuntimeEnvironment.application), mockedContext, "", null, username);
        assertNull(PrefUtils.getUserName(mockedContext));
        assertNull(PrefUtils.getCredentials(mockedContext));
    }

    @Test
    public void shouldFailIfNoUsernameIsProvided(){
        LoginProcessor.loginUser(HTTPClient.getInstance(RuntimeEnvironment.application), mockedContext, "", credentials, null);
        assertNull(PrefUtils.getUserName(mockedContext));
        assertNull(PrefUtils.getCredentials(mockedContext));
    }

    @After
    public void teardown() throws IOException {
        server.shutdown();
    }

}