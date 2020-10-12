package com.android.billingclient.api;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.ResultReceiver;
import com.android.billingclient.util.BillingHelper;

public class ProxyBillingActivity extends Activity {
    static final String KEY_RESULT_RECEIVER = "result_receiver";
    private static final int REQUEST_CODE_LAUNCH_ACTIVITY = 100;
    private static final String TAG = "ProxyBillingActivity";
    private ResultReceiver mResultReceiver;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            BillingHelper.logVerbose(TAG, "Launching Play Store billing flow");
            this.mResultReceiver = (ResultReceiver) getIntent().getParcelableExtra(KEY_RESULT_RECEIVER);
            PendingIntent pendingIntent = null;
            if (getIntent().hasExtra(BillingHelper.RESPONSE_BUY_INTENT_KEY)) {
                pendingIntent = (PendingIntent) getIntent().getParcelableExtra(BillingHelper.RESPONSE_BUY_INTENT_KEY);
            } else if (getIntent().hasExtra(BillingHelper.RESPONSE_SUBS_MANAGEMENT_INTENT_KEY)) {
                pendingIntent = (PendingIntent) getIntent().getParcelableExtra(BillingHelper.RESPONSE_SUBS_MANAGEMENT_INTENT_KEY);
            }
            try {
                startIntentSenderForResult(pendingIntent.getIntentSender(), REQUEST_CODE_LAUNCH_ACTIVITY, new Intent(), 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                BillingHelper.logWarn(TAG, "Got exception while trying to start a purchase flow: " + e);
                this.mResultReceiver.send(6, (Bundle) null);
                finish();
            }
        } else {
            BillingHelper.logVerbose(TAG, "Launching Play Store billing flow from savedInstanceState");
            this.mResultReceiver = (ResultReceiver) savedInstanceState.getParcelable(KEY_RESULT_RECEIVER);
        }
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(KEY_RESULT_RECEIVER, this.mResultReceiver);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_LAUNCH_ACTIVITY) {
            int responseCode = BillingHelper.getResponseCodeFromIntent(data, TAG);
            if (!(resultCode == -1 && responseCode == 0)) {
                BillingHelper.logWarn(TAG, "Activity finished with resultCode " + resultCode + " and billing's responseCode: " + responseCode);
            }
            this.mResultReceiver.send(responseCode, data == null ? null : data.getExtras());
        } else {
            BillingHelper.logWarn(TAG, "Got onActivityResult with wrong requestCode: " + requestCode + "; skipping...");
        }
        finish();
    }
}
