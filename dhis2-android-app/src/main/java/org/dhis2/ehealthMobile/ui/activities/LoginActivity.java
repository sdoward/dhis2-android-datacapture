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
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.dhis2.ehealthMobile.R;
import org.dhis2.ehealthMobile.WorkService;
import org.dhis2.ehealthMobile.network.HTTPClient;
import org.dhis2.ehealthMobile.network.NetworkUtils;
import org.dhis2.ehealthMobile.network.Response;
import org.dhis2.ehealthMobile.utils.AppPermissions;
import org.dhis2.ehealthMobile.utils.ToastManager;
import org.dhis2.ehealthMobile.utils.ViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

	public static final String TAG = LoginActivity.class.getSimpleName();
	public static final String USERNAME = "usernameEditText";
	public static final String SERVER = "server";
	public static final String CREDENTIALS = "creds";


	@BindView(R.id.url_edit_text)
	public EditText urlEditText;

	@BindView(R.id.username_edit_text)
	public EditText usernameEditText;

	@BindView(R.id.password_edit_text)
	public EditText passwordEditText;

	@BindView(R.id.login_button)
	public Button loginButton;

	@BindView(R.id.dhis2_logo)
	public ImageView logoImageView;

	@BindView(R.id.progress_bar)
	public ProgressBar progressBar;


	final TextWatcher loginCredentialsTextWatcher = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable edit) {
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			checkLoginButtonEnabled();
		}
	};

	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

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
		ButterKnife.bind(this);

		progressBar.setVisibility(View.GONE);

		urlEditText.addTextChangedListener(loginCredentialsTextWatcher);
		usernameEditText.addTextChangedListener(loginCredentialsTextWatcher);
		passwordEditText.addTextChangedListener(loginCredentialsTextWatcher);

//		urlEditText.setText("https://dhis2-sl-dev.ehealthafrica.org");
//		usernameEditText.setText("admin");
//		passwordEditText.setText("JdGKpmGvewkwFkL00uteU0SI6vv4IFln");

		checkLoginButtonEnabled();

		if (!AppPermissions.isPermissionGranted(getApplicationContext(), Manifest.permission.SEND_SMS)) {
			AppPermissions.requestPermission(this);
		}

		// Restoring state of activity from saved bundle
		if (savedInstanceState != null) {
			boolean loginInProcess = savedInstanceState.getBoolean(TAG, false);

			if (loginInProcess) {
				ViewUtils.hideAndDisableViews(logoImageView, urlEditText, usernameEditText, passwordEditText, loginButton);
				//ViewUtils.hideAndDisableViews(logoImageView, usernameEditText, passwordEditText, loginButton);
				showProgress();
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(TAG));

	}

	@Override
	public void onPause() {
		super.onPause();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		if (progressBar != null) {
			outState.putBoolean(TAG, progressBar.isShown());
		}
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		AppPermissions.handleRequestResults(requestCode, permissions, grantResults, new AppPermissions.AppPermissionsCallback() {
			@Override
			public void onPermissionGranted(String permission) {
			}

			@Override
			public void onPermissionDenied(String permission) {
				AppPermissions.showPermissionRationaleDialog(LoginActivity.this, permission);
			}
		});
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}


	private void checkLoginButtonEnabled() {
		loginButton.setEnabled(urlEditText.length() > 0 && usernameEditText.length() > 0 && passwordEditText.length() > 0);
	}

	@OnClick(R.id.login_button)
	public void loginUser() {
		String tmpServer = urlEditText.getText().toString();

		String user = usernameEditText.getText().toString();
		String pass = passwordEditText.getText().toString();
		String pair = String.format("%s:%s", user, pass);

		if (NetworkUtils.checkConnection(LoginActivity.this)) {
			showProgress();

			String server = tmpServer + (tmpServer.endsWith("/") ? "" : "/");
			String creds = Base64.encodeToString(pair.getBytes(), Base64.NO_WRAP);

			Intent intent = new Intent(LoginActivity.this, WorkService.class);
			intent.putExtra(WorkService.METHOD, WorkService.METHOD_LOGIN_USER);
			intent.putExtra(SERVER, server);
			intent.putExtra(USERNAME, user);
			intent.putExtra(CREDENTIALS, creds);

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
				logoImageView, urlEditText, usernameEditText, passwordEditText, loginButton);
		ViewUtils.enableViews(progressBar);
	}

	private void hideProgress() {
		ViewUtils.perfomInAnimation(this, R.anim.in_down,
				logoImageView, urlEditText, usernameEditText, passwordEditText, loginButton);
		ViewUtils.hideAndDisableViews(progressBar);
	}

}
