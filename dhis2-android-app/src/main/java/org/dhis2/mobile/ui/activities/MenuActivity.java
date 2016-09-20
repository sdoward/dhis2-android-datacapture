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

package org.dhis2.mobile.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.dhis2.mobile.R;
import org.dhis2.mobile.WorkService;
import org.dhis2.mobile.io.handlers.UserAccountHandler;
import org.dhis2.mobile.ui.fragments.AggregateReportFragment;
import org.dhis2.mobile.ui.fragments.MyProfileFragment;
import org.dhis2.mobile.utils.LetterAvatar;
import org.dhis2.mobile.utils.PrefUtils;
import org.dhis2.mobile.utils.TextFileUtils;

public class MenuActivity extends BaseActivity implements OnNavigationItemSelectedListener {
    private static final String STATE_TOOLBAR_TITLE = "state:toolbarTitle";

    // layout
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null) {
            navigationView.inflateMenu(R.menu.menu_drawer);
            navigationView.setNavigationItemSelectedListener(this);
            setupUserDetails();

        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_menu);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toggleNavigationDrawer();
                }
            });
        }

        if (savedInstanceState == null) {
            onNavigationItemSelected(navigationView.getMenu()
                    .findItem(R.id.drawer_item_aggregate_report));
        } else if (savedInstanceState.containsKey(STATE_TOOLBAR_TITLE) && toolbar != null) {
            toolbar.setTitle(savedInstanceState.getString(STATE_TOOLBAR_TITLE));
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.drawer_item_aggregate_report: {
                attachFragment(new AggregateReportFragment());
                break;
            }
            case R.id.drawer_item_profile: {
                attachFragment(new MyProfileFragment());
                break;
            }
            case R.id.drawer_item_logout: {
                logOut();
                break;
            }
        }

        toolbar.setTitle(item.getTitle());
        navigationView.setCheckedItem(R.id.drawer_item_aggregate_report);
        drawerLayout.closeDrawers();

        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (toolbar != null) {
            outState.putString(STATE_TOOLBAR_TITLE, toolbar.getTitle().toString());
        }

        super.onSaveInstanceState(outState);
    }

    protected void attachFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    protected void toggleNavigationDrawer() {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawer(navigationView);
        } else {
            drawerLayout.openDrawer(navigationView);
        }
    }

    private void logOut() {
        // start service in order to remove data
        Intent removeDataIntent = new Intent(MenuActivity.this, WorkService.class);
        removeDataIntent.putExtra(WorkService.METHOD, WorkService.METHOD_REMOVE_ALL_DATA);
        startService(removeDataIntent);

        // start LoginActivity
        Intent startLoginActivity = new Intent(MenuActivity.this, LoginActivity.class);
        startActivity(startLoginActivity);
        overridePendingTransition(R.anim.activity_close_enter, R.anim.activity_close_exit);
        finish();
    }

    private void setupUserDetails(){
        View header = navigationView.getHeaderView(0);
        TextView username = (TextView) header.findViewById(R.id.side_nav_username);
        username.setText(PrefUtils.getUserName(getApplicationContext()));
        String source = TextFileUtils.readTextFile(getApplicationContext(),
                TextFileUtils.Directory.ROOT,
                TextFileUtils.FileNames.ACCOUNT_INFO);
        String initials = username.getText().toString().substring(0,1);
        if(source != null){
            JsonObject info = (new JsonParser()).parse(source).getAsJsonObject();
            String email = UserAccountHandler.getString(info.getAsJsonPrimitive(UserAccountHandler.EMAIL));
            TextView mEmail = (TextView) header.findViewById(R.id.side_nav_email);
            mEmail.setText(email);
            initials = UserAccountHandler.getString(info.getAsJsonPrimitive(UserAccountHandler.FIRST_NAME)).substring(0,1).toUpperCase() +
                    UserAccountHandler.getString(info.getAsJsonPrimitive(UserAccountHandler.SURNAME)).substring(0,1).toUpperCase();

        }
        ImageView avatar = (ImageView) header.findViewById(R.id.side_nav_photo);
        Bitmap bm = Bitmap.createBitmap(100, 100,Bitmap.Config.ARGB_8888); Canvas cv = new Canvas(bm);
        LetterAvatar ava = new LetterAvatar(getApplicationContext(), Color.parseColor("#FF0000"), initials, 10 );
        ava.draw(cv);
        avatar.setImageBitmap(bm);

    }
}
