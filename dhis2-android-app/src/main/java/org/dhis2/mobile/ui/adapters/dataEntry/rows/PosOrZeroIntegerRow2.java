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
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.dhis2.mobile.R;
import org.dhis2.mobile.io.models.Field;
import org.dhis2.mobile.utils.IsCritical;
import org.dhis2.mobile.utils.IsDisabled;

public class PosOrZeroIntegerRow2 implements Row {
    private final LayoutInflater inflater;
    private final Field field;
    private final Field field2, field3, field4;
    private AlertDialog alertDialog;
    private AlertDialog ciriticalAlertDialog;


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




        if (convertView == null) {
            ViewGroup rowRoot = (ViewGroup) inflater.inflate(R.layout.listview_row_integer_positive_or_zero_4, null);
            TextView label = (TextView) rowRoot.findViewById(R.id.text_label);
            final EditText editText = (EditText) rowRoot.findViewById(R.id.edit_integer_pos_row);
            EditText editText2 = (EditText) rowRoot.findViewById(R.id.edit_integer_pos_row_2);
            EditText editText3 = (EditText) rowRoot.findViewById(R.id.edit_integer_pos_row_3);
            EditText editText4 = (EditText) rowRoot.findViewById(R.id.edit_integer_pos_row_4);

            TextInputLayout inputLayout = (TextInputLayout) rowRoot.findViewById(R.id.edit_integer_pos_layout);
            TextInputLayout inputLayout2 = (TextInputLayout) rowRoot.findViewById(R.id.edit_integer_pos_layout_2);
            TextInputLayout inputLayout3 = (TextInputLayout) rowRoot.findViewById(R.id.edit_integer_pos_layout_3);
            TextInputLayout inputLayout4 = (TextInputLayout) rowRoot.findViewById(R.id.edit_integer_pos_layout_4);


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



            
            holder = new EditTextHolder(label, editText, watcher,inputLayout);


            holder2 = new EditTextHolder(label, editText2, watcher2,inputLayout2);
            holder3 = new EditTextHolder(label, editText3, watcher3,inputLayout3);
            holder4 = new EditTextHolder(label, editText4, watcher4,inputLayout4);
            rowRoot.setTag(R.id.TAG_HOLDER1_ID, holder);
            rowRoot.setTag(R.id.TAG_HOLDER2_ID, holder2);
            rowRoot.setTag(R.id.TAG_HOLDER3_ID, holder3);
            rowRoot.setTag(R.id.TAG_HOLDER4_ID, holder4);



            view = rowRoot;

            alertDialog = new AlertDialog.Builder(view.getContext()).create();
            ciriticalAlertDialog = new AlertDialog.Builder(view.getContext()).create();
        } else {
            view = convertView;
            holder = (EditTextHolder) view.getTag(R.id.TAG_HOLDER1_ID);
            holder2 = (EditTextHolder) view.getTag(R.id.TAG_HOLDER2_ID);
            holder3 = (EditTextHolder) view.getTag(R.id.TAG_HOLDER3_ID);
            holder4 = (EditTextHolder) view.getTag(R.id.TAG_HOLDER4_ID);

            alertDialog = new AlertDialog.Builder(view.getContext()).create();
            ciriticalAlertDialog = new AlertDialog.Builder(view.getContext()).create();
        }

            String[] label = field.getLabel().split(" EIDSR-");

            holder.textLabel.setText(label[0].substring(6));

            holder.textWatcher.setField(field);
            holder.editText.addTextChangedListener(holder.textWatcher);
            holder.editText.setText(field.getValue());
            holder.editText.setSelectAllOnFocus(true);

//        if (holder.inputLayout != null) {
//            holder.inputLayout.setHint("<"+field.getLabel().split("<")[1].split(",")[0]);
//        }
        holder.editText.clearFocus();





            holder2.textWatcher.setField(field2);
            holder2.editText.addTextChangedListener(holder2.textWatcher);
            holder2.editText.setText(field2.getValue());
        holder2.editText.setSelectAllOnFocus(true);
//        assert holder2.inputLayout != null;
//        holder2.inputLayout.setHint("<"+field2.getLabel().split("<")[1].split(",")[0]);
            holder2.editText.clearFocus();



