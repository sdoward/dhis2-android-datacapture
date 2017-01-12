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
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class GenderRow extends Row {
    public static final String FEMALE = "gender_female";
    public static final String MALE = "gender_male";
    public static final String OTHER = "gender_other";


	public GenderRow(LayoutInflater inflater, Field field) {
		super(field, inflater);
	}

	@Override
	public View getView(int position, View convertView) {
		View view;
		BooleanRowHolder holder;

		if (convertView == null) {
			ViewGroup rootView = (ViewGroup) getInflater().inflate(R.layout.listview_row_gender, null);
			TextView label = (TextView) rootView.findViewById(R.id.text_label);

			MaleButtonListener mListener = new MaleButtonListener(getField());
			FemaleButtonListener fListener = new FemaleButtonListener(getField());
			OtherButtonListener oListener = new OtherButtonListener(getField());

			CompoundButton mButton = (CompoundButton) rootView.findViewById(R.id.option_male);
			CompoundButton fButton = (CompoundButton) rootView.findViewById(R.id.option_female);
			CompoundButton oButton = (CompoundButton) rootView.findViewById(R.id.option_other);

			mButton.setOnCheckedChangeListener(mListener);
			fButton.setOnCheckedChangeListener(fListener);
			oButton.setOnCheckedChangeListener(oListener);

			holder = new BooleanRowHolder(label, mListener, fListener, oListener, mButton, fButton,
					oButton);

			rootView.setTag(holder);
			view = rootView;
		} else {
			view = convertView;
			holder = (BooleanRowHolder) convertView.getTag();
		}

		holder.textLabel.setText(getField().getLabel());

		holder.optionMaleListener.setField(getField());
		holder.optionFemaleListener.setField(getField());
		holder.optionOtherListener.setField(getField());

		holder.optionMale.setOnCheckedChangeListener(holder.optionMaleListener);
		holder.optionFemale.setOnCheckedChangeListener(holder.optionFemaleListener);
		holder.optionOther.setOnCheckedChangeListener(holder.optionOtherListener);

		if (getField().getValue().equals(MALE))
			holder.optionMale.setChecked(true);
		else if (getField().getValue().equals(FEMALE))
			holder.optionFemale.setChecked(true);
		else if (getField().getValue().equals(OTHER))
			holder.optionOther.setChecked(true);

		return view;
	}

	@Override
	public int getViewType() {
		return FieldType.GENDER.ordinal();
	}

	private class BooleanRowHolder {
		final TextView textLabel;

		final CompoundButton optionMale;
		final CompoundButton optionFemale;
		final CompoundButton optionOther;

		final MaleButtonListener optionMaleListener;
		final FemaleButtonListener optionFemaleListener;
		final OtherButtonListener optionOtherListener;

		BooleanRowHolder(TextView tLabel, MaleButtonListener mListener,
				FemaleButtonListener fListener, OtherButtonListener oListener,
				CompoundButton mButton, CompoundButton fButton, CompoundButton oButton) {

			textLabel = tLabel;

			optionMaleListener = mListener;
			optionFemaleListener = fListener;
			optionOtherListener = oListener;

			optionMale = mButton;
			optionFemale = fButton;
			optionOther = oButton;
		}
	}

	private class MaleButtonListener implements OnCheckedChangeListener {
		private Field field;

		MaleButtonListener(Field field) {
			this.field = field;
		}

		public void setField(Field field) {
			this.field = field;
		}

		@Override
		public void onCheckedChanged(CompoundButton button, boolean isChecked) {
			if (isChecked)
				field.setValue(MALE);
		}
	}

	private class FemaleButtonListener implements OnCheckedChangeListener {
		private Field field;

		FemaleButtonListener(Field field) {
			this.field = field;
		}

		public void setField(Field field) {
			this.field = field;
		}

		@Override
		public void onCheckedChanged(CompoundButton button, boolean isChecked) {
			if (isChecked)
				field.setValue(FEMALE);
		}
	}

	private class OtherButtonListener implements OnCheckedChangeListener {
		private Field field;

		OtherButtonListener(Field field) {
			this.field = field;
		}

		public void setField(Field field) {
			this.field = field;
		}

		@Override
		public void onCheckedChanged(CompoundButton button, boolean isChecked) {
			if (isChecked)
				field.setValue(OTHER);
		}
	}

}
