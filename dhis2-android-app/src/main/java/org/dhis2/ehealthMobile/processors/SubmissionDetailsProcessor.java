package org.dhis2.ehealthMobile.processors;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.JsonObject;

import org.dhis2.ehealthMobile.io.holders.DatasetInfoHolder;
import org.dhis2.ehealthMobile.io.json.JsonHandler;
import org.dhis2.ehealthMobile.io.json.ParsingException;
import org.dhis2.ehealthMobile.network.IHttpClient;
import org.dhis2.ehealthMobile.network.Response;
import org.dhis2.ehealthMobile.ui.activities.DataEntryActivity;

/**
 * Created by george on 10/14/16.
 */

public class SubmissionDetailsProcessor {
    public static String SUBMISSION_DETAILS = "submissionDetails";

    public static void download(IHttpClient httpClient, Context context, DatasetInfoHolder info){

        Response response = httpClient.getSubmissionDetails(info.getFormId(), info.getOrgUnitId(), info.getPeriod());

        if (response.getCode() >= 200 && response.getCode() < 300) {
            String completionDate = getCompletionDate(response.getBody());
            Intent intent  = new Intent(DataEntryActivity.TAG);
            intent.putExtra(Response.CODE, response.getCode());
            intent.putExtra(SUBMISSION_DETAILS, completionDate);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }
    }

    private static String getCompletionDate(String responseBody){
        String completeDate = "completeDate";

        if (responseBody != null && responseBody.contains(completeDate)) {
            try {
                JsonObject jsonForm = JsonHandler.buildJsonObject(responseBody);
                return jsonForm.get(completeDate).getAsString();
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (ParsingException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

}
