package org.dhis2.ehealthMobile.ui.adapters.dataEntry.rows;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.dhis2.ehealthMobile.R;


/**
 * Created by george on 10/24/16.
 */

public class LabelRow extends Row {

    public final String label;
    private final Boolean showListPosition;

    public LabelRow(LayoutInflater inflater, String label, Boolean showListPosition){
        super(null, inflater);
        this.label = label;

        this.showListPosition = showListPosition;
    }
    @Override
    public View getView(int position, View convertView) {
        View view;
        TextView labelView;
        TextView labelPositionView;

        if (convertView == null) {
            ViewGroup rowRoot = (ViewGroup) getInflater().inflate(R.layout.listview_row_label_row, null);
            labelView = (TextView) rowRoot.findViewById(R.id.text_label);
            labelPositionView = (TextView) rowRoot.findViewById(R.id.text_label_number);

            rowRoot.setTag(labelView);
            view = rowRoot;
        } else {
            view = convertView;
            labelView = (TextView) view.getTag();
            labelPositionView = (TextView) view.findViewById(R.id.text_label_number);
        }

       labelView.setText(label);
        if(showListPosition) {
            labelPositionView.setText(String.valueOf(position+1));
        }
        //Offset view 30dp from the left.
        view.setX(30);

        return view;
    }

    @Override
    public int getViewType(){
        return FieldType.LABEL.ordinal();
    }

}
