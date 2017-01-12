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

import org.dhis2.ehealthMobile.io.models.Field;

import org.dhis2.ehealthMobile.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class BooleanRow extends Row {
    
    public BooleanRow(LayoutInflater inflater, Field field) {
        super(field, inflater);
    }

    @Override
    public View getView(int position, View convertView) {
        View view;
        BooleanRowHolder holder;
        
        if (convertView == null) {
            ViewGroup rootView = (ViewGroup) getInflater().inflate(R.layout.listview_row_boolean, null);
            TextView label = (TextView) rootView.findViewById(R.id.text_label);
            
            TrueButtonListener tListener = new TrueButtonListener(getField());
            FalseButtonListener fListener = new FalseButtonListener(getField());
            NoneButtonListener nListener = new NoneButtonListener(getField());
            
            CompoundButton tButton = (CompoundButton) rootView.findViewById(R.id.true_button);
            CompoundButton fButton = (CompoundButton) rootView.findViewById(R.id.false_button);
            CompoundButton nButton = (CompoundButton) rootView.findViewById(R.id.none_button);
            
            tButton.setOnCheckedChangeListener(tListener);
            fButton.setOnCheckedChangeListener(fListener);
            nButton.setOnCheckedChangeListener(nListener);
            
            holder = new BooleanRowHolder(label, tListener, fListener, 
                    nListener, tButton, fButton, nButton);
            
            rootView.setTag(holder);
            view = rootView;
        } else {
            view = convertView;
            holder = (BooleanRowHolder) convertView.getTag();
        }
        
        holder.textLabel.setText(getField().getLabel());
        
        holder.trueButtonListener.setField(getField());
        holder.falseButtonListener.setField(getField());
        holder.noneButtonListener.setField(getField());
        
        holder.trueButton.setOnCheckedChangeListener(holder.trueButtonListener);
        holder.falseButton.setOnCheckedChangeListener(holder.falseButtonListener);
        holder.noneButton.setOnCheckedChangeListener(holder.noneButtonListener);
        
        if (getField().getValue().equals(Field.FALSE)) holder.falseButton.setChecked(true);
        else if (getField().getValue().equals(Field.TRUE)) holder.trueButton.setChecked(true);
        else if (getField().getValue().equals(Field.EMPTY_FIELD)) holder.noneButton.setChecked(true);
        
        return view;
    }

    @Override
    public int getViewType() {
        return FieldType.BOOLEAN.ordinal();
    }
    
    private class BooleanRowHolder {
        final TextView textLabel;
        
        final CompoundButton trueButton;
        final CompoundButton falseButton;
        final CompoundButton noneButton;
        
        final TrueButtonListener  trueButtonListener;
        final FalseButtonListener falseButtonListener;
        final NoneButtonListener  noneButtonListener;
        
        BooleanRowHolder(TextView tLabel, TrueButtonListener tListener,
                FalseButtonListener fListener, NoneButtonListener nListener,
                CompoundButton tButton, CompoundButton fButton, CompoundButton nButton) {
            
            textLabel = tLabel;
            
            trueButtonListener = tListener;
            falseButtonListener = fListener;
            noneButtonListener = nListener;
            
            trueButton = tButton;
            falseButton = fButton;
            noneButton = nButton;
        }
    }
    
    private class TrueButtonListener implements OnCheckedChangeListener {
        private Field field;
        
        TrueButtonListener(Field field) {
            this.field = field;
        }
        
        public void setField(Field field) {
            this.field = field;
        }

        @Override
        public void onCheckedChanged(CompoundButton button, boolean isChecked) {
            if (isChecked) field.setValue(Field.TRUE);
        } 
    }
    
    private class FalseButtonListener implements OnCheckedChangeListener {
        private Field field;
        
        FalseButtonListener(Field field) {
            this.field = field;
        }
        
        public void setField(Field field) {
            this.field = field;
        }

        @Override
        public void onCheckedChanged(CompoundButton button, boolean isChecked) {
            if (isChecked)  field.setValue(Field.FALSE);
        } 
    }

    private class NoneButtonListener implements OnCheckedChangeListener {
        private Field field;
        
        NoneButtonListener(Field field) {
            this.field = field;
        }
        
        public void setField(Field field) {
            this.field = field;
        }

        @Override
        public void onCheckedChanged(CompoundButton button, boolean isChecked) {
            if (isChecked)  field.setValue(Field.EMPTY_FIELD);
        } 
    }
    
}





