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

package org.dhis2.ehealthMobile.ui.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.dhis2.ehealthMobile.BuildConfig;
import org.dhis2.ehealthMobile.R;
import org.dhis2.ehealthMobile.WorkService;
import org.dhis2.ehealthMobile.network.HTTPClient;
import org.dhis2.ehealthMobile.network.NetworkUtils;
import org.dhis2.ehealthMobile.network.Response;
import org.dhis2.ehealthMobile.utils.AppPermissions;
import org.dhis2.ehealthMobile.utils.ToastManager;
import org.dhis2.ehealthMobile.utils.ViewUtils;

import hu.supercluster.paperwork.Paperwork;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = LoginActivity.class.getSimpleName();
    public static final String USERNAME = "username";
    public static final String SERVER = "server";
    public static final String CREDENTIALS = "creds";
    private static final String DEV_URL = "devUrl";
    private static final String PROD_URL = "prodUrl";

    private Button mLoginButton;
    private EditText mUsername;
    private EditText mPassword;
    private ImageView mDhis2Logo;
    private CardView mLoginCardView;

    private ProgressBar mProgressBar;
    private Paperwork config;


    // BroadcastReceiver which aim is to listen
    // for network response on login post request
    BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int code = intent.getExtras().getInt(Response.CODE);

            // If response code is 200, then MenuActivity is started
            // If not, user is notified with error message
            if (!HTTPClient.isError(code)) {
                Intent menuActivity = new Intent(LoginActivity.this, MenuActivity.class);
                startActivity(menuActivity);
                overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit);
                finish();
            } else {
                hideProgress();
                String message = HTTPClient.getErrorMessage(LoginActivity.this, code);
                showMessage(message);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mDhis2Logo = (ImageView) findViewById(R.id.dhis2_logo);
        mLoginCardView = (CardView) findViewById(R.id.login_card_view);
        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        mLoginButton = (Button) findViewById(R.id.login_button);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.GONE);

        config = new Paperwork(this);

        // textwatcher is responsible for watching
        // after changes in all fields
        final TextWatcher textWatcher = new TextWatcher() {

            @Override
            public void afterTextChanged(Editable edit) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                checkEditTextFields();
            }
        };

        mUsername.addTextChangedListener(textWatcher);
        mPassword.addTextChangedListener(textWatcher);

        // Call method in order to check the fields
        // and change state of login button
        checkEditTextFields();

        mLoginButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        if(!AppPermissions.isPermissionGranted(getApplicationContext(), Manifest.permission.SEND_SMS)){
            AppPermissions.requestPermission(this);
        }

        // Restoring state of activity from saved bundle
        if (savedInstanceState != null) {
            boolean loginInProcess = savedInstanceState.getBoolean(TAG, false);

            if (loginInProcess) {
                showProgress();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Registering BroadcastReceiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter(TAG));

    }

    @Override
    public void onPause() {

        // Unregistering BroadcastReceiver in
        // onPause() in order to prevent leaks
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Saving state of activity
        if (mProgressBar != null) {
            outState.putBoolean(TAG, mProgressBar.isShown());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        AppPermissions.handleRequestResults(requestCode, permissions, grantResults, new AppPermissions.AppPermissionsCallback() {
            @Override
            public void onPermissionGranted(String permission) {}

            @Override
            public void onPermissionDenied(String permission) {
                AppPermissions.showPermissionRationaleDialog(LoginActivity.this, permission);
            }
        });
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // Activates *login button*,
    // if all necessary fields are full
    private void checkEditTextFields() {
        String tempUrl = getServerUrl();
        String tempUsername = mUsername.getText().toString();
        String tempPassword = mPassword.getText().toString();

        if (tempUrl.equals("") || tempUsername.equals("") || tempPassword.equals("")) {
            mLoginButton.setEnabled(false);
        } else {
            mLoginButton.setEnabled(true);
        }
    }

    // loginUser() is called when user clicks *LoginButton*
    private void loginUser() {
        String tmpServer = getServerUrl();
        String user = mUsername.getText().toString();
        String pass = mPassword.getText().toString();
        String pair = String.format("%s:%s", user, pass);

        if (NetworkUtils.checkConnection(LoginActivity.this)) {
            showProgress();

            String server = tmpServer + (tmpServer.endsWith("/") ? "" : "/");
            String creds = Base64.encodeToString(pair.getBytes(), Base64.NO_WRAP);

            // Preparing data to be sent to WorkService
            Intent intent = new Intent(LoginActivity.this, WorkService.class);
            intent.putExtra(WorkService.METHOD, WorkService.METHOD_LOGIN_USER);
            intent.putExtra(SERVER, server);
            intent.putExtra(USERNAME, user);
            intent.putExtra(CREDENTIALS, creds);

            // Starting WorkService
            startService(intent);
        } else {
            showMessage(getString(R.string.check_connection));
        }
    }

    private void showMessage(String message) {
        ToastManager.makeToast(this, message, Toast.LENGTH_LONG).show();
    }

    private void showProgress() {
        ViewUtils.perfomOutAnimation(this, R.anim.out_up, true,
                mDhis2Logo, mLoginCardView, mUsername, mPassword, mLoginButton);
        ViewUtils.enableViews(mProgressBar);
    }

    private void hideProgress() {
        ViewUtils.perfomInAnimation(this, R.anim.in_down,
                mDhis2Logo, mLoginCardView, mUsername, mPassword, mLoginButton);
        ViewUtils.hideAndDisableViews(mProgressBar);
    }

    private String getServerUrl(){
        return (BuildConfig.DEBUG) ? config.get(DEV_URL) : config.get(PROD_URL);
    }
}
