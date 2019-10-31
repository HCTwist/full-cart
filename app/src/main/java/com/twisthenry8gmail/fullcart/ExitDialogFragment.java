package com.twisthenry8gmail.fullcart;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ExitDialogFragment extends DialogFragment {

    static final String TAG = "exit_dialog";

    private ExitCallback callback;

    static ExitDialogFragment getInstance(ExitCallback callback) {

        ExitDialogFragment fragment = new ExitDialogFragment();
        fragment.callback = callback;
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        if(getContext() == null) {

            return super.onCreateDialog(savedInstanceState);
        }

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());

        builder.setMessage(R.string.item_exit_confirmation);
        builder.setPositiveButton(R.string.item_exit_positive, (dialog, which) -> {

            if(callback != null) {

                callback.saveChanges();
            }
        });
        builder.setNegativeButton(R.string.item_exit_negative, (dialog, which) -> {

            if(callback != null) {

                callback.discardChanges();
            }
        });
        builder.setNeutralButton(android.R.string.cancel, null);

        return builder.create();
    }

    interface ExitCallback {

        void discardChanges();
        void saveChanges();
    }
}
