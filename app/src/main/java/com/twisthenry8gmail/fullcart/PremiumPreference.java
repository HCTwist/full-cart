package com.twisthenry8gmail.fullcart;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceDialogFragmentCompat;

import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.SkuDetails;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class PremiumPreference extends SimplifiedDialogPreference {

    enum BillingState {

        AVAILABLE, UNAVAILABLE, LOADING
    }

    private boolean isPremium = false;
    private PremiumBillingHelper billingHelper;

    private PremiumBillingHelper.PremiumPurchaseUpdatedListener premiumListener;

    public PremiumPreference(Context context, AttributeSet attrs) {

        super(context, attrs);

        setKey(PremiumBillingHelper.PREMIUM_PRODUCT_ID);

        billingHelper = new PremiumBillingHelper(context);
        billingHelper.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {

            }

            @Override
            public void onBillingServiceDisconnected() {

            }
        });
    }

    @Override
    protected PreferenceDialogFragmentCompat getDialog(Fragment settingsFragment) {

        return isPremium ? new PurchasedDialog() : UnpurchasedDialog.getInstance();
    }

    void setPremium(boolean premium) {

        this.isPremium = premium;
        if (premium) {

            setTitle(R.string.setting_premium_title);
            setSummary(R.string.setting_premium_summary);
            setIcon(R.drawable.outline_loyalty_24);
        }
        else {

            setTitle(R.string.setting_no_premium_title);
            setSummary(R.string.setting_no_premium_summary);
            setIcon(R.drawable.outline_lock_24);
        }
    }

    void setPremiumListener(PremiumBillingHelper.PremiumPurchaseUpdatedListener listener) {

        billingHelper.setPurchasesUpdatedListener(listener);
    }

    static class PremiumDialog extends PreferenceDialogFragmentCompat {

        static final String TAG = "premium_dialog";

        PremiumBillingHelper billingHelper;

        TextView content;
        TextView statusView;
        View loadingView;
        View unavailableView;
        TextView priceView;

        @NonNull
        @Override
        public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

            if (getContext() == null) {

                return super.onCreateDialog(savedInstanceState);
            }

            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());

            View layout = LayoutInflater.from(getContext()).inflate(R.layout.premium_preference, null);

            ViewFlipper images = layout.findViewById(R.id.premium_preference_images);

            images.setInAnimation(getContext(), android.R.anim.slide_in_left);
            images.setOutAnimation(getContext(), android.R.anim.slide_out_right);

            content = layout.findViewById(R.id.premium_preference_content);
            statusView = layout.findViewById(R.id.premium_preference_loading_content);
            loadingView = layout.findViewById(R.id.premium_preference_loading);
            unavailableView = layout.findViewById(R.id.premium_preference_unavailable);
            priceView = layout.findViewById(R.id.premium_preference_price);

            billingHelper = ((PremiumPreference) getPreference()).billingHelper;

            builder.setView(layout);

            onBuild(builder, layout);

            return builder.create();
        }

        @Override
        public void onDialogClosed(boolean positiveResult) {

        }

        void onBuild(MaterialAlertDialogBuilder builder, View layout) {

        }
    }

    public static class UnpurchasedDialog extends PremiumDialog {

        boolean foundDetails = false;

        static UnpurchasedDialog getInstance() {

            return new UnpurchasedDialog();
        }

        @Override
        void onBuild(MaterialAlertDialogBuilder builder, View layout) {

            queryDetails();

            builder.setTitle(R.string.setting_category_premium);
            builder.setPositiveButton(R.string.no_premium_positive, (dialog, which) -> billingHelper.startPurchase());
            builder.setNegativeButton(android.R.string.cancel, null);

            content.setText(R.string.no_premium_content);
            statusView.setText(R.string.premium_billing_loading);

            layout.post(() -> {
                if (getDialog() != null && !foundDetails) {
                    ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
                }
            });
        }

        void queryDetails() {

            billingHelper.querySkuDetails((billingResult, skuDetailsList) -> {

                for (SkuDetails details : skuDetailsList) {
                    if (details.getSku().equals(PremiumBillingHelper.PREMIUM_PRODUCT_ID)) {

                        priceView.setText(details.getPrice());
                        setBillingAvailable(BillingState.AVAILABLE);
                        foundDetails = true;
                        break;
                    }
                }

                if (!foundDetails) {
                    setBillingAvailable(BillingState.UNAVAILABLE);
                }
            });
        }

        void setBillingAvailable(BillingState state) {

            if (getDialog() != null) {
                AlertDialog alertDialog = ((AlertDialog) getDialog());
                Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);

                switch (state) {

                    case LOADING:
                        statusView.setText(R.string.premium_billing_loading);
                        loadingView.setVisibility(View.VISIBLE);
                        unavailableView.setVisibility(View.GONE);
                        positiveButton.setEnabled(false);
                        positiveButton.setText(R.string.no_premium_positive);
                        break;
                    case UNAVAILABLE:
                        statusView.setText(R.string.premium_billing_error);
                        loadingView.setVisibility(View.INVISIBLE);
                        unavailableView.setVisibility(View.VISIBLE);
                        positiveButton.setEnabled(true);
                        positiveButton.setText(R.string.retry);
                        positiveButton.setOnClickListener(v -> {
                            setBillingAvailable(BillingState.LOADING);
                            queryDetails();
                        });
                        break;
                    case AVAILABLE:
                        statusView.setText("");
                        loadingView.setVisibility(View.INVISIBLE);
                        unavailableView.setVisibility(View.GONE);
                        positiveButton.setEnabled(true);
                        positiveButton.setText(R.string.no_premium_positive);
                        positiveButton.setOnClickListener(v -> {
                            billingHelper.startPurchase();
                            dismiss();
                        });
                }
            }
        }
    }

    public static class PurchasedDialog extends PremiumDialog {

        @Override
        void onBuild(MaterialAlertDialogBuilder builder, View layout) {

            super.onBuild(builder, layout);

            builder.setTitle(R.string.setting_premium_title);

            builder.setNegativeButton(R.string.dismiss, null);
            builder.setNeutralButton(R.string.premium_positive, (dialog, which) -> {
                if (getContext() != null) {
                    FeedbackUtil.startFeedback(getContext(), FeedbackUtil.PREMIUM_FEEDBACK_SUBJECT);
                }
            });

            content.setText(R.string.premium_content);
            loadingView.setVisibility(View.GONE);
        }
    }
}
