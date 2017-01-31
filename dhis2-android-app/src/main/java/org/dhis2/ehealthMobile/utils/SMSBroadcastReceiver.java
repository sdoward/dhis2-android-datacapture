package org.dhis2.ehealthMobile.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import org.dhis2.ehealthMobile.processors.SendSmsProcessor;

public class SMSBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = SMSBroadcastReceiver.class.getSimpleName();
    private static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    public static final String DELIVERED_SMS_ACTION = "org.dhis2.mobile.DELIVERED_SMS";
    public static final String SEND_SMS_ACTION = "org.dhis2.mobile.SEND_SMS";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, intent.getAction());
        if(intent.getAction().equals(SEND_SMS_ACTION)) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Log.i(TAG, "Result Okay");
                    PrefUtils.saveSMSStatus(context, intent.getExtras().getString(SendSmsProcessor.SMS_KEY), "Sent");
                    makeToast(context, "SMS Sent!");
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    Log.i(TAG, "SMS Error");
                    makeToast(context, "SMS Error");
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    Log.i(TAG, "Result error no service");
                    makeToast(context, "SMS Error. No Service");
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    Log.i(TAG, "Result error null PDU");
                    makeToast(context, "SMS Error. Null PDU");
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    Log.i(TAG, "Result error radio off");
                    makeToast(context, "SMS Error. Radio switched off.");
                    break;
            }
        }

        if(intent.getAction().equals(DELIVERED_SMS_ACTION)){
            switch (getResultCode()){
                case Activity.RESULT_OK:
                    PrefUtils.saveSMSStatus(context, intent.getExtras().getString(SendSmsProcessor.SMS_KEY), "Delivered");
                    Log.i(TAG, "Result Okay");
                    makeToast(context, "SMS Delivered");
                    break;
                case Activity.RESULT_CANCELED:
                    Log.i(TAG, "Result Cancelled");
                    makeToast(context, "SMS delivery failed");
                    break;
            }
        }
        if(intent.getAction().equals(SMS_RECEIVED_ACTION)){
            Log.i(TAG, "SMS RECEIVED");
        }

    }

    private void makeToast(final Context context, final String message){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        });
    }
}
