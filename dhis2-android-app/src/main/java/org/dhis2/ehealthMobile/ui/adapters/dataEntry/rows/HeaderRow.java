package org.dhis2.ehealthMobile.ui.adapters.dataEntry.rows;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.dhis2.ehealthMobile.R;

public class HeaderRow extends Row{

	private String label;

	public HeaderRow(String label, LayoutInflater inflater) {
		super(null, inflater);
		this.label = label;
	}

	@Override
	public View getView(int position, View convertView) {

		View view;
		HeaderRowHolder holder;

		if(convertView == null) {
			view = getInflater().inflate(R.layout.listview_row_header, null);

			holder = new HeaderRowHolder(view);
			view.setTag(holder);

		}else {
			view = convertView;
			holder = (HeaderRowHolder) view.getTag();
		}

		holder.textView.setText(label);

		return view;
	}

	@Override
	public int getViewType() {
		return FieldType.HEADER.ordinal();
	}

	private static class HeaderRowHolder {
		public final TextView textView;

		public HeaderRowHolder(View view){
			textView = (TextView) view.findViewById(R.id.text_view);
		}
	}
}
