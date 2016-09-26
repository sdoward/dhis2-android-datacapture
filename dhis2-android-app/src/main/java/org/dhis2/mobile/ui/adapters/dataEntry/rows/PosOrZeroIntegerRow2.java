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

package org.dhis2.mobile.ui.adapters.dataEntry.rows;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.dhis2.mobile.R;
import org.dhis2.mobile.io.models.Field;
import org.dhis2.mobile.utils.IsCritical;
import org.dhis2.mobile.utils.IsDisabled;

import java.util.ArrayList;

public class PosOrZeroIntegerRow2 implements Row {
    public static final String PREFIX = " EIDSR-";
    private final LayoutInflater inflater;
    private final Field field,field2, field3, field4;
    private AlertDialog alertDialog;
    private AlertDialog criticalAlertDialog;
    private final Boolean isCasesField = true;
    private final Boolean isDeathsField = false;


    public PosOrZeroIntegerRow2(LayoutInflater inflater, Field field, Field field2, Field field3, Field field4 ) {
        this.inflater = inflater;
        this.field = field;
        this.field2 = field2;
        this.field3 = field3;
        this.field4 = field4;
    }

    @Override
    public View getView(View convertView) {
        View view;
        final EditTextHolder holder;
        final EditTextHolder holder2;
        final EditTextHolder holder3;
        final EditTextHolder holder4;

        ArrayList<Field> fields = new ArrayList<>();
        fields.add(field);
        fields.add(field2);
        fields.add(field3);
        fields.add(field4);







        if (convertView == null) {
            ViewGroup rowRoot = (ViewGroup) inflater.inflate(R.layout.listview_row_integer_positive_or_zero_4, null);
            TextView label = (TextView) rowRoot.findViewById(R.id.text_label);
            final EditText editText = (EditText) rowRoot.findViewById(R.id.edit_integer_pos_row);
            EditText editText2 = (EditText) rowRoot.findViewById(R.id.edit_integer_pos_row_2);
            EditText editText3 = (EditText) rowRoot.findViewById(R.id.edit_integer_pos_row_3);
            EditText editText4 = (EditText) rowRoot.findViewById(R.id.edit_integer_pos_row_4);


            ArrayList<EditText> editTexts = new ArrayList<>();
            editTexts.add(editText);
            editTexts.add(editText2);
            editTexts.add(editText3); editTexts.add(editText4);



            editText.setFilters(new InputFilter[]{new InpFilter()});
            editText2.setFilters(new InputFilter[]{new InpFilter()});
            editText3.setFilters(new InputFilter[]{new InpFilter()});
            editText4.setFilters(new InputFilter[]{new InpFilter()});


            EditTextWatcher watcher = new EditTextWatcher(field);
            editText.addTextChangedListener(watcher);

            EditTextWatcher watcher2 = new EditTextWatcher(field2);
            editText2.addTextChangedListener(watcher2);

            EditTextWatcher watcher3 = new EditTextWatcher(field3);
            editText3.addTextChangedListener(watcher3);

            EditTextWatcher watcher4 = new EditTextWatcher(field4);
            editText4.addTextChangedListener(watcher4);




            holder = new EditTextHolder(label, editText, watcher, isCasesField);
            holder2 = new EditTextHolder(label, editText2, watcher2, isDeathsField);
            holder3 = new EditTextHolder(label, editText3, watcher3, isCasesField);
            holder4 = new EditTextHolder(label, editText4, watcher4, isDeathsField);


            rowRoot.setTag(R.id.TAG_HOLDER1_ID, holder);
            rowRoot.setTag(R.id.TAG_HOLDER2_ID, holder2);
            rowRoot.setTag(R.id.TAG_HOLDER3_ID, holder3);
            rowRoot.setTag(R.id.TAG_HOLDER4_ID, holder4);

//            setupFields(editTexts, fields, rowRoot);

            view = rowRoot;

//            setupFields(editTexts, fields, rowRoot, view);
            alertDialog = new AlertDialog.Builder(view.getContext()).create();
            criticalAlertDialog = new AlertDialog.Builder(view.getContext()).create();
        } else {
            view = convertView;
            holder = (EditTextHolder) view.getTag(R.id.TAG_HOLDER1_ID);
            holder2 = (EditTextHolder) view.getTag(R.id.TAG_HOLDER2_ID);
            holder3 = (EditTextHolder) view.getTag(R.id.TAG_HOLDER3_ID);
            holder4 = (EditTextHolder) view.getTag(R.id.TAG_HOLDER4_ID);
            alertDialog = new AlertDialog.Builder(view.getContext()).create();
            criticalAlertDialog = new AlertDialog.Builder(view.getContext()).create();
        }

        ArrayList<EditTextHolder> holders = new ArrayList<>();
        holders.add(holder);
        holders.add(holder2);
        holders.add(holder3);
        holders.add(holder4);


        setupFields2(holders, fields, view);

        setOnFocusChangeListener(holders, view.getContext());




        return view;
    }

    @Override
    public int getViewType() {
        return RowTypes.INTEGER_ZERO_OR_POSITIVE.ordinal();
    }
    
    private class InpFilter implements InputFilter {

        @Override
        public CharSequence filter(CharSequence str, int start, int end,
                Spanned spn, int spStart, int spEnd) {
            
            if ((str.length() > 0) && (spn.length() > 0) && (spn.charAt(0) == '0')) {
                return Field.EMPTY_FIELD;
            }
            
            if ((spn.length() > 0) && (spStart == 0) && (str.length() > 0) && (str.charAt(0) == '0')) {
                return Field.EMPTY_FIELD;
            }
           
            return str;
        }       
    }


