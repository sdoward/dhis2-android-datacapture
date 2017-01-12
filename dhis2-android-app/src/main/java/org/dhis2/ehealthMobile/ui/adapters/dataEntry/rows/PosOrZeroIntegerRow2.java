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

package org.dhis2.ehealthMobile.ui.adapters.dataEntry.rows;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.dhis2.ehealthMobile.R;
import org.dhis2.ehealthMobile.io.Constants;
import org.dhis2.ehealthMobile.io.holders.DatasetInfoHolder;
import org.dhis2.ehealthMobile.io.models.Field;
import org.dhis2.ehealthMobile.ui.activities.DataEntryActivity;
import org.dhis2.ehealthMobile.utils.DiseaseGroupLabels;
import org.dhis2.ehealthMobile.utils.IsDisabled;
import org.dhis2.ehealthMobile.utils.ViewUtils;

import java.util.ArrayList;

public class PosOrZeroIntegerRow2 extends Row {

    public static final String TAG = PosOrZeroIntegerRow2.class.getSimpleName();
    public static final String PREFIX = " EIDSR-";

    private final LayoutInflater inflater;
    public final Field field,field2, field3, field4;
    private AlertDialog alertDialog;
    private AlertDialog criticalDiseaseAlertDialog;
    private final Boolean isCriticalDisease;
    private final Boolean isAdditionalDisease;
    private final String defaultValue = "0";
    private Button deleteButton;
    private ImageView criticalDiseaseIcon;
    private IsDisabled isDisabled;
    private DiseaseGroupLabels diseaseGroupLabels;
    private final DatasetInfoHolder info;



    public PosOrZeroIntegerRow2(LayoutInflater inflater, DatasetInfoHolder info, ArrayList<Field> fields, Boolean isCriticalDisease, Boolean isAdditionalDisease) {
        super(null, inflater);

        this.inflater = inflater;
        this.field = fields.get(fields.size()-4);
        this.field2 = fields.get(fields.size()-3);
        this.field3 = fields.get(fields.size()-2);
        this.field4 = fields.get(fields.size()-1);
        this.isCriticalDisease = isCriticalDisease;
        this.isAdditionalDisease = isAdditionalDisease;
        this.info = info;
    }

    @Override
    public View getView(int position, View convertView) {
        View view;

        ArrayList<EditTextHolder> holders = new ArrayList<>();

        ArrayList<Field> fields = new ArrayList<>();
        fields.add(field);
        fields.add(field2);
        fields.add(field3);
        fields.add(field4);


        if (convertView == null) {
            ViewGroup rowRoot = (ViewGroup) inflater.inflate(R.layout.listview_row_integer_positive_or_zero_4, null);
            final EditText editText = (EditText) rowRoot.findViewById(R.id.edit_integer_pos_row);
            EditText editText2 = (EditText) rowRoot.findViewById(R.id.edit_integer_pos_row_2);
            EditText editText3 = (EditText) rowRoot.findViewById(R.id.edit_integer_pos_row_3);
            EditText editText4 = (EditText) rowRoot.findViewById(R.id.edit_integer_pos_row_4);



            ArrayList<EditText> editTexts = new ArrayList<>();
            editTexts.add(editText);
            editTexts.add(editText2);
            editTexts.add(editText3);
            editTexts.add(editText4);


            initializeEditTextHolders(editTexts, fields, rowRoot, holders);

            view = rowRoot;

            alertDialog = new AlertDialog.Builder(view.getContext()).create();
            criticalDiseaseAlertDialog = new AlertDialog.Builder(view.getContext()).create();
            isDisabled = new IsDisabled(view.getContext(), info);
            diseaseGroupLabels = new DiseaseGroupLabels(view.getContext(), info);
        } else {
            view = convertView;

            holders.add((EditTextHolder) view.getTag(R.id.TAG_HOLDER1_ID));
            holders.add((EditTextHolder) view.getTag(R.id.TAG_HOLDER2_ID));
            holders.add((EditTextHolder) view.getTag(R.id.TAG_HOLDER3_ID));
            holders.add((EditTextHolder) view.getTag(R.id.TAG_HOLDER4_ID));

            alertDialog = new AlertDialog.Builder(view.getContext()).create();
            criticalDiseaseAlertDialog = new AlertDialog.Builder(view.getContext()).create();
            isDisabled = new IsDisabled(view.getContext(), info);
            diseaseGroupLabels = new DiseaseGroupLabels(view.getContext(), info);
        }


        setupEditTextHolders(holders, fields, view, position);

        setOnFocusChangeListeners(holders, view);

        view.setTag(field.getDataElement());
        view.setContentDescription(field.getDataElement());
        highlightLabelIfIsRowComplete(view.getContext(), holders, holders.get(0));

        adjustViewIfGrouped(view, holders);
        hidePositionIfGroupedOrAdditional(holders.get(0));


        return view;
    }

