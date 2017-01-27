package org.dhis2.ehealthMobile.io.models.configfile;

import com.google.gson.annotations.SerializedName;

public class ConfigFile {

	// TODO: isolate IDSR specific code
	@SerializedName("IDSR Weekly Diseaase Report(WDR)")
	public IDSRWeeklyDiseaseReport weeklyDiseaseReport;
}
