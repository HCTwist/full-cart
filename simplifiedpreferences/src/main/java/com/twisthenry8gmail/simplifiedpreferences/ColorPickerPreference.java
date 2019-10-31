package com.twisthenry8gmail.simplifiedpreferences;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.PreferenceViewHolder;

@Deprecated
public class ColorPickerPreference extends SimplifiedDialogPreference {

    private int mDefaultColor;

    private View mSmallDot;

    public ColorPickerPreference(Context context, AttributeSet attrs) {

        super(context, attrs);
        setWidgetLayoutResource(R.layout.color_picker_dot);
    }

    @Override
    protected SimplifiedDialogPreferenceDialog onCreateDialog() {

        return SimplifiedDialog.getInstance(mDefaultColor, getPersistedInt(mDefaultColor));
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {

        mDefaultColor = a.getInt(index, 0);
        return mDefaultColor;
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {

        mSmallDot = holder.findViewById(R.id.small_dot);
        super.onBindViewHolder(holder);
        updateSmallDot();
    }

    public void updateSmallDot() {

        ((GradientDrawable) mSmallDot.getBackground().mutate()).setColor(getPersistedInt(mDefaultColor));
    }

    public static class SimplifiedDialog extends SimplifiedDialogPreferenceDialog {

        static final String DEFAULT_COLOR = "default_color";
        static final String STARTING_COLOR = "starting_color";

        ColorPickerHelper mHelper;

        static SimplifiedDialog getInstance(int defaultColor, int startingColor) {

            SimplifiedDialog dialog = new SimplifiedDialog();
            Bundle args = new Bundle();
            args.putInt(DEFAULT_COLOR, defaultColor);
            args.putInt(STARTING_COLOR, startingColor);
            dialog.setArguments(args);
            return dialog;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            if (getContext() == null || getArguments() == null) {

                return super.onCreateDialog(savedInstanceState);
            }

            mHelper = new ColorPickerHelper(getContext());
            View layout = mHelper.getDialogView();
            mHelper.setStartingColor(getArguments().getInt(STARTING_COLOR));

            mHelper.updateDot();
            mHelper.updateSeekBars();

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    persist(mHelper.getColor());
                }
            });
            builder.setNegativeButton(android.R.string.cancel, null);
            builder.setNeutralButton(R.string.reset, null);
            builder.setView(layout);

            return builder.create();
        }

        @Override
        public void onResume() {

            ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (getArguments() != null) {
                        mHelper.setRGB(getArguments().getInt(DEFAULT_COLOR));
                    }
                    mHelper.updateDot();
                    mHelper.updateSeekBars();
                }
            });
            super.onResume();
        }
    }
}
