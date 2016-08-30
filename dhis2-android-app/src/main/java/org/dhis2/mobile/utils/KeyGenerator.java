package org.dhis2.mobile.utils;

import android.util.Log;
import org.dhis2.mobile.io.models.Field;
import org.dhis2.mobile.io.models.Group;
import java.util.ArrayList;


/**
 * Created by george on 8/26/16.
 * Generates a unique key based off the data element id.
 * Meant to be used in relation with DHIS2 SMS commands
 */
public class KeyGenerator {
    private final String TAG = KeyGenerator.class.getSimpleName();
    private ArrayList<String> ids = new ArrayList<String>();

    public KeyGenerator(){

    }

    /**
     * Creates a message based on form data. Formats the message using <code>generator</code>
     * @param groups ArrayList<Group> Contains the data elements and their values
     * @param reportName String Name of the SMS command as set in DHIS2
     * @return String The formatted message
     */
    public String parse(ArrayList<Group> groups, String reportName){
        String message = "";
        message += reportName+" ";

        for (Group group : groups) {
            for (Field field : group.getFields()) {
                if(!field.getValue().equals("")) {

                    message += generate(field.getDataElement(), field.getCategoryOptionCombo(), 2)+",";
                    message += field.getValue()+",";

                }
            }
        }

        Log.d(TAG, ids.toString());

        return message;
    }

    private String generate(String dataElementId,String categoryId, int stop){

        String generatedId;

        generatedId = dataElementId.substring(0,stop)+categoryId.substring(0,stop);

        if (ids.indexOf(generatedId) == -1){
            ids.add(generatedId);
        }else{
            stop = stop + 1;
            generatedId = generate(dataElementId, categoryId, stop);
        }

        return generatedId;
    }



}
