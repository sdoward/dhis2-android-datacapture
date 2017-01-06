package suite;

import org.dhis2.ehealthMobile.io.models.CategoryComboTest;
import org.dhis2.ehealthMobile.io.models.CategoryTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by george on 1/6/17.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({CategoryComboTest.class, CategoryTest.class})
public class IOModelsUnitTestSuite {
}