    @Override
    public String getFieldId() {
        // TODO: we can assume that all the fields have the same data element..?
        return field.getDataElement();
    }

    @Override
    public int getViewType() {
        return FieldType.INTEGER_ZERO_OR_POSITIVE.ordinal();
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

    private void setOnFocusChangeListeners(final ArrayList<EditTextHolder> editTextHolders, View view){
        final Context context = view.getContext();
        for( int i = 0; i < editTextHolders.size(); i++){
            final int finalI = i;
            editTextHolders.get(i).editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    EditText editText = editTextHolders.get(finalI).editText;
                    if(hasFocus && !editText.isEnabled()){
                        hideKeyboard(context, view);
                    }
                    autoZeroChecks(hasFocus, editTextHolders, finalI);
                    if (hasLostFocusAndIsNotEmpty(hasFocus,editText)) {
                        setupValidations(editTextHolders.get(finalI),editTextHolders, finalI, context);
                    }
                    //Highlight disease label if all fields have a value meaning the row is complete.
                    highlightLabelIfIsRowComplete(context, editTextHolders, editTextHolders.get(finalI));
                }
            });
        }
    }

    private void autoZeroChecks(Boolean hasFocus, final ArrayList<EditTextHolder> holders, int finalI) {
        EditText currentEditText = holders.get(finalI).editText;

        if (hasLostFocusAndIsNotEmpty(hasFocus, currentEditText)) {
            for (EditTextHolder holder: holders) {
                if (isEmptyEditText(holder.editText) && holder.editText.isEnabled()) {
                    holder.editText.setText(defaultValue);
                }
            }
        }

        ifHasFocusAndHasZeroMakeEmpty(hasFocus, currentEditText);
        ifLostFocusAndBlankAddZero(hasFocus, currentEditText, holders);
    }

    private void ifHasFocusAndHasZeroMakeEmpty(Boolean hasFocus, EditText editText){
        if(hasFocus && editText.getText().toString().equals(defaultValue) && editText.isEnabled()){
                editText.getText().clear();
        }
    }

    private void ifLostFocusAndBlankAddZero(Boolean hasFocus, EditText editText, final ArrayList<EditTextHolder> holders){
        if(!hasFocus && isEmptyEditText(editText) && editText.isEnabled()){
            for(EditTextHolder holder: holders){
                if(!isEmptyEditText(holder.editText) && holder.editText.isEnabled()){
                    editText.getText().append(defaultValue);
                }
            }

        }
    }

    private void highlightLabelIfIsRowComplete(Context context, ArrayList<EditTextHolder> editTextHolders, EditTextHolder editTextHolder){
        if(isRowComplete(editTextHolders)){
            editTextHolder.textLabel.setTextColor(ContextCompat.getColor(context, R.color.disease_label_highlight));
        }else{
            editTextHolder.textLabel.setTextColor(ContextCompat.getColor(context, R.color.disease_label_default));
        }
    }

    private Boolean isRowComplete(ArrayList<EditTextHolder> editTextHolders){

        //Check for empty fields.
        for(EditTextHolder holder: editTextHolders){
            if(holder.editText.getText().toString().equals("") && holder.editText.isEnabled()){
                return false;
            }
        }

        return true;
    }

    private void setupValidations(EditTextHolder editTextHolder, final ArrayList<EditTextHolder> editTextHolders, int currentIndex, Context context){
        if(!editTextHolder.isCasesField  && editTextHolder.textWatcher.hasChanged()
                && !alertDialog.isShowing()){
            showDeathsGreaterValidation(getCasesField(editTextHolders, currentIndex), editTextHolders.get(currentIndex), context);
        }
        if(editTextHolder.textWatcher.hasChanged()  && Integer.parseInt(editTextHolder.editText.getText().toString()) > 0 ){
            editTextHolder.textWatcher.setChanged(false);
            if(isCriticalDisease){
                showCriticalValidation(editTextHolder, context);
            }
        }
    }

    private void showDeathsGreaterValidation(EditTextHolder casesEditTextHolder, final EditTextHolder deathsEditTextHolder, final Context context){
        int deaths, cases;
        deaths = getValueFromEditText(deathsEditTextHolder.editText);
        cases = getValueFromEditText(casesEditTextHolder.editText);

        if(deaths > cases && casesEditTextHolder.editText.isEnabled()){

            alertDialog.setTitle(context.getString(R.string.validation_alert_dialog_title));
            alertDialog.setMessage("You are about to submit more deaths than cases for "+field.getLabel().split(PREFIX)[0].substring(6));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(R.string.validation_alert_dialog_confirmation), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int position) {
                    dialogInterface.dismiss();
                    //Also need to check if death field belongs to a critical disease. If it does show validation
                    if(isCriticalDisease){
                        showCriticalValidation(deathsEditTextHolder, context);
                    }
                }
            });

            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, context.getString(R.string.validation_alert_dialog_rejection), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int position) {
                    deathsEditTextHolder.editText.setText(defaultValue);
                    dialogInterface.dismiss();
                }
            });

            alertDialog.setCanceledOnTouchOutside(false);
            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }

        }

    }

    private void showCriticalValidation(final EditTextHolder holder,Context context ){
        if(!alertDialog.isShowing()){
            criticalDiseaseAlertDialog.setTitle(context.getString(R.string.validation_alert_dialog_title));
            if(holder.isCasesField){
                criticalDiseaseAlertDialog.setMessage(getValidationMessage(holder.editText, "case"));
            }else{
                criticalDiseaseAlertDialog.setMessage(getValidationMessage(holder.editText, "death"));
            }
            criticalDiseaseAlertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(R.string.validation_alert_dialog_confirmation), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int position) {
                    dialogInterface.dismiss();
                }
            });

            criticalDiseaseAlertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, context.getString(R.string.validation_alert_dialog_rejection), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int position) {
                    holder.editText.setText(defaultValue);
                    dialogInterface.dismiss();
                }
            });
            criticalDiseaseAlertDialog.setCanceledOnTouchOutside(false);
            if (!criticalDiseaseAlertDialog.isShowing()) {
                criticalDiseaseAlertDialog.show();
            }
        }

    }

    /**
     * Rather than initializing everything line by line, the filters, holders and watchers are put in a list
     * and iterated over initializing every editText and editTextHolder accordingly.
     * @param editTexts ArrayList<EditText>
     * @param fields ArrayList<Field>
     * @param rowRoot ViewGroup
     * @param holders ArrayList<EditTextHolder>
     */
    private void initializeEditTextHolders(ArrayList<EditText> editTexts, ArrayList<Field> fields, ViewGroup rowRoot, final ArrayList<EditTextHolder> holders){
        TextView label = (TextView) rowRoot.findViewById(R.id.text_label);
        TextView diseaseNumber = (TextView) rowRoot.findViewById(R.id.diseaseNumber);

        ArrayList<Integer> tagsIds = new ArrayList<>();
        tagsIds.add(R.id.TAG_HOLDER1_ID);
        tagsIds.add(R.id.TAG_HOLDER2_ID);
        tagsIds.add(R.id.TAG_HOLDER3_ID);
        tagsIds.add(R.id.TAG_HOLDER4_ID);

        for(int i = 0; i < editTexts.size(); i++){
            editTexts.get(i).setFilters(new InputFilter[]{new InpFilter()});
            EditTextWatcher watcher = new EditTextWatcher(fields.get(i));
            editTexts.get(i).addTextChangedListener(watcher);
            holders.add(new EditTextHolder(diseaseNumber, label, editTexts.get(i), watcher, isCasesField(fields.get(i))));
            rowRoot.setTag(tagsIds.get(i), holders.get(i));
        }
    }

    /**
     * Sets up the editTextHolders that have been initialized in an array list.
     * It then iterates over the list and sets properties and checks accordingly.
     * This is so we don't have repeating lines of code doing the same thing.
     * @param holders ArrayList<EditTextHolder>
     * @param fields ArrayList<Field>
     * @param view View
     */
    private void setupEditTextHolders(ArrayList<EditTextHolder> holders, ArrayList<Field> fields, View view, int position){
        for(int i = 0; i < holders.size(); i++){
            String[] label = fields.get(i).getLabel().split(PREFIX);

            setupDeleteButton(view);
            holders.get(i).numberLabel.setText(String.valueOf(position+1));
            holders.get(i).textLabel.setText(label[0].substring(6));
            holders.get(i).textWatcher.setField(fields.get(i));
            holders.get(i).editText.addTextChangedListener(holders.get(i).textWatcher);
            holders.get(i).editText.setText(fields.get(i).getValue());
            holders.get(i).editText.setSelectAllOnFocus(true);
            holders.get(i).editText.clearFocus();
            holders.get(i).editText.setContentDescription(fields.get(i).getCategoryOptionCombo());

            isDisabled.setEnabled(holders.get(i).editText, fields.get(i));

            setIsCriticalDiseaseIcon(view);

        }
    }

    private void setupDeleteButton(View view){
        deleteButton = (Button) view.findViewById(R.id.delete_button);
        deleteButton.setVisibility(View.GONE);
        if(isAdditionalDisease){
            deleteButton.setVisibility(View.VISIBLE);
        }
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DataEntryActivity.TAG);
                intent.putExtra(TAG, field.getDataElement());
                //Tell the DataEntryActivity that holds the listView with all the diseases that a disease's delete button has been clicked
                LocalBroadcastManager.getInstance(view.getContext()).sendBroadcast(intent);
            }
        });

    }

    private Boolean isCasesField(Field field){
        Boolean isCasesField = false;
        if(field.getCategoryOptionCombo().equals(Constants.UNDER_FIVE_CASES)
                || field.getCategoryOptionCombo().equals(Constants.OVER_FIVE_CASES)){
            isCasesField = true;
        }
        return isCasesField;
    }

    private EditTextHolder getCasesField(ArrayList<EditTextHolder> holders, int currentIndex){
        return holders.get(currentIndex -1);
    }

    private Boolean hasLostFocusAndIsNotEmpty(Boolean hasFocus, EditText editText){
        Boolean hasLostFocusAndNotIsEmpty = false;
        if(!hasFocus && !isEmptyEditText(editText)){
            hasLostFocusAndNotIsEmpty = true;
        }
        return hasLostFocusAndNotIsEmpty;
    }

    private Boolean isEmptyEditText(EditText editText){
        Boolean isEmpty = false;
        if(editText.getText().toString().equals("")){
            isEmpty = true;
        }
        return isEmpty;
    }

    private String getValidationMessage(EditText editText, String type){
        String message;
        if(Integer.parseInt(editText.getText().toString()) > 1){
            message = "You are about to submit "+ editText.getText()+" "+type+"s for "+ field.getLabel().split(PREFIX)[0].substring(6);
        }else{
            message = "You are about to submit "+ editText.getText()+" "+type+" for "+ field.getLabel().split(PREFIX)[0].substring(6);
        }

        return message;
    }

    private int getValueFromEditText(EditText editText){
        int value;

        if(editText.getText().toString().equals("")) {
            value = 0;
        }else{
            value = Integer.parseInt(editText.getText().toString());
        }

        return value;
    }

    private void setIsCriticalDiseaseIcon(View view){
        criticalDiseaseIcon = (ImageView) view.findViewById(R.id.criticalDiseaseIcon);
        if(this.isCriticalDisease){
            ViewUtils.enableViews(criticalDiseaseIcon);
        }else{
            ViewUtils.hideAndDisableViews(criticalDiseaseIcon);
        }
    }

    private void adjustViewIfGrouped(View view, ArrayList<EditTextHolder> holders){
        //Offset the view to the left by 30dp if field is related to or is malaria. Why? Because design.
        if(diseaseGroupLabels.hasGroup(field.getDataElement())){
            view.setX(30);
            //compensate for offset by adjusting the padding on the right.
            view.setPadding(8,8,25,8);
            //Reduce alpha to differentiate malaria labels.
            holders.get(0).textLabel.setAlpha(0.8f);
        }else{
            view.setX(0);
            view.setPadding(8,8,0,8);
            holders.get(0).textLabel.setAlpha(1.0f);
        }
    }

    private void hidePositionIfGroupedOrAdditional(EditTextHolder holder){
        if(diseaseGroupLabels.hasGroup(field.getDataElement()) || isAdditionalDisease){
            ViewUtils.hideAndDisableViews(holder.numberLabel);
        }else{
            ViewUtils.enableViews(holder.numberLabel);
        }
    }

    private void hideKeyboard(Context context, View view){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(view == null){
            view = new View(context);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
