package org.dhis2.ehealthMobile.io.models.configfile;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class IDSRWeeklyDiseaseReport {

	@SerializedName("id")
	private String id;

	@SerializedName("shouldBeSquashed")
	private boolean shouldBeSquashed;

	@SerializedName("fieldGroups")
	private List<FieldGroup> fieldGroups;

}
