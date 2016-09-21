import android.content.Context;

import org.dhis2.mobile.io.handlers.UserAccountHandler;
import org.dhis2.mobile.io.models.Field;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertThat;

import static org.hamcrest.core.Is.is;

/**
 * Created by george on 9/6/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserAccountHandlerTest {
    private ArrayList<Field> fields;
    private String firstname = "admin";
    private String firstDataElement = "firstName";


    @Mock
    Context mMockContext;


    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        fields = UserAccountHandler.toFields(mMockContext, DummyDataTest.PROFILE_DATA);
    }

    @Test
    public void checkThatUserDataIsNotEmpty(){
        assertThat(fields.isEmpty(), is(false));
    }

    @Test
    public void checkFirstDataElement(){
        assertThat(fields.get(0).getDataElement(), is(firstDataElement));
    }

    @Test
    public void checkFirstName(){
        assertThat(fields.get(0).getValue(), is(firstname));
    }

    @Test
    public void checkProfileFields(){
        assertThat(UserAccountHandler.fromFields(fields), is(DummyDataTest.PROCESSED_PROFILE_DATA));
    }


}
