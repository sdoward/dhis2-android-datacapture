package org.dhis2.ehealthMobile.network;

public interface IHttpClient {

	void setBaseUrl(String url);
	void setCredentials(String credentials);

	Response getSubmissionDetails(String formId, String orgUnitId, String period);
	Response getSmsNumber();
	Response getProfileInfo();
	Response getDatasets();
	Response getOptionSets(String id);
	Response getConfigFile();
	Response getDatasetValues(String formId, String orgUnitId, String period, String categoryOptions);
	Response loginUser();

	Response postMyProfile(String accountInfo);
	Response postDataset(String data);
	Response postProfileInfo(String accountInfo);



}
