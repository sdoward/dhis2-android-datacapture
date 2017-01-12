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

import android.view.LayoutInflater;
import android.view.View;

import org.dhis2.ehealthMobile.io.models.Field;
import org.dhis2.ehealthMobile.io.models.configfile.FieldGroup;


public abstract class Row {

	public static class EmptyField extends Field {
		private EmptyField(){}

		@Override
		public String getLabel() {
			return "";
		}
	}

	public static final Field EMPTY_FIELD = new EmptyField();

	private final Field field;
	private final LayoutInflater inflater;
	private FieldGroup fieldGroup;

	public Row(Field field, LayoutInflater inflater){
		this.field = field;
		this.inflater = inflater;
	}

	public Field getField() {
		return field != null ? field : EMPTY_FIELD;
	}

	public LayoutInflater getInflater() {
		return inflater;
	}

	public FieldGroup getFieldGroup(){
		return fieldGroup;
	}

	public void setFieldGroup(FieldGroup fieldGroup){
		this.fieldGroup = fieldGroup;
	}

	public String getFieldId(){
		if(field != null)
			return field.getDataElement();

		return null;
	}

	public abstract View getView(int position, View convertView);
	public abstract int getViewType();
}
