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

package org.dhis2.ehealthMobile.processors;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.webkit.URLUtil;

import com.google.gson.Gson;

import org.dhis2.ehealthMobile.io.models.useraccount.UserAccount;
import org.dhis2.ehealthMobile.network.HTTPClient;
import org.dhis2.ehealthMobile.network.IHttpClient;
import org.dhis2.ehealthMobile.network.Response;
import org.dhis2.ehealthMobile.ui.activities.LoginActivity;
import org.dhis2.ehealthMobile.utils.PrefUtils;
import org.dhis2.ehealthMobile.utils.TextFileUtils;

import java.net.HttpURLConnection;

public class LoginProcessor {
    private static final String HTTP = "http://";
    private static final String HTTPS = "https://";

    public static void loginUser(IHttpClient httpClient, Context context, String url,
                                 String creds, String username) {

        if (context == null || url == null
                || creds == null || username == null) {
            Log.i(LoginActivity.TAG, "Login failed");
            return;
        }

        if(!url.startsWith(HTTPS) && !url.startsWith(HTTP))
            url = HTTPS + url;

        httpClient.setCredentials(creds);
        httpClient.setBaseUrl(url);

        Response resp = httpClient.loginUser();

        // Checking validity of server URL
        if (!URLUtil.isValidUrl(url)) {
            Intent result = new Intent(LoginActivity.TAG);
            result.putExtra(Response.CODE, HttpURLConnection.HTTP_NOT_FOUND);
            LocalBroadcastManager.getInstance(context).sendBroadcast(result);
            return;
        }

        // If credentials and address is correct,
        // user information will be saved to internal storage
        if (!HTTPClient.isError(resp.getCode())) {
            PrefUtils.initAppData(context, creds, username, url);

            Gson gson = new Gson();

            UserAccount userAccount = gson.fromJson(resp.getBody(), UserAccount.class);

            TextFileUtils.writeTextFile(context, TextFileUtils.Directory.ROOT,
                    TextFileUtils.FileNames.ACCOUNT_INFO.toString(), gson.toJson(userAccount));
        }

        // Sending result back to activity
        // through Broadcast android API
        Intent result = new Intent(LoginActivity.TAG);
        result.putExtra(Response.CODE, resp.getCode());
        LocalBroadcastManager.getInstance(context).sendBroadcast(result);
    }

}
