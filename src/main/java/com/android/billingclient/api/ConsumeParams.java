package com.android.billingclient.api;


import androidx.annotation.Nullable;

public final class ConsumeParams {
    /* access modifiers changed from: private */
    public String developerPayload;
    /* access modifiers changed from: private */
    public String purchaseToken;

    private ConsumeParams() {
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

        public ConsumeParams build() {
            ConsumeParams params = new ConsumeParams();
            String unused = params.purchaseToken = this.purchaseToken;
            String unused2 = params.developerPayload = this.developerPayload;
            return params;
        }
    }
}
