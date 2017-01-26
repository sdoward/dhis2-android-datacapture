package org.dhis2.ehealthMobile.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.dhis2.ehealthMobile.R;
import org.dhis2.ehealthMobile.utils.NotificationBuilder;

public class NotificationActivity extends AppCompatActivity {

    private final String success = "success";
    private View contentLayoutView;
    private ImageView icon;
    private TextView titleTextView;
    private TextView mContentView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notification);

        contentLayoutView = findViewById(R.id.notification_content_layout);
        icon = (ImageView) findViewById(R.id.notification_icon);
        titleTextView = (TextView) findViewById(R.id.fullscreen_title);
        mContentView = (TextView) findViewById(R.id.fullscreen_content);


        findViewById(R.id.notification_confirmation_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NotificationActivity.this, LauncherActivity.class));
            }
        });

        if(getIntent().hasExtra(NotificationBuilder.NOTIFICATION_TITLE)){
            String title = getIntent().getStringExtra(NotificationBuilder.NOTIFICATION_TITLE);
            titleTextView.setText(title);
            setNotificationIcon(title, icon);
        }
        if(getIntent().hasExtra(NotificationBuilder.NOTIFICATION_MESSAGE)){
            String message = getIntent().getStringExtra(NotificationBuilder.NOTIFICATION_MESSAGE);
            mContentView.setText(message);
        }

        goFullscreen(contentLayoutView);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @SuppressLint("InlinedApi")
    private void goFullscreen(View view){
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
    }

    private void setNotificationIcon(String title, ImageView icon){
        Drawable drawable;
        if(title.contains(success)){
            drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_check_circle_white_48dp);
        }else{
            drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_error_outline_white_48dp);
        }
        icon.setImageDrawable(drawable);
    }
}
