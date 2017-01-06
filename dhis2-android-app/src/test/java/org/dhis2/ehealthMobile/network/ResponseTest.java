package org.dhis2.ehealthMobile.network;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by george on 1/4/17.
 */
public class ResponseTest {
    private Response response;
    private int responseCode = 200;
    private String responseBody = "Success";

    @Before
    public void setUp() throws Exception {
        response = new Response(responseCode, responseBody);
    }

    @Test
    public void getCode() throws Exception {
        assertEquals(responseCode, response.getCode());
    }

    @Test
    public void getBody() throws Exception {
        assertEquals(responseBody, response.getBody());
    }

}