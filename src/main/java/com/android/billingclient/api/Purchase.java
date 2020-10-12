package com.android.billingclient.api;


import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.android.billingclient.util.BillingHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

public class Purchase {
    private String mOriginalJson;
    private final JSONObject mParsedJson = new JSONObject(this.mOriginalJson);
    private final String mSignature;

    @Retention(RetentionPolicy.SOURCE)
    public @interface PurchaseState {
        public static final int PENDING = 2;
        public static final int PURCHASED = 1;
        public static final int UNSPECIFIED_STATE = 0;
    }

    public Purchase(String jsonPurchaseInfo, String signature) throws JSONException {
        this.mOriginalJson = jsonPurchaseInfo;
        this.mSignature = signature;
    }

    public String getOrderId() {
        return this.mParsedJson.optString("orderId");
    }

    public String getPackageName() {
        return this.mParsedJson.optString("packageName");
    }

    public String getSku() {
        return this.mParsedJson.optString("productId");
    }

    public long getPurchaseTime() {
        return this.mParsedJson.optLong("purchaseTime");
    }

    public String getPurchaseToken() {
        return this.mParsedJson.optString("token", this.mParsedJson.optString("purchaseToken"));
    }

    public int getPurchaseState() {
        switch (this.mParsedJson.optInt("purchaseState", 1)) {
            case 4:
                return 2;
            default:
                return 1;
        }
    }

    @Nullable
    public String getDeveloperPayload() {
        return this.mParsedJson.optString(BillingHelper.EXTRA_PARAMS_DEVELOPER_PAYLOAD);
    }

    public boolean isAcknowledged() {
        return this.mParsedJson.optBoolean("acknowledged", true);
    }

    public boolean isAutoRenewing() {
        return this.mParsedJson.optBoolean("autoRenewing");
    }

    public String getOriginalJson() {
        return this.mOriginalJson;
    }

    public String getSignature() {
        return this.mSignature;
    }

    public String toString() {
        return "Purchase. Json: " + this.mOriginalJson;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Purchase)) {
            return false;
        }
        Purchase purchase = (Purchase) o;
        if (!TextUtils.equals(this.mOriginalJson, purchase.getOriginalJson()) || !TextUtils.equals(this.mSignature, purchase.getSignature())) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.mOriginalJson.hashCode();
    }

    public static class PurchasesResult {
        private BillingResult mBillingResult;
        private List<Purchase> mPurchaseList;

        public PurchasesResult(BillingResult mBillingResult2, List<Purchase> purchasesList) {
            this.mPurchaseList = purchasesList;
            this.mBillingResult = mBillingResult2;
        }

        public BillingResult getBillingResult() {
            return this.mBillingResult;
        }

        public int getResponseCode() {
            return getBillingResult().getResponseCode();
        }

        public List<Purchase> getPurchasesList() {
            return this.mPurchaseList;
        }
    }
}
