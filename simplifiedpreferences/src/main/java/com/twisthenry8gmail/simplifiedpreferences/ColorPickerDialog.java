package com.twisthenry8gmail.simplifiedpreferences;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

@Deprecated
public class ColorPickerDialog extends DialogFragment {

    public static final String TAG = "tag";

    private static final String COLOR = "color";

    private ColorPickerHelper helper;

    private boolean result = false;

    public static ColorPickerDialog getInstance(int color) {

        ColorPickerDialog colorPickerDialog = new ColorPickerDialog();
        Bundle args = new Bundle();
        args.putInt(ColorPickerDialog.COLOR, color);
        colorPickerDialog.setArguments(args);
        return colorPickerDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if(getContext() == null) {

            return super.onCreateDialog(savedInstanceState);
        }

        helper = new ColorPickerHelper(getContext());

        if (getArguments() != null) {
            helper.setStartingColor(getArguments().getInt(COLOR, 0));
        }
        helper.setRGB(helper.getStartingColor());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle(R.string.color_picker_title);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                result = true;
                if (getActivity() != null) ((Callback) getActivity()).colorSet(helper.getColor());
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                result = false;
                dismiss();
            }
        });

        //Instantiate neutral button and attach listener after dialog creation
        builder.setNeutralButton(R.string.reset, null);

        builder.setView(helper.getDialogView());

        return builder.create();
    }

    @Override
    public void onResume() {

        Button neutralButton = ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_NEUTRAL);
        neutralButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                result = true;
                helper.setRGB(helper.getStartingColor());
                helper.updateDot();
                helper.updateSeekBars();
            }
        });
        super.onResume();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {

        if (!result && getActivity() != null) {
            ((Callback) getActivity()).colorDismissed();
        }
        super.onDismiss(dialog);
    }

    interface Callback {

        void colorSet(int color);

        void colorDismissed();
    }
}
