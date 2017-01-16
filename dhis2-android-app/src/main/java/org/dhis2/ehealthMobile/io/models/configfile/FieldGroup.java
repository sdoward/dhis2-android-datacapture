package org.dhis2.ehealthMobile.io.models.configfile;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FieldGroup {

	public interface ColumnInterator{
		void visitColumn(FieldGroupColumn column, int depth, int index, int parentIndex);
	}

	protected int id;

	@SerializedName("columns")
	private List<FieldGroupColumn> columns;

	@SerializedName("label")
	private String label;

	@SerializedName("fields")
	protected List<String> fieldsId;

	private Integer[] columnLevels;
	private String[][] columnLabels;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public String getLabel(){
		return label;
	}

	public List<FieldGroupColumn> getColumns(){
		if(columns == null)
			return Collections.EMPTY_LIST;

		return columns;
	}

	public boolean hasFieldId(String fieldId){
		if(fieldId == null || fieldsId == null)
			return false;

		for(String id : fieldsId)
			if(fieldId.equals(id))
				return true;

		return false;
	}

	public Integer[] getColumnLevels(){
		if(columnLevels == null){

			List<FieldGroupColumn> columns = this.columns;
			List<Integer> levelsList = new ArrayList<>();

			while(columns.size() > 0){
				levelsList.add(columns.size());
				columns = columns.get(0).getChildren();
			}

			columnLevels = new Integer[levelsList.size()];
			levelsList.toArray(columnLevels);
		}

		return columnLevels;
	}

	public String[][] getColumnLabels(){

		if(columnLabels == null){
			final Integer[] levels = getColumnLevels();
			columnLabels = new String[levels.length][];

			for(int i=0;i<levels.length;i++){
				int nLabelsAtLevel = columnLevels[i];
				for (int j = i-1; j >= 0; j--)
					nLabelsAtLevel *= levels[j];

				columnLabels[i] = new String[nLabelsAtLevel];

				for (int j = 0; j < nLabelsAtLevel; j++) {
					columnLabels[i][j] = String.format("%s-%s", i, j/* % columnLevels[i]*/);
				}
			}

			visitColumns(new ColumnInterator() {
				@Override
				public void visitColumn(FieldGroupColumn column, int depth, int index, int parentIndex) {
					Log.d("FieldGroupRecursion", String.format("visit column depth %s index %s parentIndex %s", depth, index, parentIndex));

					int fullIndex = index;
					if(parentIndex >0)
						fullIndex += parentIndex * levels[depth];

					columnLabels[depth][fullIndex] = column.getLabel();
				}
			});
		}

		return columnLabels;
	}

	private void visitColumns(ColumnInterator iterator){
		for (int i = 0; i < columns.size(); i++)
			visitColumn(columns.get(i), 0, i, -1, iterator);

	}

	private void visitColumn(FieldGroupColumn column, int depth, int index, int parentIndex, ColumnInterator iterator){

		iterator.visitColumn(column, depth, index, parentIndex);

		List<FieldGroupColumn> children = column.getChildren();
		if(children == null) return;

		depth++;

		for (int i = 0; i < children.size(); i++) {
			FieldGroupColumn child = children.get(i);
			visitColumn(child, depth, i, index, iterator);
		}
	}
}
