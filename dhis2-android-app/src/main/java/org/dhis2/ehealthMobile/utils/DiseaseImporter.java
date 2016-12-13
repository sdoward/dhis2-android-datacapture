package org.dhis2.ehealthMobile.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.dhis2.ehealthMobile.R;
import org.dhis2.ehealthMobile.io.Constants;
import org.dhis2.ehealthMobile.io.json.JsonHandler;
import org.dhis2.ehealthMobile.io.json.ParsingException;
import org.dhis2.ehealthMobile.io.models.eidsr.Disease;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by george on 9/22/16.
 */

/**
 * Imports the diseases object in the diseases json file
 */
public class DiseaseImporter {
    private static final String ID = "id";
    private static final String IS_CRITICAL_FIELD = "isCritical";
    private static final String DISABLED_FIELDS = "disabledFields";
    private static final String IS_ADDITIONAL_DISEASE = "isAdditionalDisease";
    private static final String GROUP_LABEL = "groupLabel";


    /**
     * Imports the diseases object in the diseases json file and returns it as a map
     * with the disease id as the key and a disease object as the value.
     * @param context Context
     * @return Map A map of diseases.
     */
    public static Map importDiseases(Context context, String formId) {
        String json;
        json = PrefUtils.getDiseaseConfigs(context, formId);

        JsonObject obj;
        try {
            obj = JsonHandler.buildJsonObject(json);
        } catch (ParsingException ex) {
            ex.printStackTrace();
            return null;
        }

        Map mapOfDiseases = new HashMap();

        //Loop through disease object and create a map of diseases using the disease id as the key.
        for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {

            //get array of disabled fields ids using reflection
            ArrayList<String> disabledFieldsIds = new ArrayList<>();
            for (JsonElement test : entry.getValue().getAsJsonObject().get(DISABLED_FIELDS).getAsJsonArray()){
                try {
                    disabledFieldsIds.add(String.valueOf(Constants.class.getDeclaredField(test.getAsString()).get(String.class)));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }

            }
            String group;
            if(entry.getValue().getAsJsonObject().get(GROUP_LABEL) != null){
                group = entry.getValue().getAsJsonObject().get(GROUP_LABEL).getAsString();
            }else{
                group ="";
            }
            //Create a new disease object
            Disease disease =
                    new Disease(
                            entry.getKey(),
                            entry.getValue().getAsJsonObject().get(ID).getAsString(),
                            entry.getValue().getAsJsonObject().get(IS_CRITICAL_FIELD).getAsBoolean(),
                            entry.getValue().getAsJsonObject().get(IS_ADDITIONAL_DISEASE).getAsBoolean(),
                            disabledFieldsIds,
                            group

                    );

            mapOfDiseases.put(disease.getId(), disease);
        }

        return mapOfDiseases;
    }
}
