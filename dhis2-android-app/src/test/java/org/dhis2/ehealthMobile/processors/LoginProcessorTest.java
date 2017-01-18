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

import org.dhis2.ehealthMobile.network.Response;
import org.dhis2.ehealthMobile.ui.activities.LoginActivity;
import org.dhis2.ehealthMobile.utils.PrefUtils;
import org.dhis2.ehealthMobile.utils.TextFileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import java.io.IOException;
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
    private final String successMessage = "{\n" +
            "\t\"id\": \"abc123\",\n" +
            "\t\"username\": \"admin\",\n" +
            "\t\"firstName\": \"admin\",\n" +
            "\t\"surname\": \"admin\",\n" +
            "\t\"email\": \"foobar@sl.ehealthafrica.org\",\n" +
            "\t\"phoneNumber\": \"123456\",\n" +
            "\t\"gender\": \"gender_male\",\n" +
            "\t\"settings\": {\n" +
            "\t\t\"keyDbLocale\": null,\n" +
            "\t\t\"keyMessageSmsNotification\": \"true\",\n" +
            "\t\t\"keyUiLocale\": \"en\",\n" +
            "\t\t\"keyAnalysisDisplayProperty\": \"name\",\n" +
            "\t\t\"keyMessageEmailNotification\": \"true\"\n" +
            "\t}\n" +
            "}";

    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();
    }

    @Test
    public void shouldLoginUser() throws InterruptedException {
        MockResponse serverResponse = new MockResponse();
        serverResponse.setResponseCode(HttpURLConnection.HTTP_OK);
        serverResponse.setBody(successMessage);
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
                        TextFileUtils.FileNames.ACCOUNT_INFO);

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

        LoginProcessor.loginUser(mockedContext, url.toString(), credentials, username);
        assertTrue(isReceiverCalled[0]);

    }

    @Test
    public void shouldFailIfNoContextIsProvided(){
        LoginProcessor.loginUser(null, "", credentials, username);
        assertNull(PrefUtils.getUserName(mockedContext));
        assertNull(PrefUtils.getCredentials(mockedContext));
    }

    @Test
    public void shouldFailIfNoCredentialsAreProvided(){
        LoginProcessor.loginUser(mockedContext, "", null, username);
        assertNull(PrefUtils.getUserName(mockedContext));
        assertNull(PrefUtils.getCredentials(mockedContext));
    }

    @Test
    public void shouldFailIfNoUsernameIsProvided(){
        LoginProcessor.loginUser(mockedContext, "", credentials, null);
        assertNull(PrefUtils.getUserName(mockedContext));
        assertNull(PrefUtils.getCredentials(mockedContext));
    }

    @After
    public void teardown() throws IOException {
        server.shutdown();
    }

}