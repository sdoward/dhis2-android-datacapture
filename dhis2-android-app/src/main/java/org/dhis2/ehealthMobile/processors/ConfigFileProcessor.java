package org.dhis2.ehealthMobile.processors;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.dhis2.ehealthMobile.network.HTTPClient;
import org.dhis2.ehealthMobile.network.Response;
import org.dhis2.ehealthMobile.network.URLConstants;
import org.dhis2.ehealthMobile.ui.fragments.AggregateReportFragment;
import org.dhis2.ehealthMobile.utils.PrefUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by george on 11/25/16.
 */

public class ConfigFileProcessor {
    public static final String TAG = ConfigFileProcessor.class.getSimpleName();
    public static final String COMPULSORY_DISEASES = "compulsoryDiseases";
    public static final String DISEASE_CONFIGS = "diseaseConfigs";
    public static final String SHOULD_BE_SQUASHED = "shouldBeSquashed";
    private static final String ID = "id";

    public static void download(HTTPClient httpClient, Context context){

        String credentials = PrefUtils.getCredentials(context);

        Response response = httpClient.getConfigFile();

        if (response.getCode() >= 200 && response.getCode() < 300) {
            String compulsoryDiseases, diseaseConfigs;
            try {
                JSONObject obj = new JSONObject(response.getBody());
                Iterator<String> keys = obj.keys();
                while(keys.hasNext()){
                    String key = keys.next();
                    String formId = obj.getJSONObject(key).getString(ID);
                    boolean shouldBeSquashed;

                    shouldBeSquashed = getBoolean(obj,key, SHOULD_BE_SQUASHED);
                    PrefUtils.saveConfigFileShouldBeSquashed(context, formId, shouldBeSquashed);

                    compulsoryDiseases = getString(obj,key, COMPULSORY_DISEASES);
                    PrefUtils.saveConfigFileCompulsoryDiseases(context, formId, compulsoryDiseases);

                    diseaseConfigs = obj.getJSONObject(key).getString(DISEASE_CONFIGS);
                    PrefUtils.saveConfigFileDiseaseConfig(context, formId, diseaseConfigs);

                }
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        Intent intent = new Intent(AggregateReportFragment.TAG);
        intent.putExtra(Response.CODE, response.getCode());
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private static String getString(JSONObject obj, String key, String value){

        try {
            return obj.getJSONObject(key).optString(value, "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    private static boolean getBoolean(JSONObject obj, String key, String value){

        try {
            return obj.getJSONObject(key).optBoolean(value, false);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

}
