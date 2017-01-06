package suite;

import org.dhis2.ehealthMobile.processors.FormsDownloadProcessorTest;
import org.dhis2.ehealthMobile.processors.LoginProcessorTest;
import org.dhis2.ehealthMobile.processors.MyProfileProcessorTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by george on 1/6/17.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({FormsDownloadProcessorTest.class, LoginProcessorTest.class, MyProfileProcessorTest.class})
public class ProcessorsUnitTestSuite {}
