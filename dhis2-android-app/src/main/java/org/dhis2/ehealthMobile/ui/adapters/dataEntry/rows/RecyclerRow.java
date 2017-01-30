package org.dhis2.ehealthMobile.ui.adapters.dataEntry.rows;

import android.content.Context;

import org.dhis2.ehealthMobile.io.models.Field;

/**
 * Created by george on 1/28/17.
 */

public interface RecyclerRow {
    void setData(Field field, Context context);
}
