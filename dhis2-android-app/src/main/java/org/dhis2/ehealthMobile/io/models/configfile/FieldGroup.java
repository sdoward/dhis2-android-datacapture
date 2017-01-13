package org.dhis2.ehealthMobile.io.models.configfile;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

public class FieldGroup {

	protected int id;

	@SerializedName("columns")
	private List<FieldGroupColumn> columns;

	@SerializedName("label")
	private String label;

	@SerializedName("fields")
	protected List<String> fieldsId;

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
}
