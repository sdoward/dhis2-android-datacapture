package org.dhis2.ehealthMobile.io.models.configfile;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

public class FieldGroupColumn {

	@SerializedName("label")
	private String label;

	@SerializedName("columns")
	private List<FieldGroupColumn> columns;

	public String getLabel(){
		return label;
	}

	public List<FieldGroupColumn> getChildren(){

		if(columns == null)
			return Collections.EMPTY_LIST;

		return columns;
	}
}
