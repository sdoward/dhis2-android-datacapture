package org.dhis2.ehealthmobile;

import android.view.LayoutInflater;
import android.view.View;

import com.google.gson.Gson;

import org.dhis2.ehealthMobile.R;
import org.dhis2.ehealthMobile.io.models.configfile.FieldGroup;
import org.dhis2.ehealthMobile.ui.adapters.dataEntry.rows.FieldType;
import org.dhis2.ehealthMobile.ui.adapters.dataEntry.rows.HeaderRow;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HeaderRowTest extends BaseRoboelectricTest{

	@Test
	public void headerRowViewShouldHaveAllColumnLabels(){
		FieldGroup fieldGroup = loadJson("columns_with_labels", FieldGroup.class);
		HeaderRow headerRow = new HeaderRow(fieldGroup, LayoutInflater.from(getContext()));

		View headerRowView = headerRow.getView(0, null);

		String[] labels = new String[]{
			"label0", "label1", "label00", "label01", "label10", "label11"
		};

		for(String label : labels)
			assertThat(textViewWithText(headerRowView, label)).isNotNull();

	}

	@Test
	public void viewTypeShouldBeDefinedAccordingToColumnsTree(){

		FieldGroup fieldGroup = new FieldGroup();
		HeaderRow headerRow = new HeaderRow(fieldGroup, null);
		assertThat(headerRow.getViewType()).isEqualTo(FieldType.values().length * 10);


		fieldGroup = loadJson("columns_2_2", FieldGroup.class);
		headerRow = new HeaderRow(fieldGroup, null);
		assertThat(headerRow.getViewType()).isEqualTo(
				Integer.valueOf(String.format("%s%s%s%s", FieldType.values().length, 2, 2, 0))
		);


		fieldGroup = loadJson("columns_1_3", FieldGroup.class);
		headerRow = new HeaderRow(fieldGroup, null);
		assertThat(headerRow.getViewType()).isEqualTo(
				Integer.valueOf(String.format("%s%s%s%s", FieldType.values().length, 1, 3, 0))
		);


		// broken layout, for now throws no exception
		fieldGroup = loadJson("columns_broken", FieldGroup.class);
		headerRow = new HeaderRow(fieldGroup, null);
		assertThat(headerRow.getViewType()).isEqualTo(
				Integer.valueOf(String.format("%s%s%s%s", FieldType.values().length, 3, 3, 0))
		);
	}


}
