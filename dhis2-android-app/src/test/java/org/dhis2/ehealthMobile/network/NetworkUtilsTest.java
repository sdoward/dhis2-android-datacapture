package org.dhis2.ehealthMobile.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowConnectivityManager;
import org.robolectric.shadows.ShadowNetworkInfo;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Created by george on 1/4/17.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class NetworkUtilsTest {
    private Context context;
    private ShadowConnectivityManager shadowConnectivityManager;

    @Before
    public void setUp(){
        context = RuntimeEnvironment.application.getApplicationContext();
        ConnectivityManager connectivityManager = getConnectivityManager();
        shadowConnectivityManager = Shadows.shadowOf(connectivityManager);
    }

    @Test
    public void shouldHaveAConnection() {
        NetworkInfo networkInfo =  ShadowNetworkInfo.newInstance(NetworkInfo.DetailedState.CONNECTED, ConnectivityManager.TYPE_MOBILE, 0, true, true);
        shadowConnectivityManager.setActiveNetworkInfo(networkInfo);
        assertTrue(NetworkUtils.checkConnection(context));
    }

    @Test
    public void shouldNotHaveAConnection(){
        NetworkInfo networkInfo =  ShadowNetworkInfo.newInstance(NetworkInfo.DetailedState.DISCONNECTED, ConnectivityManager.TYPE_MOBILE, 0, false, false);
        shadowConnectivityManager.setActiveNetworkInfo(networkInfo);
        assertFalse(NetworkUtils.checkConnection(context));
    }

    @Test
    public void testNetworkInfo(){
        NetworkInfo networkInfo =  ShadowNetworkInfo.newInstance(NetworkInfo.DetailedState.CONNECTED, ConnectivityManager.TYPE_WIFI, 0, true, true);
        shadowConnectivityManager.setActiveNetworkInfo(networkInfo);
        assertThat(NetworkUtils.getNetworkInfo(context), is(instanceOf(NetworkInfo.class)));

        NetworkInfo activeNetworkInfo = NetworkUtils.getNetworkInfo(context);
        assertTrue(activeNetworkInfo.isConnected());
        assertTrue(activeNetworkInfo.isAvailable());
    }

    @Test
    public void shouldHaveAWifiConnection(){
        NetworkInfo networkInfo =  ShadowNetworkInfo.newInstance(NetworkInfo.DetailedState.CONNECTED, ConnectivityManager.TYPE_WIFI, 0, true, true);
        shadowConnectivityManager.setActiveNetworkInfo(networkInfo);
        assertTrue(NetworkUtils.isConnectedWifi(context));
    }

    @Test
    public void shouldNotHaveAWifiConnection(){
        NetworkInfo networkInfo =  ShadowNetworkInfo.newInstance(NetworkInfo.DetailedState.DISCONNECTED, ConnectivityManager.TYPE_WIFI, 0, false, false);
        shadowConnectivityManager.setActiveNetworkInfo(networkInfo);
        assertFalse(NetworkUtils.isConnectedWifi(context));
    }

    private ConnectivityManager getConnectivityManager() {
        return (ConnectivityManager) RuntimeEnvironment.application.getSystemService(Context.CONNECTIVITY_SERVICE);
    }
}