    private void setOnFocusChangeListener(final ArrayList<EditTextHolder> holders, final Context context){
        for( int i = 0; i < holders.size(); i++){
            final int finalI = i;
            holders.get(i).editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (!hasFocus && !holders.get(finalI).editText.getText().toString().equals("")) {
                        setAutoZero(holders);
                        setupValidations(holders.get(finalI),holders, finalI, context);
                    }
                }
            });
        }
    }

    private void setAutoZero(final ArrayList<EditTextHolder> holders) {
            for(int i = 0; i < holders.size(); i++){
                if (holders.get(i).editText.getText().toString().equals("")) {
                    if (holders.get(i).editText.isEnabled()) {
                        holders.get(i).editText.setText("0");
                    }
                }
            }
    }
    private void setupValidations(EditTextHolder holder, final ArrayList<EditTextHolder> holders, int currentIndex, Context context){
        if(!holder.isCasesField  && holder.textWatcher.hasChanged()
                && !alertDialog.isShowing()){
            showValidation(getCasesField(holders, currentIndex), holders.get(currentIndex), context);
        }
        if(holder.textWatcher.hasChanged()  && Integer.parseInt(holder.editText.getText().toString()) > 0 ){
            holder.textWatcher.setChanged(false);
            if(IsCritical.check(field, context)){
                showCriticalValidation(holder, context);
            }

        }
    }
    private void showValidation(EditTextHolder casesHolder, final EditTextHolder deathsHolder, final Context context){
        int deaths, cases;
        if (deathsHolder.editText.getText().toString().equals("")){
            deaths = 0;
        }else{
            deaths = Integer.parseInt(deathsHolder.editText.getText().toString());
        }
        if(casesHolder.editText.getText().toString().equals("")) {
            cases = 0;
        }else{
            cases = Integer.parseInt(casesHolder.editText.getText().toString());
        }
        if(deaths > cases){

            alertDialog.setTitle(context.getString(R.string.validation_alert_dialog_title));
            alertDialog.setMessage("You are about to submit more deaths than cases for "+field.getLabel().split(PREFIX)[0].substring(6));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(R.string.validation_alert_dialog_confirmation), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int pos) {
                    dialogInterface.dismiss();
                    if(IsCritical.check(field, context)){
                        showCriticalValidation(deathsHolder, context);
                    }
                }
            });

            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, context.getString(R.string.validation_alert_dialog_rejection), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int pos) {
                    deathsHolder.editText.setText("0");
                    dialogInterface.dismiss();

                }
            });
            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }

        }

    }
    private void showCriticalValidation(final EditTextHolder holder,Context context ){
        if(!alertDialog.isShowing()){
            criticalAlertDialog.setTitle(context.getString(R.string.validation_alert_dialog_title));
            criticalAlertDialog.setMessage("You are about to submit "+ holder.editText.getText()+" death(s) for "+ field.getLabel().split(PREFIX)[0].substring(6));
            criticalAlertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(R.string.validation_alert_dialog_confirmation), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int pos) {
                    dialogInterface.dismiss();
                }
            });

            criticalAlertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, context.getString(R.string.validation_alert_dialog_rejection), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int pos) {
                    holder.editText.setText("0");
                    dialogInterface.dismiss();
                }
            });
            if (!criticalAlertDialog.isShowing()) {
                criticalAlertDialog.show();
            }
        }

    }

    private void setupFields(ArrayList<EditText> editTexts, ArrayList<Field> fields, ViewGroup rowRoot){
        TextView label = (TextView) rowRoot.findViewById(R.id.text_label);
        ArrayList<Integer> tagsIds = new ArrayList<>();
        tagsIds.add(R.id.TAG_HOLDER1_ID);
        tagsIds.add(R.id.TAG_HOLDER2_ID);
        tagsIds.add(R.id.TAG_HOLDER3_ID);
        tagsIds.add(R.id.TAG_HOLDER4_ID);

        for(int i = 0; i < editTexts.size(); i++){
            editTexts.get(i).setFilters(new InputFilter[]{new InpFilter()});
            EditTextWatcher watcher = new EditTextWatcher(fields.get(i));
            editTexts.get(i).addTextChangedListener(watcher);
            EditTextHolder holder = new EditTextHolder(label, editTexts.get(i), watcher);
            rowRoot.setTag(tagsIds.get(i), holder);

        }
    }

    private void setupFields2(ArrayList<EditTextHolder> holders, ArrayList<Field> fields, View view){
        for(int i = 0; i < holders.size(); i++){

            String[] label = fields.get(i).getLabel().split(PREFIX);

            holders.get(i).textLabel.setText(label[0].substring(6));

            holders.get(i).textWatcher.setField(fields.get(i));
            holders.get(i).editText.addTextChangedListener(holders.get(i).textWatcher);
            holders.get(i).editText.setText(fields.get(i).getValue());
            holders.get(i).editText.setSelectAllOnFocus(true);
            holders.get(i).editText.clearFocus();

            IsDisabled.setEnabled(holders.get(i).editText, fields.get(i), view.getContext());

        }
    }

    private EditTextHolder getCasesField(ArrayList<EditTextHolder> holders, int currentIndex){
        return holders.get(currentIndex -1);
    }
}