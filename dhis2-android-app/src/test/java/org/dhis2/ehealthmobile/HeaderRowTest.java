package org.dhis2.ehealthmobile;

import com.google.gson.Gson;

import org.dhis2.ehealthMobile.io.models.configfile.FieldGroup;
import org.dhis2.ehealthMobile.ui.adapters.dataEntry.rows.FieldType;
import org.dhis2.ehealthMobile.ui.adapters.dataEntry.rows.HeaderRow;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HeaderRowTest {


	@Test
	public void testHeaderRowViewType(){


		FieldGroup fieldGroup = new FieldGroup();//gson.fromJson("{}", FieldGroup.class);

		HeaderRow headerRow = new HeaderRow(fieldGroup, null);
		assertThat(headerRow.getViewType()).isEqualTo(FieldType.values().length * 10);

		Gson gson = new Gson();
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
