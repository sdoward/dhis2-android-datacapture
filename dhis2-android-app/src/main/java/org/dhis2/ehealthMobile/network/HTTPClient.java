/*
 * Copyright (c) 2014, Araz Abishov
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package org.dhis2.ehealthMobile.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import org.dhis2.ehealthMobile.R;
import org.dhis2.ehealthMobile.utils.PrefUtils;

import android.content.Context;
import android.util.Log;


public class HTTPClient implements IHttpClient{

	private static final int CONNECTION_TIME_OUT = 1500;

	private static HTTPClient instance;

	public static HTTPClient getInstance(Context context){
		if(instance == null)
			instance = new HTTPClient(context);

		return instance;
	}

	private String baseUrl;
	private String credentials;

	public HTTPClient(Context context) {
		baseUrl = PrefUtils.getServerURL(context);
		credentials = PrefUtils.getCredentials(context);
	}

	public void setCredentials(String credentials){
		this.credentials = credentials;
	}

	public void setBaseUrl(String baseUrl){
		this.baseUrl = baseUrl;
	}

	@Override
	public Response getDatasetValues(String formId, String orgUnitId, String period, String categoryOptions) {
		String url = baseUrl
				+ URLConstants.DATASET_VALUES_URL + "/" + formId + "/"
				+ URLConstants.FORM_PARAM + orgUnitId
				+ URLConstants.PERIOD_PARAM + period;

		if (categoryOptions != null)
			url = url + URLConstants.CATEGORY_OPTIONS_PARAM + categoryOptions;

		return get(url);
	}

	@Override
	public Response postProfileInfo(String accountInfo) {
		return post(baseUrl + URLConstants.API_USER_ACCOUNT_URL, accountInfo);
	}

	@Override
	public Response postDataset(String data) {
		return post(baseUrl + URLConstants.DATASET_UPLOAD_URL, data);
	}

	@Override
	public Response getConfigFile(){
		return get(baseUrl + URLConstants.DATA_STORE + "/" + URLConstants.CONFIG_URL);
	}

	@Override
	public Response getOptionSets(String id) {
		return get(baseUrl + URLConstants.OPTION_SET_URL + "/" + id + URLConstants.OPTION_SET_PARAM);
	}

	@Override
	public Response getDatasets() {
		return get(baseUrl + URLConstants.DATASETS_URL);
	}

	@Override
	public Response getProfileInfo() {
		return get(baseUrl + URLConstants.API_USER_ACCOUNT_URL);
	}

	@Override
	public Response postMyProfile(String accountInfo) {
		return post(baseUrl + URLConstants.API_USER_ACCOUNT_URL, accountInfo);
	}

	@Override
	public Response getSmsNumber() {
		return get(baseUrl + URLConstants.DATA_STORE + "/" + URLConstants.SMS_NUMBER_URL);
	}

	@Override
	public Response getSubmissionDetails(String formId, String orgUnitId, String period) {
		String url = baseUrl + URLConstants.DATASET_UPLOAD_URL+ "?"+URLConstants.DATASET_PARAM + formId
				+ "&" + URLConstants.ORG_UNIT_PARAM + orgUnitId + URLConstants.PERIOD_PARAM_2 + period
				+ "&" + URLConstants.LIMIT_PARAM+ "0";

		return get(url);
	}

	@Override
	public Response loginUser() {
		return get(baseUrl + URLConstants.API_USER_ACCOUNT_URL);
	}


	private Response get(String url) {
		return executeRequest(url, credentials, "GET", null);
	}


	private Response post(String url, String data) {
		return executeRequest(url, credentials, "POST", data);
	}


	private Response executeRequest(String server, String creds, String method, String data) {

		Log.d(HTTPClient.class.getSimpleName(), String.format("executeRequest() %s %s", method, server));

		HttpURLConnection connection = null;
		int code = -1;
		String body = "";

		try {
			URL url = new URL(server);
			connection = (HttpURLConnection) url.openConnection();
			connection.setInstanceFollowRedirects(false);
			connection.setConnectTimeout(CONNECTION_TIME_OUT);
			connection.setRequestProperty("Authorization", "Basic " + creds);
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestMethod(method);
			connection.setDoInput(true);

			if(data != null){
				connection.setDoOutput(true);
				connection.setRequestProperty("Content-Type", "application/json");
				OutputStream output = connection.getOutputStream();
				output.write(data.getBytes());
				output.close();
			}

			connection.connect();
			code = connection.getResponseCode();
			body = readInputStream(connection.getInputStream());
		} catch (MalformedURLException e) {
			code = HttpURLConnection.HTTP_NOT_FOUND;
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			code = HttpURLConnection.HTTP_NOT_FOUND;
		} catch (IOException one) {
			one.printStackTrace();
			try {
				if (connection != null) {
					code = connection.getResponseCode();
				}
			} catch (IOException two) {
				two.printStackTrace();
			}
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}

		Log.d(HTTPClient.class.getSimpleName(), String.format("return response (%s): %s", code, body));
		return new Response(code, body);
	}


	private static String readInputStream(InputStream stream)
			throws IOException {
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(stream));
		try {
			StringBuilder builder = new StringBuilder();
			String line;

			while ((line = reader.readLine()) != null) {
				builder.append(line);
				builder.append('\n');
			}

			return builder.toString();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static boolean isError(int code) {
		return code != HttpURLConnection.HTTP_OK;
	}

	public static String getErrorMessage(Context context, int code) {
		switch (code) {
			case HttpURLConnection.HTTP_UNAUTHORIZED:
				return context.getString(R.string.wrong_username_password);
			case HttpURLConnection.HTTP_NOT_FOUND:
				return context.getString(R.string.wrong_url);
			case HttpURLConnection.HTTP_MOVED_PERM:
				return context.getString(R.string.wrong_url);
			default:
				return context.getString(R.string.try_again);
		}
	}

}
