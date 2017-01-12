package org.dhis2.ehealthMobile.io.models.configfile;

import com.google.gson.annotations.SerializedName;

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

	public boolean hasFieldId(String fieldId){
		if(fieldId == null || fieldsId == null)
			return false;

		for(String id : fieldsId)
			if(fieldId.equals(id))
				return true;

		return false;
	}
}
