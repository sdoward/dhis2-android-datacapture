package org.dhis2.ehealthMobile.processors;

import android.content.Context;
import android.util.Log;

import org.dhis2.ehealthMobile.network.IHttpClient;
import org.dhis2.ehealthMobile.network.Response;
import org.dhis2.ehealthMobile.utils.PrefUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jasper on 10/31/16.
 */

public class SMSNumberProcessor {
    public static final String TAG = SMSNumberProcessor.class.getSimpleName();

    public static String SMS_NUMBER = "smsNumber";

    public static void download(IHttpClient httpClient, Context context){

        Response response = httpClient.getSmsNumber();

        if (response.getCode() >= 200 && response.getCode() < 300) {
            String smsNumber;
            try {
                JSONObject obj = new JSONObject(response.getBody());
                smsNumber = obj.getString(SMS_NUMBER);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
                return;
            }

            PrefUtils.saveSmsNumber(context, smsNumber);
        }
    }
}
