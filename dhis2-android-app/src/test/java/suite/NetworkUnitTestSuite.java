package suite;

import org.dhis2.ehealthMobile.network.HTTPClientTest;
import org.dhis2.ehealthMobile.network.NetworkExceptionTest;
import org.dhis2.ehealthMobile.network.NetworkUtilsTest;
import org.dhis2.ehealthMobile.network.ResponseTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by george on 1/6/17.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({HTTPClientTest.class, NetworkExceptionTest.class, NetworkUtilsTest.class, ResponseTest.class})
public class NetworkUnitTestSuite {}
