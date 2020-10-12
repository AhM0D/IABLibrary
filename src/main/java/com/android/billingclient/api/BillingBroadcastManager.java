package com.android.billingclient.api;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.NonNull;

import com.android.billingclient.util.BillingHelper;

class BillingBroadcastManager {
    private static final String ACTION = "com.android.vending.billing.PURCHASES_UPDATED";
    private static final String TAG = "BillingBroadcastManager";
    private final Context mContext;
    /* access modifiers changed from: private */
    public final BillingBroadcastReceiver mReceiver;

    BillingBroadcastManager(Context context, @NonNull PurchasesUpdatedListener listener) {
        this.mContext = context;
        this.mReceiver = new BillingBroadcastReceiver(listener);
    }

    /* access modifiers changed from: package-private */
    public void registerReceiver() {
        this.mReceiver.register(this.mContext, new IntentFilter(ACTION));
    }

    /* access modifiers changed from: package-private */
    public PurchasesUpdatedListener getListener() {
        return this.mReceiver.mListener;
    }

    /* access modifiers changed from: package-private */
    public void destroy() {
        this.mReceiver.unRegister(this.mContext);
    }

    private class BillingBroadcastReceiver extends BroadcastReceiver {
        private boolean mIsRegistered;
        /* access modifiers changed from: private */
        public final PurchasesUpdatedListener mListener;

        private BillingBroadcastReceiver(@NonNull PurchasesUpdatedListener listener) {
            this.mListener = listener;
        }

        public void register(Context context, IntentFilter filer) {
            if (!this.mIsRegistered) {
                context.registerReceiver(BillingBroadcastManager.this.mReceiver, filer);
                this.mIsRegistered = true;
            }
        }

        public void unRegister(Context context) {
            if (this.mIsRegistered) {
                context.unregisterReceiver(BillingBroadcastManager.this.mReceiver);
                this.mIsRegistered = false;
                return;
            }
            BillingHelper.logWarn(BillingBroadcastManager.TAG, "Receiver is not registered.");
        }

        public void onReceive(Context context, Intent intent) {
            this.mListener.onPurchasesUpdated(BillingHelper.getBillingResultFromIntent(intent, BillingBroadcastManager.TAG), BillingHelper.extractPurchases(intent.getExtras()));
        }
    }
}
