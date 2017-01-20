package org.dhis2.ehealthMobile;

import android.content.Context;

import com.squareup.okhttp.mockwebserver.MockWebServer;

import org.dhis2.ehealthMobile.network.HTTPClient;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public abstract class BaseRoboElectricTest {

	private MockWebServer server;

	@Before
	public void setup(){
		server = new MockWebServer();
		HTTPClient.getInstance(RuntimeEnvironment.application).setBaseUrl(server.url("/").toString());
	}

	@After
	public void tearDown() throws Exception {
		server.shutdown();
	}

	protected MockWebServer getServer(){
		return server;
	}

	protected Context getContext(){
		return ShadowApplication.getInstance().getApplicationContext();
	}
}
