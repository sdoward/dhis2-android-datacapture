package org.dhis2.ehealthMobile.io.models.configfile;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FieldGroup {

	@SerializedName("columns")
	private List<FieldGroupColumn> columns;

	@SerializedName("label")
	private String label;

	@SerializedName("fields")
	private List<String> fieldsId;

}
