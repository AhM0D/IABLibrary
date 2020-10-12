package com.android.billingclient.api;

import java.util.Collections;
import java.util.List;

class BillingClientNativeCallback implements AcknowledgePurchaseResponseListener, BillingClientStateListener, ConsumeResponseListener, PriceChangeConfirmationListener, PurchaseHistoryResponseListener, PurchasesUpdatedListener, RewardResponseListener, SkuDetailsResponseListener {
    private final long futureHandle;

    public static native void nativeOnAcknowledgePurchaseResponse(int i, String str, long j);

    public static native void nativeOnBillingServiceDisconnected();

    public static native void nativeOnBillingSetupFinished(int i, String str, long j);

    public static native void nativeOnConsumePurchaseResponse(int i, String str, String str2, long j);

    public static native void nativeOnPriceChangeConfirmationResult(int i, String str, long j);

    public static native void nativeOnPurchaseHistoryResponse(int i, String str, PurchaseHistoryRecord[] purchaseHistoryRecordArr, long j);

    public static native void nativeOnPurchasesUpdated(int i, String str, Purchase[] purchaseArr);

    public static native void nativeOnQueryPurchasesResponse(int i, String str, Purchase[] purchaseArr, long j);

    public static native void nativeOnRewardResponse(int i, String str, long j);

    public static native void nativeOnSkuDetailsResponse(int i, String str, SkuDetails[] skuDetailsArr, long j);

    BillingClientNativeCallback() {
        this.futureHandle = 0;
    }

    BillingClientNativeCallback(long futureHandle2) {
        this.futureHandle = futureHandle2;
    }

    public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
        if (skuDetailsList == null) {
            skuDetailsList = Collections.emptyList();
        }
        nativeOnSkuDetailsResponse(billingResult.getResponseCode(), billingResult.getDebugMessage(), (SkuDetails[]) skuDetailsList.toArray(new SkuDetails[skuDetailsList.size()]), this.futureHandle);
    }

    public void onAcknowledgePurchaseResponse(BillingResult billingResult) {
        nativeOnAcknowledgePurchaseResponse(billingResult.getResponseCode(), billingResult.getDebugMessage(), this.futureHandle);
    }

    public void onBillingSetupFinished(BillingResult billingResult) {
        nativeOnBillingSetupFinished(billingResult.getResponseCode(), billingResult.getDebugMessage(), this.futureHandle);
    }

    public void onBillingServiceDisconnected() {
        nativeOnBillingServiceDisconnected();
    }

    public void onConsumeResponse(BillingResult billingResult, String purchaseToken) {
        nativeOnConsumePurchaseResponse(billingResult.getResponseCode(), billingResult.getDebugMessage(), purchaseToken, this.futureHandle);
    }

    public void onPriceChangeConfirmationResult(BillingResult billingResult) {
        nativeOnPriceChangeConfirmationResult(billingResult.getResponseCode(), billingResult.getDebugMessage(), this.futureHandle);
    }

    public void onPurchaseHistoryResponse(BillingResult billingResult, List<PurchaseHistoryRecord> purchaseHistoryRecordList) {
        if (purchaseHistoryRecordList == null) {
            purchaseHistoryRecordList = Collections.emptyList();
        }
        nativeOnPurchaseHistoryResponse(billingResult.getResponseCode(), billingResult.getDebugMessage(), (PurchaseHistoryRecord[]) purchaseHistoryRecordList.toArray(new PurchaseHistoryRecord[purchaseHistoryRecordList.size()]), this.futureHandle);
    }

    public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
        if (purchases == null) {
            purchases = Collections.emptyList();
        }
        nativeOnPurchasesUpdated(billingResult.getResponseCode(), billingResult.getDebugMessage(), (Purchase[]) purchases.toArray(new Purchase[purchases.size()]));
    }

    public void onRewardResponse(BillingResult billingResult) {
        nativeOnRewardResponse(billingResult.getResponseCode(), billingResult.getDebugMessage(), this.futureHandle);
    }

    /* access modifiers changed from: package-private */
    public void onQueryPurchasesResponse(BillingResult billingResult, List<Purchase> purchases) {
        if (purchases == null) {
            purchases = Collections.emptyList();
        }
        nativeOnQueryPurchasesResponse(billingResult.getResponseCode(), billingResult.getDebugMessage(), (Purchase[]) purchases.toArray(new Purchase[purchases.size()]), this.futureHandle);
    }
}
