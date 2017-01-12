package org.dhis2.ehealthmobile;

import android.view.View;

import org.dhis2.ehealthMobile.io.models.Field;
import org.dhis2.ehealthMobile.io.models.configfile.FieldGroup;
import org.dhis2.ehealthMobile.ui.adapters.dataEntry.FieldAdapter;
import org.dhis2.ehealthMobile.ui.adapters.dataEntry.rows.Row;
import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;


public class FieldAdapterTest {

	private static class DummyRow extends Row {

		public DummyRow(Field field) {
			super(field, null);
		}

		@Override
		public View getView(int position, View convertView) {
			return null;
		}

		@Override
		public int getViewType() {
			return 0;
		}

		@Override
		public String toString() {
			return "DummyRow with field "+ getFieldId();
		}
	}

	private static class DummyFieldGroup extends FieldGroup {

		public DummyFieldGroup(int id, String...fieldIds){
			this.id = id;
			this.fieldsId = Arrays.asList(fieldIds);
		}
	}

	private static class DummyField extends Field {
		public DummyField(String dataElement, String label){
			this.dataElement = dataElement;
			this.label = label;
		}
	}

	@Test
	public void testAddFieldGroupsToRows(){

		FieldGroup[] fieldGroups = new FieldGroup[]{
			new DummyFieldGroup(0, "1", "2", "3"),
			new DummyFieldGroup(1, "4", "5"),
		};

		Row[] rows = new Row[]{
			new DummyRow(new DummyField("5", "")),
			new DummyRow(new DummyField("4", "")),
			new DummyRow(new DummyField("3", "")),
			new DummyRow(new DummyField("2", "")),
			new DummyRow(new DummyField("1", "")),
			new DummyRow(new DummyField("nope", "")),
			new DummyRow(new DummyField(null, "")),
		};

		FieldAdapter.addFieldGroupToRows(Arrays.asList(fieldGroups), Arrays.asList(rows));

		assertThat(rows[0].getFieldGroup()).isEqualTo(fieldGroups[1]);
		assertThat(rows[1].getFieldGroup()).isEqualTo(fieldGroups[1]);
		assertThat(rows[2].getFieldGroup()).isEqualTo(fieldGroups[0]);
		assertThat(rows[3].getFieldGroup()).isEqualTo(fieldGroups[0]);
		assertThat(rows[4].getFieldGroup()).isEqualTo(fieldGroups[0]);
		assertThat(rows[5].getFieldGroup()).isNull();
		assertThat(rows[6].getFieldGroup()).isNull();
	}


	@Test
	public void testRowComparator(){

		FieldGroup[] fieldGroups = new FieldGroup[]{
				new DummyFieldGroup(0, "1", "2", "3"),
				new DummyFieldGroup(1, "4", "5"),
		};

		Row[] rows = new Row[]{
				new DummyRow(new DummyField("5", "e")),
				new DummyRow(new DummyField("4", "d")),
				new DummyRow(new DummyField("3", "c")),
				new DummyRow(new DummyField("2", "b")),
				new DummyRow(new DummyField("1", "a")),
				new DummyRow(new DummyField("nope", "h")),
				new DummyRow(new DummyField(null, "f")),
				new DummyRow(new DummyField(null, "g")),
		};

		FieldAdapter.addFieldGroupToRows(Arrays.asList(fieldGroups), Arrays.asList(rows));

		Arrays.sort(rows, FieldAdapter.ROW_COMPARATOR);

		assertThat(rows[0].getFieldId()).isEqualTo("1");
		assertThat(rows[1].getFieldId()).isEqualTo("2");
		assertThat(rows[2].getFieldId()).isEqualTo("3");
		assertThat(rows[3].getFieldId()).isEqualTo("4");
		assertThat(rows[4].getFieldId()).isEqualTo("5");
		assertThat(rows[5].getFieldId()).isNull();
		assertThat(rows[6].getFieldId()).isNull();
		assertThat(rows[7].getFieldId()).isEqualTo("nope");


	}
}
