package org.dhis2.ehealthMobile.network;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by george on 1/4/17.
 */
public class NetworkExceptionTest {
    private int errorCode = 123;

    @Test (expected = NetworkException.class)
    public void testException() throws NetworkException {
        throw new NetworkException(errorCode);
    }

    @Test
    public void testExceptionMessage(){
        try{
            throw new NetworkException(errorCode);
        }catch (NetworkException networkException){
            assertEquals(networkException.getErrorCode(), errorCode);
        }
    }

}