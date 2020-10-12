package com.android.billingclient.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import org.json.JSONException;

public class BillingFlowParams {
    public static final String EXTRA_PARAM_CHILD_DIRECTED = "childDirected";
    public static final String EXTRA_PARAM_KEY_ACCOUNT_ID = "accountId";
    public static final String EXTRA_PARAM_KEY_DEVELOPER_ID = "developerId";
    public static final String EXTRA_PARAM_KEY_OLD_SKUS = "skusToReplace";
    public static final String EXTRA_PARAM_KEY_REPLACE_SKUS_PRORATION_MODE = "prorationMode";
    public static final String EXTRA_PARAM_KEY_RSKU = "rewardToken";
    public static final String EXTRA_PARAM_KEY_VR = "vr";
    public static final String EXTRA_PARAM_UNDER_AGE_OF_CONSENT = "underAgeOfConsent";
    /* access modifiers changed from: private */
    public String mAccountId;
    /* access modifiers changed from: private */
    public String mDeveloperId;
    /* access modifiers changed from: private */
    public String mOldSku;
    /* access modifiers changed from: private */
    public int mReplaceSkusProrationMode = 0;
    /* access modifiers changed from: private */
    public SkuDetails mSkuDetails;
    /* access modifiers changed from: private */
    public boolean mVrPurchaseFlow;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ProrationMode {
        public static final int DEFERRED = 4;
        public static final int IMMEDIATE_AND_CHARGE_PRORATED_PRICE = 2;
        public static final int IMMEDIATE_WITHOUT_PRORATION = 3;
        public static final int IMMEDIATE_WITH_TIME_PRORATION = 1;
        public static final int UNKNOWN_SUBSCRIPTION_UPGRADE_DOWNGRADE_POLICY = 0;
    }

    public String getSku() {
        if (this.mSkuDetails == null) {
            return null;
        }
        return this.mSkuDetails.getSku();
    }

    public String getSkuType() {
        if (this.mSkuDetails == null) {
            return null;
        }
        return this.mSkuDetails.getType();
    }

    public SkuDetails getSkuDetails() {
        return this.mSkuDetails;
    }

    @Deprecated
    public ArrayList<String> getOldSkus() {
        return new ArrayList<>(Arrays.asList(new String[]{this.mOldSku}));
    }

    public String getOldSku() {
        return this.mOldSku;
    }

    public String getAccountId() {
        return this.mAccountId;
    }

    public boolean getVrPurchaseFlow() {
        return this.mVrPurchaseFlow;
    }

    public int getReplaceSkusProrationMode() {
        return this.mReplaceSkusProrationMode;
    }

    /* access modifiers changed from: package-private */
    public boolean hasExtraParams() {
        return (!this.mVrPurchaseFlow && this.mAccountId == null && this.mDeveloperId == null && this.mReplaceSkusProrationMode == 0) ? false : true;
    }

    public String getDeveloperId() {
        return this.mDeveloperId;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private String mAccountId;
        private String mDeveloperId;
        private String mOldSku;
        private int mReplaceSkusProrationMode;
        private SkuDetails mSkuDetails;
        private boolean mVrPurchaseFlow;

        private Builder() {
            this.mReplaceSkusProrationMode = 0;
        }

        public Builder setSkuDetails(SkuDetails skuDetails) {
            this.mSkuDetails = skuDetails;
            return this;
        }

        private Builder setSkuDetails(String originalSkuDetails) {
            try {
                this.mSkuDetails = new SkuDetails(originalSkuDetails);
                return this;
            } catch (JSONException e) {
                throw new RuntimeException("Incorrect skuDetails JSON object!");
            }
        }

        @Deprecated
        public Builder setOldSkus(ArrayList<String> oldSkus) {
            if (oldSkus != null && oldSkus.size() > 0) {
                this.mOldSku = oldSkus.get(0);
            }
            return this;
        }

        public Builder setOldSku(String oldSku) {
            this.mOldSku = oldSku;
            return this;
        }

        @Deprecated
        public Builder addOldSku(String oldSku) {
            this.mOldSku = oldSku;
            return this;
        }

        public Builder setReplaceSkusProrationMode(int replaceSkusProrationMode) {
            this.mReplaceSkusProrationMode = replaceSkusProrationMode;
            return this;
        }

        public Builder setAccountId(String accountId) {
            this.mAccountId = accountId;
            return this;
        }

        public Builder setVrPurchaseFlow(boolean isVrPurchaseFlow) {
            this.mVrPurchaseFlow = isVrPurchaseFlow;
            return this;
        }

        public Builder setDeveloperId(String developerId) {
            this.mDeveloperId = developerId;
            return this;
        }

        public BillingFlowParams build() {
            BillingFlowParams params = new BillingFlowParams();
            SkuDetails unused = params.mSkuDetails = this.mSkuDetails;
            String unused2 = params.mOldSku = this.mOldSku;
            String unused3 = params.mAccountId = this.mAccountId;
            boolean unused4 = params.mVrPurchaseFlow = this.mVrPurchaseFlow;
            int unused5 = params.mReplaceSkusProrationMode = this.mReplaceSkusProrationMode;
            String unused6 = params.mDeveloperId = this.mDeveloperId;
            return params;
        }
    }
}
