package org.dhis2.mobile.utils;

/**
 * Created by george on 9/1/16.
 */


import android.widget.EditText;

import org.dhis2.mobile.io.Constants;
import org.dhis2.mobile.io.models.Field;

import java.util.Arrays;

/**
 * Utility class that checks whether a field is disabled or not
 */
public class IsDisabled {
    private static final String [] dElementsIds = new String[] {Constants.DIARRHOEA, Constants.SEVERE_MALNUTRTION, Constants.MATERNAL_DEATHS,
            Constants.NEONATAL_TETANUS};

    /**
     * Checks whether a field is disabled in the E-idsr form
     * @param editText EditText
     * @param field Field {@see Field}
     *
     */

    public static void check( final EditText editText, Field field){
        Boolean isEnabled = true;
        if(Arrays.asList(dElementsIds).contains(field.getDataElement())){
            switch (field.getDataElement()){
                case Constants.DIARRHOEA:
                    if(field.getCategoryOptionCombo().equals("jpmVF8rhBNA") || field.getCategoryOptionCombo().equals("Ciavs7v4qYa")){
                        isEnabled = false;
                    }
                    break;
                case Constants.SEVERE_MALNUTRTION:
                    if(field.getCategoryOptionCombo().equals("jpmVF8rhBNA") || field.getCategoryOptionCombo().equals("Ciavs7v4qYa")){
                        isEnabled = false;
                    }
                    break;
                case Constants.MATERNAL_DEATHS:
                    if(field.getCategoryOptionCombo().equals("e079rjYqlMH") || field.getCategoryOptionCombo().equals("jpmVF8rhBNA")
                            || field.getCategoryOptionCombo().equals("C5qLGpw4uet")){
                        isEnabled = false;
                    }
                    break;
                case Constants.NEONATAL_TETANUS:
                    if(field.getCategoryOptionCombo().equals("jpmVF8rhBNA") || field.getCategoryOptionCombo().equals("Ciavs7v4qYa")){
                        isEnabled = false;
                    }
                    break;
                default:
//                    isEnabled = false;
                    break;


            }
        }

        editText.setEnabled(isEnabled);

    }
}
