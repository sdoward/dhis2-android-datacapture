package org.dhis2.mobile.utils;

import android.content.Context;

import org.dhis2.mobile.io.models.Field;
import org.dhis2.mobile.io.models.eidsr.Disease;

import java.util.Map;

/**
 * Created by george on 9/1/16.
 */

/**
 * Checks whether a disease is under the critical disease category.
 */
public class IsCritical {
    private final Map diseases;

    public IsCritical(Context context){
        this.diseases = DiseaseImporter.importDiseases(context);
    }

    public Boolean check(Field field){
        Boolean isCritical = false;

        assert diseases != null;
        if(diseases.get(field.getDataElement()) != null) {
            Disease disease = (Disease) diseases.get(field.getDataElement());

            if (disease.isCritical()) {
                isCritical = true;
            }
        }

        return isCritical;
    }
}
