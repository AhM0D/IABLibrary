package com.android.billingclient.api;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class SkuDetails {
    private String mOriginalJson;
    private JSONObject mParsedJson;

    public SkuDetails(String jsonSkuDetails) throws JSONException {
        mParsedJson = new JSONObject(jsonSkuDetails);
        this.mOriginalJson = jsonSkuDetails;
    }

    public String getOriginalJson() {
        return this.mOriginalJson;
    }

    public String getSku() {
        return this.mParsedJson.optString("productId");
    }

    public String getType() {
        return this.mParsedJson.optString("type");
    }

    public String getPrice() {
        return this.mParsedJson.optString("price");
    }

    public long getPriceAmountMicros() {
        return 0L;
    }

    public String getPriceCurrencyCode() {
        return "";
    }

    public String getOriginalPrice() {
        return "";
    }

    public long getOriginalPriceAmountMicros() {
        return 0L;
    }

    public String getTitle() {
        return this.mParsedJson.optString("title");
    }

    public String getDescription() {
        return this.mParsedJson.optString("description");
    }

    public String getSubscriptionPeriod() {
        return "";
    }

    public String getFreeTrialPeriod() {
        return "";
    }

    public String getIntroductoryPrice() {
        return "";
    }

    public long getIntroductoryPriceAmountMicros() {
        return 0L;
    }

    public String getIntroductoryPricePeriod() {
        return "";
    }

    public String getIntroductoryPriceCycles() {
        return "";
    }

    public String getIconUrl() {
        return "";
    }

    public boolean isRewarded() {
        return false;
    }

    /* access modifiers changed from: package-private */
    public String getSkuDetailsToken() {
        return "";
    }

    /* access modifiers changed from: package-private */
    public String rewardToken() {
        return "";
    }

    public String toString() {
        return "SkuDetails: " + this.mOriginalJson;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return TextUtils.equals(this.mOriginalJson, ((SkuDetails) o).mOriginalJson);
    }

    public int hashCode() {
        return this.mOriginalJson.hashCode();
    }

    public static class SkuDetailsResult {
        private String mDebugMessage;
        private int mResponseCode;
        private List<SkuDetails> mSkuDetailsList;

        public SkuDetailsResult(int responseCode, String debugMessage, List<SkuDetails> skuDetailsList) {
            this.mResponseCode = responseCode;
            this.mDebugMessage = debugMessage;
            this.mSkuDetailsList = skuDetailsList;
        }

        public List<SkuDetails> getSkuDetailsList() {
            return this.mSkuDetailsList;
        }

        public int getResponseCode() {
            return this.mResponseCode;
        }

        public String getDebugMessage() {
            return this.mDebugMessage;
        }
    }
}
