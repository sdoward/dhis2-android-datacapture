package org.dhis2.mobile.utils;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import org.dhis2.mobile.io.models.Field;
import org.dhis2.mobile.io.models.eidsr.Disease;

import java.util.Map;

/**
 * Created by george on 9/27/16.
 */

public class IsAdditionalDisease {

    public static Boolean check(String id, Context context){
        Boolean isAdditional = false;
        Map diseases = DiseaseImporter.importDiseases(context);
        assert diseases != null;
        Disease disease = (Disease) diseases.get(id);
        if(disease != null && disease.isAdditionalDisease()){
            isAdditional = true;
        }
        return isAdditional;
    }
}
