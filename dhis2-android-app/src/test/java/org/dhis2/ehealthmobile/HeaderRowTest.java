package org.dhis2.ehealthmobile;

import android.view.LayoutInflater;
import android.view.View;

import com.google.gson.Gson;

import org.dhis2.ehealthMobile.io.models.configfile.FieldGroup;
import org.dhis2.ehealthMobile.ui.adapters.dataEntry.rows.FieldType;
import org.dhis2.ehealthMobile.ui.adapters.dataEntry.rows.HeaderRow;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HeaderRowTest extends BaseRoboelectricTest{

	Gson gson = new Gson();

	String columnWithLabelsJson = "{\n" +
			"\t\"columns\" : [\n" +
			"\t\t{\t\n" +
			"\t\t\t\"label\": \"label0\",\n" +
			"\t\t\t\"columns\": [\n" +
			"\t\t\t\t{\"label\": \"label00\"},\n" +
			"\t\t\t\t{\"label\": \"label01\"}\n" +
			"\t\t\t]\n" +
			"\t\t},\n" +
			"\t\t{\n" +
			"\t\t\t\"label\": \"label1\",\n" +
			"\t\t\t\"columns\": [\n" +
			"\t\t\t\t{\"label\": \"label10\"},\n" +
			"\t\t\t\t{\"label\": \"label11\"}\n" +
			"\t\t\t]\n" +
			"\t\t}\n" +
			"\t]\t\n" +
			"}";
/*
{
	"columns" : [
		{
			"label": "label0",
			"columns": [
				{"label": "label00"},
				{"label": "label01"}
			]
		},
		{
			"label": "label1",
			"columns": [
				{"label": "label10"},
				{"label": "label11"}
			]
		}
	]
}
 */
	@Test
	public void headerRowViewShouldHaveAllColumnLabels(){
		FieldGroup fieldGroup = gson.fromJson(columnWithLabelsJson, FieldGroup.class);
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



		String jsonString =
"{\n" +
	"\"columns\": [" +
		"{" +
			"\"columns\": [{},{}]" +
		"}," +
		"{" +
			"\"columns\": [{},{}]" +
		"}" +
	"]" +
"}";
		fieldGroup = gson.fromJson(jsonString, FieldGroup.class);
		headerRow = new HeaderRow(fieldGroup, null);
		assertThat(headerRow.getViewType()).isEqualTo(
				Integer.valueOf(String.format("%s%s%s%s", FieldType.values().length, 2, 2, 0))
		);


		jsonString =
"{\n" +
	"\"columns\": [" +
		"{" +
			"\"columns\": [{},{}, {}]" +
		"}" +
	"]" +
"}";
		fieldGroup = gson.fromJson(jsonString, FieldGroup.class);
		headerRow = new HeaderRow(fieldGroup, null);
		assertThat(headerRow.getViewType()).isEqualTo(
				Integer.valueOf(String.format("%s%s%s%s", FieldType.values().length, 1, 3, 0))
		);


		// broken layout, for now throws no exception
		jsonString =
"{\n" +
	"\"columns\": [" +
		"{" +
			"\"columns\": [{},{}, {}]" +
		"}," +
		"{" +
			"\"columns\": [{}]" +
		"}," +
		"{" +
			"\"columns\": [{}]" +
		"}" +
	"]" +
"}";
		fieldGroup = gson.fromJson(jsonString, FieldGroup.class);
		headerRow = new HeaderRow(fieldGroup, null);
		assertThat(headerRow.getViewType()).isEqualTo(
				Integer.valueOf(String.format("%s%s%s%s", FieldType.values().length, 3, 3, 0))
		);
	}


}
