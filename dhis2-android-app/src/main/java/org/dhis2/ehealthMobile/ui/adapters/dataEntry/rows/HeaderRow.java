package org.dhis2.ehealthMobile.ui.adapters.dataEntry.rows;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.dhis2.ehealthMobile.R;
import org.dhis2.ehealthMobile.io.models.configfile.FieldGroup;
import org.dhis2.ehealthMobile.io.models.configfile.FieldGroupColumn;
import org.dhis2.ehealthMobile.ui.views.ColumnsContainer;

import java.util.List;

public class HeaderRow extends Row{

	private FieldGroup fieldGroup;
	private int viewType;


	public HeaderRow(FieldGroup fieldGroup, LayoutInflater inflater) {
		super(null, inflater);
		this.fieldGroup = fieldGroup;
	}

	@Override
	public View getView(int position, View convertView) {

		/* assuming that each level of FieldGroupColumn has the same amount of children
		 ok
		 | HEADER LABEL |   A   |   B   |
		 |              | 1 | 2 | 3 | 4 |

		 will break
		 | HEADER LABEL |     A     |   B   |
		 |              | 1 | 2 | 3 | 4 | 5 |
		*/

		View view;
		HeaderRowHolder holder;

		if(convertView == null) {
			view = getInflater().inflate(R.layout.listview_row_header, null);

			holder = new HeaderRowHolder(view);
			view.setTag(holder);
			holder.columnsContainer.makeColumns(fieldGroup);
		}else {
			view = convertView;
			holder = (HeaderRowHolder) view.getTag();
		}

		holder.textView.setText(fieldGroup.getLabel());
		holder.columnsContainer.setLabels(fieldGroup);


		return view;
	}

	@Override
	public int getViewType() {

		if(viewType == 0) {
			StringBuilder builder = new StringBuilder();
			builder.append(String.valueOf(FieldType.values().length));

			List<FieldGroupColumn> columns = fieldGroup.getColumns();
			builder.append(String.valueOf(columns.size()));


			while (columns.size() > 0) {
				columns = columns.get(0).getChildren();
				builder.append(String.valueOf(columns.size()));
			}

			viewType = Integer.valueOf(builder.toString());
		}

		return viewType;
	}

	private static class HeaderRowHolder {
		final TextView textView;
		final ColumnsContainer columnsContainer;

		public HeaderRowHolder(View view){
			textView = (TextView) view.findViewById(R.id.text_view);
			columnsContainer = (ColumnsContainer) view.findViewById(R.id.columns_container);
		}
	}
}
