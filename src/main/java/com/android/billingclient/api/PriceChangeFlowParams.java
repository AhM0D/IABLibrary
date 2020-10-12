package com.android.billingclient.api;

import org.json.JSONException;

public class PriceChangeFlowParams {
    /* access modifiers changed from: private */
    public SkuDetails skuDetails;

    public SkuDetails getSkuDetails() {
        return this.skuDetails;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private SkuDetails skuDetails;

        public Builder setSkuDetails(SkuDetails skuDetails2) {
            this.skuDetails = skuDetails2;
            return this;
        }

        private Builder setSkuDetails(String originalSkuDetails) {
            try {
                this.skuDetails = new SkuDetails(originalSkuDetails);
                return this;
            } catch (JSONException e) {
                throw new RuntimeException("Incorrect skuDetails JSON object!");
            }
        }

        public PriceChangeFlowParams build() {
            PriceChangeFlowParams params = new PriceChangeFlowParams();
            if (this.skuDetails != null) {
                SkuDetails unused = params.skuDetails = this.skuDetails;
                return params;
            }
            throw new RuntimeException("SkuDetails must be set");
        }
    }
}
