package org.dhis2.ehealthmobile;

import android.content.Context;

import org.dhis2.ehealthMobile.Dhis2App;
import org.dhis2.ehealthMobile.di.DataModule;
import org.dhis2.ehealthMobile.network.IHttpClient;
import org.dhis2.ehealthMobile.network.Response;

import java.net.HttpURLConnection;

public class HttpClientInstrumentationTest extends BaseInstrumentationTest implements IHttpClient {

	private static final Response DEFAULT_RESPONSE = new Response(HttpURLConnection.HTTP_NOT_IMPLEMENTED, "");

	private Response loginUserResponse = DEFAULT_RESPONSE;

	@Override
	public void setup() {
		super.setup();
		Dhis2App.get(getContext())
				.setDataModule(new DataModule(){
					@Override
					public IHttpClient provideHttpClient(Context context) {
						return HttpClientInstrumentationTest.this;
					}
				});
	}

	@Override
	public void setBaseUrl(String url) {

	}

	@Override
	public void setCredentials(String credentials) {

	}

	@Override
	public Response getSubmissionDetails(String formId, String orgUnitId, String period) {
		return DEFAULT_RESPONSE;
	}

	@Override
	public Response getSmsNumber() {
		return DEFAULT_RESPONSE;
	}

	@Override
	public Response getProfileInfo() {
		return DEFAULT_RESPONSE;
	}

	@Override
	public Response getDatasets() {
		return DEFAULT_RESPONSE;
	}

	@Override
	public Response getOptionSets(String id) {
		return DEFAULT_RESPONSE;
	}

	@Override
	public Response getConfigFile() {
		return DEFAULT_RESPONSE;
	}

	@Override
	public Response getDatasetValues(String formId, String orgUnitId, String period, String categoryOptions) {
		return DEFAULT_RESPONSE;
	}

	protected void setLoginUserResponse(int code, String body){
		this.loginUserResponse = new Response(code, body);
	}

	@Override
	public Response loginUser() {
		return loginUserResponse;
	}

	@Override
	public Response postMyProfile(String accountInfo) {
		return DEFAULT_RESPONSE;
	}

	@Override
	public Response postDataset(String data) {
		return DEFAULT_RESPONSE;
	}

	@Override
	public Response postProfileInfo(String accountInfo) {
		return DEFAULT_RESPONSE;
	}
}
