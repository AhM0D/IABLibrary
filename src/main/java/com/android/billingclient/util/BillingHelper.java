package com.android.billingclient.util;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;


import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.Purchase;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public final class BillingHelper {
    public static final String DEBUG_MESSAGE = "DEBUG_MESSAGE";
    public static final String EXTRA_PARAMS_DEVELOPER_PAYLOAD = "developerPayload";
    public static final String EXTRA_PARAMS_ENABLE_PENDING_PURCHASES = "enablePendingPurchases";
    public static final String EXTRA_PARAM_KEY_SKU_DETAILS_TOKEN = "skuDetailsToken";
    public static final String EXTRA_PARAM_KEY_SUBS_PRICE_CHANGE = "subs_price_change";
    public static final String INAPP_CONTINUATION_TOKEN = "INAPP_CONTINUATION_TOKEN";
    private static final String INTERNAL_ERROR = "An internal error occurred.";
    public static final String LIBRARY_VERSION_KEY = "playBillingLibraryVersion";
    public static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    public static final String RESPONSE_BUY_INTENT_KEY = "BUY_INTENT";
    public static final String RESPONSE_CODE = "RESPONSE_CODE";
    public static final String RESPONSE_GET_SKU_DETAILS_LIST = "DETAILS_LIST";
    public static final String RESPONSE_INAPP_ITEM_LIST = "INAPP_PURCHASE_ITEM_LIST";
    private static final String RESPONSE_INAPP_PURCHASE_DATA = "INAPP_PURCHASE_DATA";
    public static final String RESPONSE_INAPP_PURCHASE_DATA_LIST = "INAPP_PURCHASE_DATA_LIST";
    private static final String RESPONSE_INAPP_SIGNATURE = "INAPP_DATA_SIGNATURE";
    public static final String RESPONSE_INAPP_SIGNATURE_LIST = "INAPP_DATA_SIGNATURE_LIST";
    public static final String RESPONSE_SUBS_MANAGEMENT_INTENT_KEY = "SUBS_MANAGEMENT_INTENT";
    private static final String TAG = "BillingHelper";

    public static void logVerbose(String tag, String msg) {
        if (Log.isLoggable(tag, Log.VERBOSE)) {
            Log.v(tag, msg);
        }
    }

    public static void logWarn(String tag, String msg) {
        if (Log.isLoggable(tag, Log.WARN)) {
            Log.w(tag, msg);
        }
    }

    public static int getResponseCodeFromIntent(Intent intent, String tag) {
        return getBillingResultFromIntent(intent, tag).getResponseCode();
    }

    public static BillingResult getBillingResultFromIntent(Intent intent, String tag) {
        if (intent != null) {
            return BillingResult.newBuilder().setResponseCode(getResponseCodeFromBundle(intent.getExtras(), tag)).setDebugMessage(getDebugMessageFromBundle(intent.getExtras(), tag)).build();
        }
        logWarn(TAG, "Got null intent!");
        return BillingResult.newBuilder().setResponseCode(6).setDebugMessage(INTERNAL_ERROR).build();
    }

    public static int getResponseCodeFromBundle(Bundle bundle, String tag) {
        if (bundle == null) {
            logWarn(tag, "Unexpected null bundle received!");
            return 6;
        }
        Object responseCode = bundle.get(RESPONSE_CODE);
        if (responseCode == null) {
            logVerbose(tag, "getResponseCodeFromBundle() got null response code, assuming OK");
            return 0;
        } else if (responseCode instanceof Integer) {
            return ((Integer) responseCode).intValue();
        } else {
            logWarn(tag, "Unexpected type for bundle response code: " + responseCode.getClass().getName());
            return 6;
        }
    }

    public static String getDebugMessageFromBundle(Bundle bundle, String tag) {
        if (bundle == null) {
            logWarn(tag, "Unexpected null bundle received!");
            return "RELEASE";
        }
        Object debugMessage = bundle.get(DEBUG_MESSAGE);
        if (debugMessage == null) {
            logVerbose(tag, "getDebugMessageFromBundle() got null response code, assuming OK");
            return "RELEASE";
        } else if (debugMessage instanceof String) {
            return (String) debugMessage;
        } else {
            logWarn(tag, "Unexpected type for debug message: " + debugMessage.getClass().getName());
            return "RELEASE";
        }
    }

    public static List<Purchase> extractPurchases(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        List<String> purchaseDataList = bundle.getStringArrayList(RESPONSE_INAPP_PURCHASE_DATA_LIST);
        List<String> dataSignatureList = bundle.getStringArrayList(RESPONSE_INAPP_SIGNATURE_LIST);
        List<Purchase> resultList = new ArrayList<>();
        if (purchaseDataList == null || dataSignatureList == null) {
            logWarn(TAG, "Couldn't find purchase lists, trying to find single data.");
            Purchase tmpPurchase = extractPurchase(bundle.getString(RESPONSE_INAPP_PURCHASE_DATA), bundle.getString(RESPONSE_INAPP_SIGNATURE));
            if (tmpPurchase == null) {
                logWarn(TAG, "Couldn't find single purchase data as well.");
                return null;
            }
            resultList.add(tmpPurchase);
            return resultList;
        }
        int i = 0;
        while (i < purchaseDataList.size() && i < dataSignatureList.size()) {
            Purchase tmpPurchase2 = extractPurchase(purchaseDataList.get(i), dataSignatureList.get(i));
            if (tmpPurchase2 != null) {
                resultList.add(tmpPurchase2);
            }
            i++;
        }
        return resultList;
    }

    public static Bundle constructExtraParamsForLaunchBillingFlow(BillingFlowParams params, boolean isIabV9Supported, boolean enablePendingPurchases, String libraryVersion) {
        Bundle extraParams = new Bundle();
        extraParams.putString(LIBRARY_VERSION_KEY, libraryVersion);
        if (params.getReplaceSkusProrationMode() != 0) {
            extraParams.putInt(BillingFlowParams.EXTRA_PARAM_KEY_REPLACE_SKUS_PRORATION_MODE, params.getReplaceSkusProrationMode());
        }
        if (!TextUtils.isEmpty(params.getAccountId())) {
            extraParams.putString(BillingFlowParams.EXTRA_PARAM_KEY_ACCOUNT_ID, params.getAccountId());
        }
        if (params.getVrPurchaseFlow()) {
            extraParams.putBoolean(BillingFlowParams.EXTRA_PARAM_KEY_VR, true);
        }
        if (!TextUtils.isEmpty(params.getOldSku())) {
            extraParams.putStringArrayList(BillingFlowParams.EXTRA_PARAM_KEY_OLD_SKUS, new ArrayList(Arrays.asList(new String[]{params.getOldSku()})));
        }
        if (!TextUtils.isEmpty(params.getDeveloperId())) {
            extraParams.putString(BillingFlowParams.EXTRA_PARAM_KEY_DEVELOPER_ID, params.getDeveloperId());
        }
        if (isIabV9Supported && enablePendingPurchases) {
            extraParams.putBoolean(EXTRA_PARAMS_ENABLE_PENDING_PURCHASES, true);
        }
        return extraParams;
    }

    public static Bundle constructExtraParamsForQueryPurchases(boolean isIabV9Supported, boolean enablePendingPurchases, String libraryVersion) {
        Bundle extraParams = new Bundle();
        extraParams.putString(LIBRARY_VERSION_KEY, libraryVersion);
        if (isIabV9Supported && enablePendingPurchases) {
            extraParams.putBoolean(EXTRA_PARAMS_ENABLE_PENDING_PURCHASES, true);
        }
        return extraParams;
    }

    public static Bundle constructExtraParamsForLoadRewardedSku(String rewardToken, int childDirected, int underAgeOfConsent, String libraryVersion) {
        Bundle extraParams = new Bundle();
        extraParams.putString(BillingFlowParams.EXTRA_PARAM_KEY_RSKU, rewardToken);
        extraParams.putString(LIBRARY_VERSION_KEY, libraryVersion);
        if (childDirected != 0) {
            extraParams.putInt(BillingFlowParams.EXTRA_PARAM_CHILD_DIRECTED, childDirected);
        }
        if (underAgeOfConsent != 0) {
            extraParams.putInt(BillingFlowParams.EXTRA_PARAM_UNDER_AGE_OF_CONSENT, childDirected);
        }
        return extraParams;
    }

    public static Bundle constructExtraParamsForGetSkuDetails(boolean isIabV9Supported, boolean enablePendingPurchases, String libraryVersion) {
        Bundle extraParams = new Bundle();
        if (isIabV9Supported) {
            extraParams.putString(LIBRARY_VERSION_KEY, libraryVersion);
        }
        if (isIabV9Supported && enablePendingPurchases) {
            extraParams.putBoolean(EXTRA_PARAMS_ENABLE_PENDING_PURCHASES, true);
        }
        return extraParams;
    }

    public static Bundle constructExtraParamsForConsume(ConsumeParams consumeParams, boolean isIabV9Supported, String libraryVersion) {
        Bundle extraParams = new Bundle();
        if (isIabV9Supported) {
            extraParams.putString(LIBRARY_VERSION_KEY, libraryVersion);
        }
        String developerPayload = consumeParams.getDeveloperPayload();
        if (isIabV9Supported && !TextUtils.isEmpty(developerPayload)) {
            extraParams.putString(EXTRA_PARAMS_DEVELOPER_PAYLOAD, developerPayload);
        }
        return extraParams;
    }

    public static Bundle constructExtraParamsForAcknowledgePurchase(AcknowledgePurchaseParams acknowledgePurchaseParams, String libraryVersion) {
        Bundle extraParams = new Bundle();
        extraParams.putString(LIBRARY_VERSION_KEY, libraryVersion);
        String developerPayload = acknowledgePurchaseParams.getDeveloperPayload();
        if (!TextUtils.isEmpty(developerPayload)) {
            extraParams.putString(EXTRA_PARAMS_DEVELOPER_PAYLOAD, developerPayload);
        }
        return extraParams;
    }

    private static Purchase extractPurchase(String purchaseData, String signatureData) {
        if (purchaseData == null || signatureData == null) {
            logWarn(TAG, "Received a bad purchase data.");
            return null;
        }
        try {
            return new Purchase(purchaseData, signatureData);
        } catch (JSONException e) {
            logWarn(TAG, "Got JSONException while parsing purchase data: " + e);
            return null;
        }
    }
}
