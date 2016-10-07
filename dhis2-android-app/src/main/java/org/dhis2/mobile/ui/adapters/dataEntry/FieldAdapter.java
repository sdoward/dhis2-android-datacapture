/*
 * Copyright (c) 2014, Araz Abishov
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package org.dhis2.mobile.ui.adapters.dataEntry;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.dhis2.mobile.io.Constants;
import org.dhis2.mobile.io.json.JsonHandler;
import org.dhis2.mobile.io.json.ParsingException;
import org.dhis2.mobile.io.models.Field;
import org.dhis2.mobile.io.models.Group;
import org.dhis2.mobile.io.models.OptionSet;
import org.dhis2.mobile.io.models.eidsr.Disease;
import org.dhis2.mobile.ui.adapters.dataEntry.rows.GenderRow;
import org.dhis2.mobile.ui.adapters.dataEntry.rows.NegativeIntegerRow;
import org.dhis2.mobile.ui.adapters.dataEntry.rows.PosIntegerRow;
import org.dhis2.mobile.ui.adapters.dataEntry.rows.PosOrZeroIntegerRow2;
import org.dhis2.mobile.ui.adapters.dataEntry.rows.Row;
import org.dhis2.mobile.ui.adapters.dataEntry.rows.RowTypes;
import org.dhis2.mobile.utils.IsAdditionalDisease;
import org.dhis2.mobile.utils.IsCritical;
import org.dhis2.mobile.utils.TextFileUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class FieldAdapter extends BaseAdapter {
    private ArrayList<Row> rows;
    private final String adapterLabel;
    private final Group group;
    private LayoutInflater inflater;
    private ArrayList<Field> groupedFields;
    private IsCritical isCriticalDisease;

    public FieldAdapter(Group group, Context context) {
        ArrayList<Field> fields = group.getFields();
        Collections.sort(fields, Field.COMPARATOR);
        groupedFields = new ArrayList<Field>();
        String previousFieldId = "";
        this.group = group;
        this.rows = new ArrayList<Row>();
        this.adapterLabel = group.getLabel();
        inflater = LayoutInflater.from(context);
        isCriticalDisease = new IsCritical(context);
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            if (field.hasOptionSet()) {
                OptionSet optionSet = getOptionSet(context, field.getOptionSet());
//                rows.add(new AutoCompleteRow(inflater, field, optionSet, context));
            } else if (field.getType().equals(RowTypes.TEXT.name())) {
//                rows.add(new TextRow(inflater, field));
            } else if (field.getType().equals(RowTypes.LONG_TEXT.name())) {
//                rows.add(new LongTextRow(inflater, field));
            } else if (field.getType().equals(RowTypes.NUMBER.name())) {
//                rows.add(new NumberRow(inflater, field));
            } else if (field.getType().equals(RowTypes.INTEGER.name())) {
//                rows.add(new IntegerRow(inflater, field));
            } else if (field.getType().equals(RowTypes.INTEGER_ZERO_OR_POSITIVE.name())) {
                //Changed from the others to support grouping of Diseases
                //Specific test case for eidsr form
                if(!field.getDataElement().equals(previousFieldId) && groupedFields.size() > 0 && !IsAdditionalDisease.check(previousFieldId, context)){
                    //each disease has four fields.
                    //we create a row from the last for fields added
                    rows.add(new PosOrZeroIntegerRow2(inflater,
                                groupedFields,
                                isCriticalDisease.check(groupedFields.get(groupedFields.size()-1))
                                ));

                }
                groupedFields.add(field);
                previousFieldId = field.getDataElement();


            } else if (field.getType().equals(RowTypes.INTEGER_POSITIVE.name())) {
                rows.add(new PosIntegerRow(inflater, field));
            } else if (field.getType().equals(RowTypes.INTEGER_NEGATIVE.name())) {
                rows.add(new NegativeIntegerRow(inflater, field));
            } else if (field.getType().equals(RowTypes.BOOLEAN.name())) {
//                if(!field.getDataElement().equals(Constants.TIMELY)){
//                    rows.add(new BooleanRow(inflater, field));
//                }
            } else if (field.getType().equals(RowTypes.TRUE_ONLY.name())) {
//                rows.add(new CheckBoxRow(inflater, field));
            } else if (field.getType().equals(RowTypes.DATE.name())) {
//                rows.add(new DatePickerRow(inflater, field, this, context));
            } else if (field.getType().equals(RowTypes.GENDER.name())) {
                rows.add(new GenderRow(inflater, field));
            }
        }

    }

    @Override
    public int getCount() {
        return rows.size();
    }

    @Override
    public int getViewTypeCount() {
        return RowTypes.values().length;
    }

    @Override
    public int getItemViewType(int position) {
        return rows.get(position).getViewType();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        return rows.get(position).getView(convertView);
    }

    public String getLabel() {
        return adapterLabel;
    }

    public Group getGroup() {
        return group;
    }

    private static OptionSet getOptionSet(Context context, String id) {
        String source = TextFileUtils.readTextFile(context, TextFileUtils.Directory.OPTION_SETS, id);
        try {
            JsonObject jOptionSet = JsonHandler.buildJsonObject(source);
            Gson gson = new Gson();
            return gson.fromJson(jOptionSet, OptionSet.class);
        } catch (ParsingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addItem(Disease disease) {

        ArrayList<Field> fields = this.group.getFields();
        ArrayList<Field> additionalDiseaseFields = new ArrayList<>();

        for(Field field: fields){
            if(field.getDataElement().equals(disease.getId())){
                additionalDiseaseFields.add(field);
            }
        }

        this.rows.add(new PosOrZeroIntegerRow2(inflater, additionalDiseaseFields, false));
        notifyDataSetChanged();
    }

    public void removeItemAtPosition(int position){
        this.rows.remove(position);
        notifyDataSetChanged();
    }






}
