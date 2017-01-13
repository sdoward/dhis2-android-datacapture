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

		List<Integer> columnLevels = new ArrayList<>();

		List<FieldGroupColumn> columns = fieldGroup.getColumns();
		while(columns.size() > 0){
			columnLevels.add(columns.size());
			columns = columns.get(0).getChildren();
		}

		int[] columnSpan = new int[columnLevels.size()];

		for(int i=0;i<columnSpan.length;i++){
			columnSpan[i] = 1;
			for(int j=i+1;j<columnSpan.length;j++){
				columnSpan[i] *= columnLevels.get(j);
			}
		}

		int rowWidth = columnSize;
		for(Integer i : columnLevels)
			rowWidth *= i;

		Random random = new Random();

		for(int i=0;i<columnLevels.size();i++){
			LinearLayout row = new LinearLayout(getContext());
			row.setLayoutParams(new LayoutParams(rowWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
			row.setOrientation(HORIZONTAL);

			int nLabelsAtLevel = columnLevels.get(i);
			for(int j=i-1;j>=0;j--)
				nLabelsAtLevel *= columnLevels.get(j);

			int labelWidth = columnSpan[i] * columnSize;

			for(int j=0;j<nLabelsAtLevel;j++){
				TextView label = new TextView(getContext());
				label.setLayoutParams(new LayoutParams(labelWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
				label.setText("X");
				label.setGravity(Gravity.CENTER);
				label.setBackgroundColor(0xff000000 + random.nextInt() % 0xffffff);
				row.addView(label);
			}
			addView(row);
		}

	}

	public void setLabels(FieldGroup fieldGroup){

	}
}
