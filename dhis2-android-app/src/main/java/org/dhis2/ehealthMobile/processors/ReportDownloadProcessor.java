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
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.google.gson.JsonObject;

import org.dhis2.ehealthMobile.io.holders.DatasetInfoHolder;
import org.dhis2.ehealthMobile.io.json.JsonHandler;
import org.dhis2.ehealthMobile.io.json.ParsingException;
import org.dhis2.ehealthMobile.io.models.CategoryOption;
import org.dhis2.ehealthMobile.io.models.Form;
import org.dhis2.ehealthMobile.network.HTTPClient;
import org.dhis2.ehealthMobile.network.Response;
import org.dhis2.ehealthMobile.network.URLConstants;
import org.dhis2.ehealthMobile.ui.activities.DataEntryActivity;
import org.dhis2.ehealthMobile.utils.FormUtils;
import org.dhis2.ehealthMobile.utils.PrefUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Processes previous forms that has been sent for a particular Dataset
 */
public class ReportDownloadProcessor {

    private ReportDownloadProcessor() {
    }

    /**
     * Downloads forms that have been filled out from previous weeks or periods
     * @param context Context
     * @param info DatasetInfoHolder
     * @see DatasetInfoHolder
     */
    public static void download(Context context, DatasetInfoHolder info) {
        String url = buildUrl(context, info);
        String creds = PrefUtils.getCredentials(context);
        Response response = HTTPClient.get(url, creds);

        Form form = null;
        if (response.getCode() >= 200 && response.getCode() < 300) {
            form = parseForm(response.getBody());
            if(FormUtils.shouldBeSquashed(context, info.getFormId())){
                form = FormUtils.squashFormGroups(form);
            }
        }

        Intent intent = new Intent(DataEntryActivity.TAG);
        intent.putExtra(Response.CODE, response.getCode());

        if (form != null) {
            intent.putExtra(Response.BODY, (Parcelable) form);
        }

        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private static String buildUrl(Context context, DatasetInfoHolder info) {
        String server = PrefUtils.getServerURL(context);
        String categoryOptions = buildCategoryOptionsString(info);
        String url = server
                + URLConstants.DATASET_VALUES_URL + "/" + info.getFormId() + "/"
                + URLConstants.FORM_PARAM + info.getOrgUnitId()
                + URLConstants.PERIOD_PARAM + info.getPeriod();
        if (categoryOptions != null) {
            url = url + URLConstants.CATEGORY_OPTIONS_PARAM + categoryOptions;
        }

        return url;
    }

    private static String buildCategoryOptionsString(DatasetInfoHolder info) {
        List<String> categoryOptions = new ArrayList<>();

        // extracting uids
        if (info.getCategoryOptions() != null && !info.getCategoryOptions().isEmpty()) {
            for (CategoryOption categoryOption : info.getCategoryOptions()) {
                categoryOptions.add(categoryOption.getId());
            }
        }

        if (!categoryOptions.isEmpty()) {
            return "[" + TextUtils.join(",", categoryOptions) + "]";
        }

        return null;
    }

    private static Form parseForm(String responseBody) {
        if (responseBody != null) {
            try {
                JsonObject jsonForm = JsonHandler.buildJsonObject(responseBody);
                return JsonHandler.fromJson(jsonForm, Form.class);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (ParsingException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

}