            holder3.textWatcher.setField(field3);
            holder3.editText.addTextChangedListener(holder3.textWatcher);
            holder3.editText.setText(field3.getValue());
            holder3.editText.setSelectAllOnFocus(true);
//        assert holder3.inputLayout != null;
//        holder3.inputLayout.setHint(">"+field3.getLabel().split(">")[1].split(",")[0]);
            holder3.editText.clearFocus();



            holder4.textWatcher.setField(field4);
            holder4.editText.addTextChangedListener(holder4.textWatcher);
            holder4.editText.setText(field4.getValue());
            holder4.editText.setSelectAllOnFocus(true);
//        assert holder4.inputLayout != null;
//        holder4.inputLayout.setHint(">"+field4.getLabel().split(">")[1].split(",")[0]);
            holder4.editText.clearFocus();


        //check whether field should be diabled
        IsDisabled.check(holder.editText, field);
        IsDisabled.check(holder2.editText, field2);
        IsDisabled.check(holder3.editText, field3);
        IsDisabled.check(holder4.editText, field4);




        setAutoZero(holder, holder2, holder3, holder4, null, null);
        setAutoZero(holder2, holder3, holder4, holder, holder, holder2);
        setAutoZero(holder3, holder4, holder, holder2, null, null);
        setAutoZero(holder4, holder, holder2, holder3, holder3, holder4);




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

    private void setAutoZero(final EditTextHolder holder, final EditTextHolder holder2, final EditTextHolder holder3, final EditTextHolder holder4,
                             final EditTextHolder casesHolder, final EditTextHolder deathsHolder){
        holder.editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Log.d("Changed", holder.textWatcher.hasChanged()+"");
                if(!holder.editText.getText().toString().equals("")) {
                    if (!b && holder2.editText.getText().toString().equals("")) {
                        if(holder2.editText.isEnabled()) {
                            holder2.editText.setText("0");
                        }
                    }
                    if (!b && holder3.editText.getText().toString().equals("")) {
                        if(holder3.editText.isEnabled()) {
                            holder3.editText.setText("0");
                        }
                    }
                    if (!b && holder4.editText.getText().toString().equals("")) {
                        if(holder4.editText.isEnabled()) {
                            holder4.editText.setText("0");
                        }
                    }
                    if (!b && holder.editText.getText().toString().equals("")) {
                        if(holder.editText.isEnabled()) {
                            holder.editText.setText("0");
                        }
                    }
                    if(!b && deathsHolder != null && casesHolder != null && holder.textWatcher.hasChanged() && !alertDialog.isShowing()){
                        showValidation(casesHolder, deathsHolder, view.getContext());
                    }
                    if(!b && holder.textWatcher.hasChanged()){
                        holder.textWatcher.setChanged(false);
                        if(IsCritical.check(field)){
                            showCriticalValidation(holder);
                        }

                    }
                }


            }
        });
    }
    private void showValidation(EditTextHolder casesHolder, final EditTextHolder deathsHolder, Context context){
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

            alertDialog.setTitle("Yo Dawg!");
            alertDialog.setMessage("You have entered more deaths than cases. Are you sure thats the case?");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YUP", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    if(IsCritical.check(field)){
                        showCriticalValidation(deathsHolder);
                    }
                }
            });

            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NAH DAWG", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    deathsHolder.editText.setText("0");
                    dialogInterface.dismiss();

                }
            });
            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }

        }

    }
    private void showCriticalValidation(final EditTextHolder holder){
        if(!alertDialog.isShowing()){
            ciriticalAlertDialog.setTitle("Whoa, Nelly!");
            ciriticalAlertDialog.setMessage("Thats a mighty critical disease you got there. Are you sure?");
            ciriticalAlertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yee Doggies!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            ciriticalAlertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Nope", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    holder.editText.setText("0");
                    dialogInterface.dismiss();
                }
            });
            if (!ciriticalAlertDialog.isShowing()) {
                ciriticalAlertDialog.show();
            }
        }

    }
}
