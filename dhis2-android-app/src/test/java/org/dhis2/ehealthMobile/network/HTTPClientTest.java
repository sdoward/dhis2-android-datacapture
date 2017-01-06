package org.dhis2.ehealthMobile.network;

import android.util.Base64;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.net.HttpURLConnection;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Created by george on 1/4/17.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class HTTPClientTest {
    private MockWebServer server;
    private final String successMessage = "{\"message\": \"success\"}\n";
    private final String errorMessage = "{\"message\": \"error\"}\n";
    private String username = "foo";
    private String password = "bar123";
    private String pair = String.format("%s:%s", username, password);
    private final String credentials = Base64.encodeToString(pair.getBytes(), Base64.NO_WRAP);
    private final String data = "The quick brown fox jumped over the lazy dog";
    private final String contentType = "application/json";

    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();
    }

    @Test
    public void shouldMakeSuccessfulGETCall() throws IOException, InterruptedException {
        MockResponse serverResponse = new MockResponse();
        serverResponse.setResponseCode(HttpURLConnection.HTTP_OK);
        serverResponse.setBody(successMessage);
        serverResponse.addHeader("Content-Type", contentType);

        server.enqueue(serverResponse);

        HttpUrl url = server.url("");
        Response httpResponse = HTTPClient.get(url.toString(), credentials);

        assertThat(httpResponse.getBody(), is(successMessage));
        assertNotNull(server.takeRequest().getHeader("Authorization"));
        assertEquals(httpResponse.getCode(), HttpURLConnection.HTTP_OK);
    }

    @Test
    public void GETCallShouldFailWith401() throws IOException, InterruptedException {
        MockResponse serverResponse = new MockResponse();
        serverResponse.setResponseCode(HttpURLConnection.HTTP_UNAUTHORIZED);
        serverResponse.setBody(errorMessage);
        serverResponse.addHeader("Content-Type", contentType);
        serverResponse.addHeader("WWW-Authenticate: Basic realm=\"myRealm\"");

        server.enqueue(serverResponse);

        HttpUrl url = server.url("");
        Response httpResponse = HTTPClient.get(url.toString(), credentials);

        assertThat(httpResponse.getBody(), is(Response.EMPTY_RESPONSE));
        assertNotNull(server.takeRequest().getHeader("Authorization"));
        assertEquals(httpResponse.getCode(), HttpURLConnection.HTTP_UNAUTHORIZED);
    }

    @Test
    public void GETCallShouldFailWith404() throws InterruptedException {
        MockResponse serverResponse = new MockResponse();
        serverResponse.setResponseCode(HttpURLConnection.HTTP_NOT_FOUND);
        serverResponse.setBody(errorMessage);
        serverResponse.addHeader("Content-Type", contentType);

        server.enqueue(serverResponse);

        HttpUrl url = server.url("");
        Response httpResponse = HTTPClient.get(url.toString(), "");

        assertThat(httpResponse.getBody(), is(Response.EMPTY_RESPONSE));
        assertNotNull(server.takeRequest().getHeader("Authorization"));
        assertEquals(HttpURLConnection.HTTP_NOT_FOUND, httpResponse.getCode());
    }

    @Test
    public void shouldMakeSuccessfulPOST() throws InterruptedException {
        MockResponse serverResponse = new MockResponse();
        serverResponse.setResponseCode(HttpURLConnection.HTTP_OK);
        serverResponse.setBody(successMessage);
        serverResponse.addHeader("Content-Type", contentType);

        server.enqueue(serverResponse);

        HttpUrl url = server.url("");
        Response httpResponse = HTTPClient.post(url.toString(), credentials, data);

        RecordedRequest request = server.takeRequest();
        assertThat(httpResponse.getBody(), is(successMessage));
        assertNotNull(request.getHeader("Authorization"));
        assertEquals(httpResponse.getCode(), HttpURLConnection.HTTP_OK);
        assertEquals(request.getBody().readUtf8(), data);
    }

    @Test
    public void POSTShouldFailWith401() throws InterruptedException {
        MockResponse serverResponse = new MockResponse();
        serverResponse.setResponseCode(HttpURLConnection.HTTP_UNAUTHORIZED);
        serverResponse.addHeader("Content-Type", contentType);

        server.enqueue(serverResponse);

        HttpUrl url = server.url("");
        Response httpResponse = HTTPClient.post(url.toString(), credentials, data);

        RecordedRequest request = server.takeRequest();
        assertThat(httpResponse.getBody(), is(Response.EMPTY_RESPONSE));
        assertNotNull(request.getHeader("Authorization"));
        assertEquals(httpResponse.getCode(), HttpURLConnection.HTTP_UNAUTHORIZED);
        assertEquals(request.getBody().readUtf8(), data);
    }

    @After
    public void teardown() throws IOException {
        server.shutdown();
    }

}