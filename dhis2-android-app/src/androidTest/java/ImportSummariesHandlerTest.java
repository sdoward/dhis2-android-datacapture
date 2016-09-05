import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import org.dhis2.mobile.io.handlers.ImportSummariesHandler;
import org.dhis2.mobile.network.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by george on 9/2/16.
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class ImportSummariesHandlerTest {
    private Response mBadResponse;
    private Response mGoodResponse;
    private String defaultValue = "Import process was aborted";
    private String description = "Import process completed successfully";

    @Before
    public  void createResponse(){
        mBadResponse = new Response(200, DummyDataTest.badResponse);
        mGoodResponse = new Response(200, DummyDataTest.goodImport);
    }

    @Test
    public void unsuccessfulImportSummary(){
        assertThat(ImportSummariesHandler.isSuccess(mBadResponse.getBody()), is(false));
    }
    @Test
    public void successfulImportSummary(){
        assertThat(ImportSummariesHandler.isSuccess(mGoodResponse.getBody()), is(true));
    }
    @Test
    public void descriptionIsDefault(){
        assertThat(ImportSummariesHandler.getDescription(mBadResponse.getBody(), defaultValue), is(defaultValue));
    }
    @Test
    public void descriptionIsDescription(){
        assertThat(ImportSummariesHandler.getDescription(mGoodResponse.getBody(), defaultValue), is(description));
    }

}
