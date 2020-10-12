package com.android.billingclient.api;


import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.android.billingclient.util.BillingHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class PurchaseHistoryRecord {
    private String mOriginalJson;
    private final JSONObject mParsedJson = new JSONObject(this.mOriginalJson);
    private final String mSignature;

    public PurchaseHistoryRecord(String jsonPurchaseInfo, String signature) throws JSONException {
        this.mOriginalJson = jsonPurchaseInfo;
        this.mSignature = signature;
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

    @Nullable
    public String getDeveloperPayload() {
        return this.mParsedJson.optString(BillingHelper.EXTRA_PARAMS_DEVELOPER_PAYLOAD);
    }

    public String getOriginalJson() {
        return this.mOriginalJson;
    }

    public String getSignature() {
        return this.mSignature;
    }

    public String toString() {
        return "PurchaseHistoryRecord. Json: " + this.mOriginalJson;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PurchaseHistoryRecord)) {
            return false;
        }
        PurchaseHistoryRecord purchase = (PurchaseHistoryRecord) o;
        if (!TextUtils.equals(this.mOriginalJson, purchase.getOriginalJson()) || !TextUtils.equals(this.mSignature, purchase.getSignature())) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.mOriginalJson.hashCode();
    }
}
