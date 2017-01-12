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

package org.dhis2.ehealthMobile.ui.adapters.dataEntry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.dhis2.ehealthMobile.io.Constants;
import org.dhis2.ehealthMobile.io.holders.DatasetInfoHolder;
import org.dhis2.ehealthMobile.io.json.JsonHandler;
import org.dhis2.ehealthMobile.io.json.ParsingException;
import org.dhis2.ehealthMobile.io.models.Field;
import org.dhis2.ehealthMobile.io.models.Group;
import org.dhis2.ehealthMobile.io.models.OptionSet;
import org.dhis2.ehealthMobile.io.models.configfile.ConfigFile;
import org.dhis2.ehealthMobile.io.models.configfile.FieldGroup;
import org.dhis2.ehealthMobile.io.models.configfile.IDSRWeeklyDiseaseReport;
import org.dhis2.ehealthMobile.io.models.eidsr.Disease;
import org.dhis2.ehealthMobile.ui.activities.DataEntryActivity;
import org.dhis2.ehealthMobile.ui.adapters.dataEntry.rows.AutoCompleteRow;
import org.dhis2.ehealthMobile.ui.adapters.dataEntry.rows.BooleanRow;
import org.dhis2.ehealthMobile.ui.adapters.dataEntry.rows.CheckBoxRow;
import org.dhis2.ehealthMobile.ui.adapters.dataEntry.rows.DatePickerRow;
import org.dhis2.ehealthMobile.ui.adapters.dataEntry.rows.GenderRow;
import org.dhis2.ehealthMobile.ui.adapters.dataEntry.rows.IntegerRow;
import org.dhis2.ehealthMobile.ui.adapters.dataEntry.rows.LabelRow;
import org.dhis2.ehealthMobile.ui.adapters.dataEntry.rows.LongTextRow;
import org.dhis2.ehealthMobile.ui.adapters.dataEntry.rows.NegativeIntegerRow;
import org.dhis2.ehealthMobile.ui.adapters.dataEntry.rows.NumberRow;
import org.dhis2.ehealthMobile.ui.adapters.dataEntry.rows.PosIntegerRow;
import org.dhis2.ehealthMobile.ui.adapters.dataEntry.rows.PosOrZeroIntegerRow2;
import org.dhis2.ehealthMobile.ui.adapters.dataEntry.rows.Row;
import org.dhis2.ehealthMobile.ui.adapters.dataEntry.rows.FieldType;
import org.dhis2.ehealthMobile.ui.adapters.dataEntry.rows.TextRow;
import org.dhis2.ehealthMobile.utils.DiseaseGroupLabels;
import org.dhis2.ehealthMobile.utils.IsAdditionalDisease;
import org.dhis2.ehealthMobile.utils.IsCritical;
import org.dhis2.ehealthMobile.utils.PrefUtils;
import org.dhis2.ehealthMobile.utils.TextFileUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FieldAdapter extends BaseAdapter {

	public static void addFieldGroupToRows(List<FieldGroup> fieldGroups, List<Row> rows){
		for(Row row : rows){
			String fieldId = row.getFieldId();
			if(fieldId == null) continue;

			for(FieldGroup fieldGroup : fieldGroups){
				if(fieldGroup.hasFieldId(fieldId)) {
					row.setFieldGroup(fieldGroup);
					break;
				}
			}
		}
	}

	public static Comparator<Row> ROW_COMPARATOR = new Comparator<Row>() {
		@Override
		public int compare(Row lhs, Row rhs) {
			FieldGroup fieldGroup1 = lhs.getFieldGroup();
			FieldGroup fieldGroup2 = rhs.getFieldGroup();

			if(fieldGroup1 == null && fieldGroup2 != null)
				return 1;

			if(fieldGroup2 == null && fieldGroup1 != null)
				return -1;

			if(fieldGroup1 == fieldGroup2)
				return Field.COMPARATOR.compare(lhs.getField(), rhs.getField());

			return fieldGroup1.getId() - fieldGroup2.getId();

		}
	};

	public void addHeadersRow(List<Row> rows){

	}

	private ArrayList<Row> rows;
	private final String adapterLabel;
	private final Group group;
	private LayoutInflater inflater;
	private IsCritical isCritical;
	private IsAdditionalDisease isAdditionalDisease;
	private Map<String, Map<String, PosOrZeroIntegerRow2>> additionalDiseasesRows = new HashMap<>();
	private LabelRow diseaseLabel;
	private DiseaseGroupLabels diseaseGroupLabels;
	private ArrayList<String> labelsAdded = new ArrayList<>();
	private final DatasetInfoHolder info;


	public FieldAdapter(DatasetInfoHolder info, Group group, Context context) {
		ArrayList<Field> fields = group.getFields();
		ArrayList<Field> groupedFields = new ArrayList<>();
		String previousFieldId = "";
		this.info = info;
		this.group = group;
		this.rows = new ArrayList<>();
		this.adapterLabel = group.getLabel();
		inflater = LayoutInflater.from(context);
		isCritical = new IsCritical(context, info);
		isAdditionalDisease = new IsAdditionalDisease(context, info);
		diseaseGroupLabels = new DiseaseGroupLabels(context, info);

		for (int i = 0; i < fields.size(); i++) {
			Field field = fields.get(i);
			if (field.hasOptionSet()) {
				OptionSet optionSet = getOptionSet(context, field.getOptionSet());
				if (!field.getDataElement().equals(Constants.RECEIPT_OF_FORM))
					rows.add(new AutoCompleteRow(inflater, field, optionSet, context));
				continue;
			}
			
			FieldType fieldType = field.getType();

			switch (fieldType) {
				case TEXT:
					rows.add(new TextRow(inflater, field));
					break;
				case LONG_TEXT:
					if (!field.getDataElement().equals(Constants.COMMENT_FIELD))
						rows.add(new LongTextRow(inflater, field));
					break;
				case NUMBER:
					rows.add(new NumberRow(inflater, field));
					break;
				case INTEGER:
					rows.add(new IntegerRow(inflater, field));
					break;
				case INTEGER_ZERO_OR_POSITIVE:
					//Changed from the others to support grouping of Diseases
					//Specific test case for eidsr form
					handleIntegerOrZeroRow2(info, field, groupedFields, previousFieldId);

					groupedFields.add(field);
					previousFieldId = field.getDataElement();
					break;
				case INTEGER_POSITIVE:
					rows.add(new PosIntegerRow(inflater, field));
					break;
				case INTEGER_NEGATIVE:
					rows.add(new NegativeIntegerRow(inflater, field));
					break;
				case BOOLEAN:
					if (!field.getDataElement().equals(Constants.TIMELY)) {
						rows.add(new BooleanRow(inflater, field));
					}
					break;
				case TRUE_ONLY:
					rows.add(new CheckBoxRow(inflater, field));
					break;
				case DATE:
					if (!field.getDataElement().equals(Constants.DATE_RECEIVED))
						rows.add(new DatePickerRow(inflater, field, this, context));
					break;
				case GENDER:
					rows.add(new GenderRow(inflater, field));
					break;
			}
		}
		for (Map.Entry<String, Map<String, PosOrZeroIntegerRow2>> disease : additionalDiseasesRows.entrySet()) {
			for (Map.Entry<String, PosOrZeroIntegerRow2> rows : additionalDiseasesRows.get(disease.getKey()).entrySet()) {
				this.rows.add(rows.getValue());
				//we then need to tell the dataEntryActivity that this additional disease has already been displayed.
				if (context instanceof DataEntryActivity) {
					((DataEntryActivity) context).addToDiseasesShown(disease.getKey(), rows.getKey());
				}
			}
		}


		// since the headers of the form are not included in the dataset
		// we need to get them from the config file
		ConfigFile configFile = PrefUtils.getConfigFile(context);
		if(configFile == null) return;

		IDSRWeeklyDiseaseReport wdr = configFile.weeklyDiseaseReport;
		if(wdr == null) return;

		List<FieldGroup> fieldGroups = wdr.getFieldGroups();
		if(fieldGroups == null || fieldGroups.size() == 0) return;

		addFieldGroupToRows(fieldGroups, rows);

		// sort the rows based on fieldgroup first and then alphabetically
		Collections.sort(rows, ROW_COMPARATOR);

		// add the headers row
		addHeadersRow(rows);
	}

	@Override
	public int getCount() {
		return rows.size();
	}

	@Override
	public int getViewTypeCount() {
		return FieldType.values().length;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		return rows.get(position).getView(getAdjustedPosition(position), convertView);
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

		for (Field field : fields) {
			if (field.getDataElement().equals(disease.getId())) {
				additionalDiseaseFields.add(field);
			}
		}

		this.rows.add(new PosOrZeroIntegerRow2(inflater, info, additionalDiseaseFields, false, true));
		notifyDataSetChanged();
	}

	public void removeItemAtPosition(int position) {
		PosOrZeroIntegerRow2 row = (PosOrZeroIntegerRow2) this.rows.get(position);
		clearPosOrZeroIntegerRow2Fields(row);
		this.rows.remove(position);
		notifyDataSetChanged();
	}

	private Boolean groupedFieldsHasValue(ArrayList<Field> groupedFields) {
		int size = groupedFields.size();

		for (int i = 1; i < 5; i++) {
			if (!isFieldValueEmpty(groupedFields.get(size - i))) {
				return true;
			}
		}


		return false;
	}

	private void clearPosOrZeroIntegerRow2Fields(PosOrZeroIntegerRow2 row) {
		row.field.setValue("");
		row.field2.setValue("");
		row.field3.setValue("");
		row.field4.setValue("");
	}

	private Boolean isFieldValueEmpty(Field field) {
		Boolean isEmpty = false;
		if (field.getValue().equals("")) {
			isEmpty = true;
		}
		return isEmpty;
	}

	private void handleIntegerOrZeroRow2(DatasetInfoHolder info, Field field, ArrayList<Field> groupedFields, String previousFieldId) {
		if (!field.getDataElement().equals(previousFieldId) && groupedFields.size() > 0
				&& !isAdditionalDisease.check(previousFieldId)) {
			//each disease has four fields.
			Boolean isCriticalDisease = isCritical.check(previousFieldId);
			Boolean isAnAdditionalDisease = isAdditionalDisease.check(previousFieldId);
			rows.add(new PosOrZeroIntegerRow2(inflater, info, groupedFields, isCriticalDisease, isAnAdditionalDisease));

			//handle diseases that belong to groups.
			if (diseaseGroupLabels.hasGroup(field.getDataElement())) {
				String label = diseaseGroupLabels.getLabel(field.getDataElement());
				diseaseLabel = new LabelRow(inflater, label, true);
				if (!labelsAdded.contains(label)) {
					rows.add(diseaseLabel);
					labelsAdded.add(label);
				}
			}

		}
		//check if its an additional disease and has values.
		if (!field.getDataElement().equals(previousFieldId) && groupedFields.size() > 0 && isAdditionalDisease.check(previousFieldId)
				&& groupedFieldsHasValue(groupedFields)) {
			//if it is an additional disease and does have value then we add it to a map of additional diseases rows
			//we later iterate over the map and add these additional diseases to the bottom of the listView
			Boolean isAnAdditionalDisease = isAdditionalDisease.check(previousFieldId);
			additionalDiseasesRows.put(previousFieldId, new HashMap<String, PosOrZeroIntegerRow2>());
			additionalDiseasesRows.get(previousFieldId).put(groupedFields.get(groupedFields.size() - 1).getLabel(),
					new PosOrZeroIntegerRow2(inflater, info, groupedFields, false, isAnAdditionalDisease));

		}
	}


	/**
	 * Checks whether a labelRow has been added at a position prior to the current on provided
	 * If so it then checks the size of that group and returns it.
	 *
	 * @param position int
	 * @return int The size of a group with a specific label.
	 */
	private int getPositionDifference(int position) {
		int size = 0;
		int i = 0;
		for (Row row : this.rows) {
			if (row instanceof LabelRow && position > i) {
				LabelRow rowAdded = (LabelRow) this.rows.get(i);
				size += diseaseGroupLabels.getGroupSize(rowAdded.label);
			}
			i++;
		}
		return size;
	}

	//Returns position of a view after adjusting for groups. **Grouped views are treated as one.**
	private int getAdjustedPosition(int position) {
		return position - getPositionDifference(position);
	}

}
