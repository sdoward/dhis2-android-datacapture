package org.dhis2.ehealthMobile.ui.adapters.dataEntry.rows;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.dhis2.ehealthMobile.R;
import org.dhis2.ehealthMobile.io.models.Field;

/**
 * Created by george on 1/28/17.
 */

public class InfoRow extends RecyclerView.ViewHolder implements RecyclerRow {
    private TextView title, content;

    public InfoRow(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        content = (TextView) itemView.findViewById(R.id.content);
    }

    @Override
    public void setData(Field field, Context context) {
        String value = (!("").equals(field.getValue()) ? field.getValue() : context.getString(R.string.info_placeholder));
        title.setText(field.getLabel());
        content.setText(value);
    }

}
