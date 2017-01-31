package org.dhis2.ehealthMobile.utils;

import android.content.Context;

import org.dhis2.ehealthMobile.io.holders.DatasetInfoHolder;
import org.dhis2.ehealthMobile.io.models.eidsr.Disease;

import java.util.Map;

/**
 * Created by george on 9/1/16.
 */

/**
 * Checks whether a disease is under the critical disease category.
 */
public class IsCritical {
    private final Map diseases;

    public IsCritical(Context context, DatasetInfoHolder info){
        this.diseases = DiseaseImporter.importDiseases(context, info.getFormId());
    }

    public Boolean check(String id){
        Boolean isCritical = false;

        assert diseases != null;
        Disease disease = (Disease) diseases.get(id);

        if (disease != null && disease.isCritical()) {
            isCritical = true;
        }

        return isCritical;
    }
}
