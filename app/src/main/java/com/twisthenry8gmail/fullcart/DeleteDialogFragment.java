package com.twisthenry8gmail.fullcart;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

/**
 * Dialog fragment that displays a 'delete' alert dialog
 */
public class DeleteDialogFragment extends DialogFragment {

    public static final String TAG = "delete_dialog";
    private static final String MESSAGE = "message";
    private static final String POSITION = "adapter_position";

    static DeleteDialogFragment getInstance(String message, int position) {

        DeleteDialogFragment deleteDialogFragment = new DeleteDialogFragment();
        Bundle args = new Bundle();
        args.putString(DeleteDialogFragment.MESSAGE, message);
        args.putInt(POSITION, position);
        deleteDialogFragment.setArguments(args);
        return deleteDialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if (getContext() == null) {
            return super.onCreateDialog(savedInstanceState);
        }

        String message = getArguments() != null ? getArguments().getString(MESSAGE) : "";

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.yes, (dialogInterface, i) -> {

            int position = getArguments().getInt(POSITION);

            if (getTargetFragment() != null) {
                ((Callback) getTargetFragment()).delete(position);
            }
            else if (getActivity() != null) {
                ((Callback) getActivity()).delete(position);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);

        return builder.create();
    }

    public interface Callback {

        void delete(int position);
    }
}


