package com.twisthenry8gmail.fullcart;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

/**
 * Implementation of {@link android.app.DatePickerDialog} that has a title based upon the date type.
 * This class should not be manually instantiated but should use the
 * {@link #getInstance(String, ListItemPantry.ByDateType)}. To receive the date set, either the
 * fragment this is being shown from should be set as a target
 * ({@link androidx.fragment.app.Fragment#setTargetFragment(Fragment, int)}) and should implement
 * {@link Callback}, or the activity should implement the {@link Callback}.
 */
public class ByDatePicker extends DialogFragment {

    public static final String TAG = "by_date_dialog";

    private static final String BY_DATE = "by_date";
    private static final String BY_DATE_TYPE = "by_date_type";

    private DatePicker datePicker;

    /**
     * Instantiate the {@link ByDatePicker}
     *
     * @param byDate   the date to be chosen by default on the picker. To choose the current date pass
     *                 an empty string
     * @param dateType the type of date. This influences the dialog title
     * @return an instance of {@link ByDatePicker}
     */
    static ByDatePicker getInstance(String byDate, ListItemPantry.ByDateType dateType) {

        ByDatePicker datePickerDialog = new ByDatePicker();
        Bundle args = new Bundle();
        args.putString(BY_DATE, byDate);
        args.putInt(BY_DATE_TYPE, dateType.ordinal());
        datePickerDialog.setArguments(args);
        return datePickerDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if (getArguments() == null || getContext() == null) {

            return super.onCreateDialog(savedInstanceState);
        }

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext(), R.style.NoMinorMinWidthDialog);

        int byDateType = getArguments().getInt(BY_DATE_TYPE);
        builder.setTitle(getResources().getStringArray(R.array.by_date_titles)[byDateType]);

        View layout = View.inflate(getContext(), R.layout.by_date_picker_dialog, null);

        datePicker = layout.findViewById(R.id.by_date_picker_picker);

        String oldByDate = getArguments().getString(BY_DATE);
        if (oldByDate == null || oldByDate.isEmpty()) {

            oldByDate = DateUtil.formatCurrentDate();
        }

        int[] date = DateUtil.getDateForDatePicker(oldByDate);
        datePicker.init(date[0], date[1], date[2], null);

        Calendar currentCalendar = Calendar.getInstance();
        Calendar selectionCalendar = Calendar.getInstance();
        selectionCalendar.set(date[0], date[1], date[2]);

        datePicker.setMinDate(Math.min(currentCalendar.getTimeInMillis(), selectionCalendar.getTimeInMillis()));

        builder.setView(layout);

        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> callback(false));
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dismiss());
        builder.setNeutralButton(R.string.skip, (dialog, which) -> callback(true));

        return builder.create();
    }

    /**
     * Returns the date choice to a fragment or the activity
     *
     * @param skipped whether the date should be skipped. If so this will return an empty date to
     *                the callback
     */
    private void callback(boolean skipped) {

        Callback callback = null;
        if (getTargetFragment() != null) {
            callback = (Callback) getTargetFragment();
        }
        else if (getActivity() != null) {
            callback = (Callback) getActivity();
        }

        if (callback != null) {

            callback.onDateSet(skipped ? "" : DateUtil.formatSimpleDate(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth()));
        }
    }

    public interface Callback {

        void onDateSet(String date);
    }
}

