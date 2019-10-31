package com.twisthenry8gmail.simplifiedpreferences;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.TimePicker;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

@Deprecated
public class TimePickerPreference extends SimplifiedDialogPreference {

    private String defaultValue;

    public TimePickerPreference(Context context, AttributeSet attrs) {

        super(context, attrs);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {

        defaultValue = a.getString(index);
        return defaultValue;
    }

    @Override
    protected SimplifiedDialogPreferenceDialog onCreateDialog() {

        String[] time = getPersistedString(defaultValue).split(":");
        int hour = 0, minute = 0;
        try {
            hour = Integer.parseInt(time[0]);
            minute = Integer.parseInt(time[1]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return SimplifiedDialog.getInstance(hour, minute);
    }

    public static class SimplifiedDialog extends SimplifiedDialogPreferenceDialog {

        static final String HOUR = "hour";
        static final String MINUTE = "minute";

        static SimplifiedDialog getInstance(int hour, int minute) {

            SimplifiedDialog dialog = new SimplifiedDialog();
            Bundle args = new Bundle();
            args.putInt(HOUR, hour);
            args.putInt(MINUTE, minute);
            dialog.setArguments(args);
            return dialog;
        }

        private TimePicker timePicker;

        private int startingHour;
        private int startingMinute;

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            if (getContext() == null) {
                return super.onCreateDialog(savedInstanceState);
            }

            startingHour = getArguments() == null ? Calendar.getInstance().get(Calendar.HOUR) : getArguments().getInt(HOUR);
            startingMinute = getArguments() == null ? Calendar.getInstance().get(Calendar.MINUTE) : getArguments().getInt(MINUTE);

            timePicker = new TimePicker(getContext());
            timePicker.setIs24HourView(true);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                timePicker.setCurrentHour(startingHour);
                timePicker.setCurrentMinute(startingMinute);
            }
            else {
                timePicker.setHour(startingHour);
                timePicker.setMinute(startingMinute);
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.NoMinorMinWidthDialog);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    int hour, minute;

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                        hour = timePicker.getCurrentHour();
                        minute = timePicker.getCurrentMinute();
                    }
                    else {
                        hour = timePicker.getHour();
                        minute = timePicker.getMinute();
                    }

                    if (hour != startingHour || minute != startingMinute) {

                        persist(hour + ":" + minute);
                    }
                }
            });
            builder.setNegativeButton(android.R.string.cancel, null);

            builder.setView(timePicker);

            return builder.create();
        }
    }
}
