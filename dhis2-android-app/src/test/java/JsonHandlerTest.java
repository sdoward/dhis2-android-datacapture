import com.google.gson.JsonElement;

import org.dhis2.mobile.io.json.JsonHandler;
import org.dhis2.mobile.io.json.ParsingException;
import org.dhis2.mobile.network.Response;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.*;

/**
 * Created by george on 9/6/16.
 */
public class JsonHandlerTest {
    private Response responseObject;
    private ParsingException exception;

    @Before
    public void setup(){
        responseObject = new Response(200, DummyDataTest.GOOD_GET_METHOD_FORM_RESPONSE);
        exception = new ParsingException(DummyDataTest.JSON_EXCEPTION_STRING);
    }

    @Test(expected = ParsingException.class)
    public void ShouldNotBuildObjectWhenPassedEmptyString() throws ParsingException {
        JsonHandler.buildJsonObject("");
    }


    @Test
    public void shouldBuildJsonObject() throws ParsingException {
        assertThat(JsonHandler.buildJsonObject(responseObject.getBody()), instanceOf(JsonElement.class));
    }

    @Test
    public void shouldBuildJsonArray() throws ParsingException {
        assertThat(JsonHandler.buildJsonArray(DummyDataTest.GOOD_GET_METHOD_FORM_RESPONSE_ARRAY).get(0).toString(), is(DummyDataTest.GOOD_GET_METHOD_FORM_RESPONSE));
    }
    @Test(expected = ParsingException.class)
    public void shouldNotBuildJsonArray() throws ParsingException {
        //responseObject body is not an array. Should throw an error.
        JsonHandler.buildJsonArray(responseObject.getBody());
    }



}