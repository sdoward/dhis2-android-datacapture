package org.dhis2.ehealthMobile.ui.adapters.profile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.dhis2.ehealthMobile.R;
import org.dhis2.ehealthMobile.io.models.Field;
import org.dhis2.ehealthMobile.io.models.Group;
import org.dhis2.ehealthMobile.ui.adapters.dataEntry.rows.InfoRow;

import java.util.ArrayList;

/**
 * Created by george on 1/28/17.
 */

public class ProfileAdapter extends RecyclerView.Adapter {
    private final Group group;
    private final Context context;
    private final ArrayList<Field> fields;

    public ProfileAdapter(Group group, Context context) {
        this.fields = group.getFields();
        this.group = group;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_row_info_row, parent, false);

        return new InfoRow(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Field field = fields.get(position);
        InfoRow row = (InfoRow) holder;
        row.setData(field, this.context);
    }

    @Override
    public int getItemCount() {
        return fields.size();
    }

    public Group getGroup(){
        return this.group;
    }

}
