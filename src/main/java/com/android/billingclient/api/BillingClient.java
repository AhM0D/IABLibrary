package com.android.billingclient.api;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public abstract class BillingClient {

    @Retention(SOURCE)
    public @interface BillingResponseCode {
        /** The request has reached the maximum timeout before Google Play responds. */
        int SERVICE_TIMEOUT = -3;
        /** Requested feature is not supported by Play Store on the current device. */
        int FEATURE_NOT_SUPPORTED = -2;
        /**
         * Play Store service is not connected now - potentially transient state.
         *
         * <p>E.g. Play Store could have been updated in the background while your app was still
         * running. So feel free to introduce your retry policy for such use case. It should lead to a
         * call to {@link #startConnection} right after or in some time after you received this code.
         */
        int SERVICE_DISCONNECTED = -1;
        /** Success */
        int OK = 0;
        /** User pressed back or canceled a dialog */
        int USER_CANCELED = 1;
        /** Network connection is down */
        int SERVICE_UNAVAILABLE = 2;
        /** Billing API version is not supported for the type requested */
        int BILLING_UNAVAILABLE = 3;
        /** Requested product is not available for purchase */
        int ITEM_UNAVAILABLE = 4;
        /**
         * Invalid arguments provided to the API. This error can also indicate that the application was
         * not correctly signed or properly set up for In-app Billing in Google Play, or does not have
         * the necessary permissions in its manifest
         */
        int DEVELOPER_ERROR = 5;
        /** Fatal error during the API action */
        int ERROR = 6;
        /** Failure to purchase since item is already owned */
        int ITEM_ALREADY_OWNED = 7;
        /** Failure to consume since item is not owned */
        int ITEM_NOT_OWNED = 8;
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ChildDirected {
        public static final int CHILD_DIRECTED = 1;
        public static final int NOT_CHILD_DIRECTED = 2;
        public static final int UNSPECIFIED = 0;
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface FeatureType {
        public static final String IN_APP_ITEMS_ON_VR = "inAppItemsOnVr";
        public static final String PRICE_CHANGE_CONFIRMATION = "priceChangeConfirmation";
        public static final String SUBSCRIPTIONS = "subscriptions";
        public static final String SUBSCRIPTIONS_ON_VR = "subscriptionsOnVr";
        public static final String SUBSCRIPTIONS_UPDATE = "subscriptionsUpdate";
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SkuType {
        public static final String INAPP = "inapp";
        public static final String SUBS = "subs";
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface UnderAgeOfConsent {
        public static final int NOT_UNDER_AGE_OF_CONSENT = 2;
        public static final int UNDER_AGE_OF_CONSENT = 1;
        public static final int UNSPECIFIED = 0;
    }

    public abstract void acknowledgePurchase(AcknowledgePurchaseParams acknowledgePurchaseParams, AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener);

    public abstract void consumeAsync(ConsumeParams consumeParams, @NonNull ConsumeResponseListener consumeResponseListener);

    @UiThread
    public abstract void endConnection();

    @UiThread
    public abstract BillingResult isFeatureSupported(String str);

    @UiThread
    public abstract boolean isReady();

    @UiThread
    public abstract BillingResult launchBillingFlow(Activity activity, BillingFlowParams billingFlowParams);

    @UiThread
    public abstract void launchPriceChangeConfirmationFlow(Activity activity, PriceChangeFlowParams priceChangeFlowParams, @NonNull PriceChangeConfirmationListener priceChangeConfirmationListener);

    public abstract void loadRewardedSku(RewardLoadParams rewardLoadParams, @NonNull RewardResponseListener rewardResponseListener);

    public abstract void queryPurchaseHistoryAsync(String str, @NonNull PurchaseHistoryResponseListener purchaseHistoryResponseListener);

    public abstract Purchase.PurchasesResult queryPurchases(String str);

    public abstract void querySkuDetailsAsync(SkuDetailsParams skuDetailsParams, @NonNull SkuDetailsResponseListener skuDetailsResponseListener);

    @UiThread
    public abstract void startConnection(@NonNull BillingClientStateListener billingClientStateListener);

    public static final class Builder {
        private int mChildDirected;
        private final Context mContext;
        private boolean mEnablePendingPurchases;
        private PurchasesUpdatedListener mListener;
        private int mUnderAgeOfConsent;

        private Builder(Context context) {
            this.mChildDirected = 0;
            this.mUnderAgeOfConsent = 0;
            this.mContext = context;
        }

        @UiThread
        public Builder setListener(PurchasesUpdatedListener listener) {
            this.mListener = listener;
            return this;
        }

        @UiThread
        public Builder setChildDirected(int childDirected) {
            this.mChildDirected = childDirected;
            return this;
        }

        @UiThread
        public Builder setUnderAgeOfConsent(int underAgeOfConsent) {
            this.mUnderAgeOfConsent = underAgeOfConsent;
            return this;
        }

        @UiThread
        public Builder enablePendingPurchases() {
            this.mEnablePendingPurchases = true;
            return this;
        }

        @UiThread
        public BillingClient build() {
            if (this.mContext == null) {
                throw new IllegalArgumentException("Please provide a valid Context.");
            } else if (this.mListener == null) {
                throw new IllegalArgumentException("Please provide a valid listener for purchases updates.");
            } else if (this.mEnablePendingPurchases) {
                return new BillingClientImpl(this.mContext, this.mChildDirected, this.mUnderAgeOfConsent, this.mEnablePendingPurchases, this.mListener);
            } else {
                throw new IllegalArgumentException("Support for pending purchases must be enabled. Enable this by calling 'enablePendingPurchases()' on BillingClientBuilder.");
            }
        }
    }

    @UiThread
    public static Builder newBuilder(@NonNull Context context) {
        return new Builder(context);
    }
}
