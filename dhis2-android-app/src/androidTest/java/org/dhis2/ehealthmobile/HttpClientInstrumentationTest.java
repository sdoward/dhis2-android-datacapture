package org.dhis2.ehealthmobile;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.dhis2.ehealthMobile.Dhis2App;
import org.dhis2.ehealthMobile.di.DataModule;
import org.dhis2.ehealthMobile.network.IHttpClient;
import org.dhis2.ehealthMobile.network.Response;

import java.net.HttpURLConnection;

public abstract class HttpClientInstrumentationTest extends BaseInstrumentationTest implements IHttpClient {

	private static final Response DEFAULT_RESPONSE = new Response(HttpURLConnection.HTTP_OK, "{}");

	private Response loginUserResponse = DEFAULT_RESPONSE;
	private Response getSmsNumberResponse = DEFAULT_RESPONSE;

	@Override
	public void setup() {
		super.setup();
		Dhis2App.get(InstrumentationRegistry.getTargetContext())
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

	public void setSmsNumber(String number){
		getSmsNumberResponse = new Response(HttpURLConnection.HTTP_OK, String.format("{\"smsNumber\": \"%s\"}", number));
	}

	@Override
	public Response getSmsNumber() {
		return getSmsNumberResponse;
	}

	@Override
	public Response getProfileInfo() {
		return DEFAULT_RESPONSE;
	}

	@Override
	public Response getDatasets() {
		return new Response(HttpURLConnection.HTTP_OK, loadJson("api_me_assignedDataSets"));
	}

	@Override
	public Response getOptionSets(String id) {
		return new Response(HttpURLConnection.HTTP_OK, loadJson("api_optionSets_"+id));
	}

	@Override
	public Response getConfigFile() {
		return new Response(HttpURLConnection.HTTP_OK, loadJson("api_dataStore_android_config"));
	}

	@Override
	public Response getDatasetValues(String formId, String orgUnitId, String period, String categoryOptions) {
		return new Response(HttpURLConnection.HTTP_OK, loadJson("api_dataSets_rq0LNr72Ndo_form"));
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
