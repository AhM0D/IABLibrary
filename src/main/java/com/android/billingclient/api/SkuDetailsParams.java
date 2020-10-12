package com.android.billingclient.api;

import java.util.ArrayList;
import java.util.List;

public class SkuDetailsParams {
    /* access modifiers changed from: private */
    public String mSkuType;
    /* access modifiers changed from: private */
    public List<String> mSkusList;

    public String getSkuType() {
        return this.mSkuType;
    }

    public List<String> getSkusList() {
        return this.mSkusList;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private String mSkuType;
        private List<String> mSkusList;

        private Builder() {
        }

        public Builder setSkusList(List<String> skusList) {
            this.mSkusList = new ArrayList(skusList);
            return this;
        }

        public Builder setType(String type) {
            this.mSkuType = type;
            return this;
        }

        public SkuDetailsParams build() {
            SkuDetailsParams params = new SkuDetailsParams();
            String unused = params.mSkuType = this.mSkuType;
            List unused2 = params.mSkusList = this.mSkusList;
            return params;
        }
    }
}
