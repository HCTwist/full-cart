package com.twisthenry8gmail.fullcart;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import java.util.ArrayList;
import java.util.List;

class PremiumBillingHelper {

    static final String PREMIUM_PRODUCT_ID = "premium";
    private static final List<String> skuList = new ArrayList<>();

    static {
        skuList.add(PREMIUM_PRODUCT_ID);
    }

    private Context context;

    private BillingClient client;

    private PremiumPurchaseUpdatedListener premiumPurchaseListener;

    PremiumBillingHelper(Context context) {

        this.context = context;

        client = BillingClient.newBuilder(context).setListener((billingResult, purchases) -> {

            boolean premium = false;
            if (purchases != null) {
                for (Purchase purchase : purchases) {
                    if (purchase.getSku().equals(PREMIUM_PRODUCT_ID)) {
                        premium = true;
                        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(PREMIUM_PRODUCT_ID, true).apply();

                        if (!purchase.isAcknowledged()) {
                            AcknowledgePurchaseParams acknowledgeParams = AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.getPurchaseToken()).build();
                            client.acknowledgePurchase(acknowledgeParams, billingResult1 -> {
                            });
                        }
                    }
                }
            }

            if (premiumPurchaseListener != null) {
                premiumPurchaseListener.premiumPurchaseUpdated(premium);
            }
        }).enablePendingPurchases().build();
    }

    void setPurchasesUpdatedListener(PremiumPurchaseUpdatedListener listener) {

        this.premiumPurchaseListener = listener;
    }

    void startConnection(BillingClientStateListener listener) {

        client.startConnection(listener);
    }

    void querySkuDetails(SkuDetailsResponseListener listener) {

        SkuDetailsParams params = SkuDetailsParams.newBuilder().setSkusList(skuList).setType(BillingClient.SkuType.INAPP).build();
        client.querySkuDetailsAsync(params, listener);
    }

    void startPurchase() {

        querySkuDetails((billingResult, skuDetailsList) -> {

            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                BillingFlowParams params = BillingFlowParams.newBuilder().setSkuDetails(skuDetailsList.get(0)).build();
                client.launchBillingFlow((AppCompatActivity) context, params);
            }
        });
    }

    /**
     * Check whether the user is premium. The result is fetched from a cache in the form of a
     * SharedPreference, however after this has been called, the premium status is checked from the
     * billing API and the cache updated if necessary
     *
     * @return whether the user is considered premium
     */
    boolean isPremium() {

        resolveInconsistencies();
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PREMIUM_PRODUCT_ID, false);
    }

    void resolveInconsistencies() {

        client.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {

                List<Purchase> purchaseList = client.queryPurchases(BillingClient.SkuType.INAPP).getPurchasesList();
                boolean foundPremium = false;

                if (purchaseList != null) {
                    for (Purchase purchase : purchaseList) {
                        if (purchase.getSku().equals(PREMIUM_PRODUCT_ID)) {
                            foundPremium = true;
                            break;
                        }
                    }
                }

                if (foundPremium != PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PREMIUM_PRODUCT_ID, false)) {
                    PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(PREMIUM_PRODUCT_ID, foundPremium).apply();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {

            }
        });
    }

    void consumePremium() {

        client.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {

                List<Purchase> purchaseList = client.queryPurchases(BillingClient.SkuType.INAPP).getPurchasesList();
                for (Purchase purchase : purchaseList) {
                    if (purchase.getSku().equals(PREMIUM_PRODUCT_ID)) {
                        ConsumeParams params = ConsumeParams.newBuilder().setPurchaseToken(purchase.getPurchaseToken()).build();
                        client.consumeAsync(params, (billingResult1, purchaseToken) -> {

                        });
                    }
                }
            }

            @Override
            public void onBillingServiceDisconnected() {

            }
        });
    }

    interface PremiumPurchaseUpdatedListener {

        void premiumPurchaseUpdated(boolean premium);
    }
}
