package org.dhis2.ehealthMobile.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import org.dhis2.ehealthMobile.R;

/**
 * Created by george on 12/2/16.
 */

public class AppPermissions {

    public interface AppPermissionsCallback{
        void onPermissionGranted(String permission);
        void onPermissionDenied(String permission);
    }
    public static final int MY_PERMISSIONS = 1;
    public static String[] requiredPermissions = new String[]{Manifest.permission.SEND_SMS};

    public AppPermissions(){

    }

    public int checkPermission(Context context, String permission){
        return  ContextCompat.checkSelfPermission(context,
                permission);
    }

    public boolean isPermissionGranted(Context context, String permission){
        return PackageManager.PERMISSION_GRANTED == checkPermission(context, permission);
    }

    public void showPermissionRationaleDialog(final Activity activity, String permission){
        permission = permission.split("\\.")[permission.split("\\.").length-1];
        String title = activity.getString(R.string.sms_permission_dialog_title);
        String message = activity.getString(R.string.sms_permission_dialog_message, permission);
        String confirmationText = activity.getString(R.string.sms_permission_dialog_confirmation);

        AlertDialog dialog = new AlertDialog.Builder(activity).create();
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, confirmationText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermission(activity);
            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void requestPermission(Activity activity ) {
            ActivityCompat.requestPermissions(activity,requiredPermissions,
                    MY_PERMISSIONS);
    }

    public void handleRequestResults(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, AppPermissionsCallback callback){
        switch (requestCode){
            case AppPermissions.MY_PERMISSIONS:{
                int index = 0;
                for(String permission: permissions){
                    if(permission.equals(Manifest.permission.SEND_SMS)){
                        if (grantResults.length > 0
                                && grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                            //Permission granted ヽ(´▽`)/
                            callback.onPermissionGranted(permission);
                        } else {
                            // permission denied, ¯\_(⊙︿⊙)_/¯
                            callback.onPermissionDenied(permission);
                        }
                    }
                    index++;
                }
            }
        }
    }

    public boolean canShowRationale(Activity activity, String permission){
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }

}
