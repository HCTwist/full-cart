package com.twisthenry8gmail.fullcart;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceDialogFragmentCompat;
import androidx.preference.PreferenceManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimePickerPreference extends SimplifiedDialogPreference {

    static final String DEFAULT_USE_BY_NOTIFICATION_TIME = "09:00";

    public TimePickerPreference(Context context, AttributeSet attrs) {

        super(context, attrs);

        setSummaryProvider(preference -> {

            String time = getContext() == null ?
                    DEFAULT_USE_BY_NOTIFICATION_TIME :
                    PreferenceManager.getDefaultSharedPreferences(getContext())
                            .getString(getKey(), DEFAULT_USE_BY_NOTIFICATION_TIME);

            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date date;
            try {
                date = dateFormat.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
                date = new Date();
            }

            return DateUtils.formatDateTime(context, date.getTime(), DateUtils.FORMAT_SHOW_TIME);
        });
    }

    @Override
    protected PreferenceDialogFragmentCompat getDialog(Fragment settingsFragment) {

        String[] valueSplit = getPersistedString(DEFAULT_USE_BY_NOTIFICATION_TIME).split(":");
        return Dialog.getInstance(Integer.parseInt(valueSplit[0]), Integer.parseInt(valueSplit[1]));
    }

    public static class Dialog extends PreferenceDialogFragmentCompat {

        static final String TAG = "time_picker_dialog";

        private TimePicker timePicker;

        private int startingHour;
        private int startingMinute;

        static final String HOUR = "hour";
        static final String MINUTE = "minute";

        static Dialog getInstance(int hour, int minute) {

            Dialog dialog = new Dialog();
            Bundle args = new Bundle();
            args.putInt(HOUR, hour);
            args.putInt(MINUTE, minute);
            dialog.setArguments(args);
            return dialog;
        }

        @NonNull
        @Override
        public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {

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

            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext(), R.style.NoMinorMinWidthDialog);
            builder.setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {

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

                    ((TimePickerPreference) getPreference()).persistString(hour + ":" + minute);
                }
            });
            builder.setNegativeButton(android.R.string.cancel, null);

            builder.setView(timePicker);

            return builder.create();
        }

        //TODO Why is this necessary etc? Make SimplifiedDialogPreferenceDialogFragment?!
        @Override
        public void onDialogClosed(boolean positiveResult) {

            ((TimePickerPreference) getPreference()).notifyChanged();
        }
    }
}
