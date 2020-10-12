package com.android.billingclient.api;


import androidx.annotation.Nullable;

public final class AcknowledgePurchaseParams {
    /* access modifiers changed from: private */
    public String developerPayload;
    /* access modifiers changed from: private */
    public String purchaseToken;

    private AcknowledgePurchaseParams() {
    }

    @Nullable
    public String getDeveloperPayload() {
        return this.developerPayload;
    }

    public String getPurchaseToken() {
        return this.purchaseToken;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String developerPayload;
        private String purchaseToken;

        private Builder() {
        }

        public Builder setPurchaseToken(String purchaseToken2) {
            this.purchaseToken = purchaseToken2;
            return this;
        }

        public Builder setDeveloperPayload(String developerPayload2) {
            this.developerPayload = developerPayload2;
            return this;
        }

        public AcknowledgePurchaseParams build() {
            AcknowledgePurchaseParams params = new AcknowledgePurchaseParams();
            String unused = params.developerPayload = this.developerPayload;
            String unused2 = params.purchaseToken = this.purchaseToken;
            return params;
        }
    }
}
