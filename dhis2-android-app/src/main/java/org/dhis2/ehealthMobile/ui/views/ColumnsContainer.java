package org.dhis2.ehealthMobile.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.dhis2.ehealthMobile.R;
import org.dhis2.ehealthMobile.io.models.configfile.FieldGroup;
import org.dhis2.ehealthMobile.io.models.configfile.FieldGroupColumn;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ColumnsContainer extends LinearLayout {

	private int columnSize;

	public ColumnsContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOrientation(VERTICAL);
		columnSize = context.getResources().getDimensionPixelSize(R.dimen.min_column_size);
	}

	public void makeColumns(FieldGroup fieldGroup){
		removeAllViews();

		Integer[] columnLevels = fieldGroup.getColumnLevels();
		String[][] columnLabels = fieldGroup.getColumnLabels();

		int[] columnSpan = new int[columnLevels.length];

		for(int i=0;i<columnSpan.length;i++){
			columnSpan[i] = 1;
			for(int j=i+1;j<columnSpan.length;j++){
				columnSpan[i] *= columnLevels[j];
			}
		}

		int rowWidth = columnSize;
		for(Integer i : columnLevels)
			rowWidth *= i;

		for(int i=0;i<columnLevels.length;i++){
			LinearLayout row = new LinearLayout(getContext());
			row.setLayoutParams(new LayoutParams(rowWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
			row.setOrientation(HORIZONTAL);

			int labelWidth = columnSpan[i] * columnSize;
			String[] labelRow = columnLabels[i];

			for(int j=0;j<labelRow.length;j++){
				TextView label = new TextView(getContext());
				label.setLayoutParams(new LayoutParams(labelWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
				label.setGravity(Gravity.CENTER);

				// a visual help that might help debugging
				// TODO: remove when production ready
				label.setText("X");
				label.setBackgroundColor(0xff000000 + new Random().nextInt() % 0xffffff);

				row.addView(label);
			}
			addView(row);
		}

	}

	public void setLabels(FieldGroup fieldGroup){

		String[][] labels = fieldGroup.getColumnLabels();

		for(int i=0;i<labels.length;i++){
			ViewGroup row = (ViewGroup) getChildAt(i);
			String[] labelRow = labels[i];

			for (int j = 0; j < labelRow.length; j++) {
				TextView textView = (TextView) row.getChildAt(j);
				textView.setText(labelRow[j]);
			}
		}
	}
}
