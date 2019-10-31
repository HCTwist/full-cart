package com.twisthenry8gmail.fullcart;

import android.content.Context;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Helper class for date functions
 */
class DateUtil {

    private DateUtil() {

    }

    private static final int WORDED_DATE_NO_YEAR = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NO_YEAR;
    static final int TECHNICAL_DATE = DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_YEAR;

    /**
     * Convert the current date into an integer array
     *
     * @return an array of length 3; with year, month and day of month
     */
    private static int[] getCurrentDateForPicker() {

        Calendar calendar = Calendar.getInstance();

        int[] array = new int[3];

        array[0] = calendar.get(Calendar.YEAR);
        array[1] = calendar.get(Calendar.MONTH);
        array[2] = calendar.get(Calendar.DAY_OF_MONTH);

        return array;
    }

    /**
     * Convert a date in the yyyy-MM-dd format to an integer array
     *
     * @param simpleDate the date in an array of length 3; with year, month and day of month form
     * @return an array of length 3; with year, month and day of month
     */
    static int[] getDateForDatePicker(String simpleDate) {

        if (simpleDate.isEmpty()) return getCurrentDateForPicker();

        SimpleDateFormat dateFormat = new SimpleDateFormat(DatabaseHelper.SIMPLE_DATE_FORMAT, Locale.getDefault());
        try {
            Date date = dateFormat.parse(simpleDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            int[] array = new int[3];

            array[0] = calendar.get(Calendar.YEAR);
            array[1] = calendar.get(Calendar.MONTH);
            array[2] = calendar.get(Calendar.DAY_OF_MONTH);

            return array;
        } catch (ParseException e) {
            e.printStackTrace();
            return new int[]{0, 0, 0};
        }
    }

    /**
     * Produce a date in yy-MM-dd format
     *
     * @param year  the year
     * @param month the month
     * @param day   the day
     * @return the date in yyyy-MM-dd format
     */
    static String formatSimpleDate(int year, int month, int day) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        SimpleDateFormat dateFormat = new SimpleDateFormat(DatabaseHelper.SIMPLE_DATE_FORMAT, Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    /**
     * @return the current date in yy-MM-dd HH:mm:ss
     */
    static String formatCurrentDate() {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DatabaseHelper.DATE_FORMAT, Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    /**
     * Produce a readable date in the format "dd/mm/yy"
     *
     * @param date          the date to display
     * @param inputPattern  the format of <code>date</code>
     * @param outputPattern the desired format code, for example {@value TECHNICAL_DATE}
     * @return a readable date
     */
    static String displayDate(Context context, String date, String inputPattern, int outputPattern) {

        SimpleDateFormat dateFormat = new SimpleDateFormat(inputPattern, Locale.getDefault());
        Date d;
        try {
            d = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            d = new Date();
        }

        return DateUtils.formatDateTime(context, d.getTime(), outputPattern);
    }

    /**
     * Produce a readable date based on pantry settings
     *
     * @param date the date to display
     * @return a readable date
     */
    private static String displayPantryDate(Context context, String date, int preference) {

        switch (preference) {

            case 0:
                return displayDate(context, date, DatabaseHelper.SIMPLE_DATE_FORMAT, WORDED_DATE_NO_YEAR);
            case 1:
                return displayDate(context, date, DatabaseHelper.SIMPLE_DATE_FORMAT, TECHNICAL_DATE);
        }

        return "";
    }

    /**
     * Convenience method for {@link DateUtil#displayPantryDate(Context, String, int)}
     */
    static String displayPantryDate(Context context, String date) {

        int preferenceValue = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString(SettingsFragment.KEY_PANTRY_DATE_FORMAT, "0"));
        return displayPantryDate(context, date, preferenceValue);
    }

    /**
     * Produce a readable time
     *
     * @param formatTime a time in HH:mm format
     * @return a readable time
     */
    static String displayTime(String formatTime, Context context) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Date date;
        try {
            date = dateFormat.parse(formatTime);
        } catch (ParseException e) {
            e.printStackTrace();
            date = new Date();
        }

        return DateUtils.formatDateTime(context, date.getTime(), DateUtils.FORMAT_SHOW_TIME);
    }

    /**
     * Calculate the number of days since the current day
     *
     * @param formatDate a date in yy-MM-dd format
     * @return the difference in days from the input date to the current date
     */
    static long getCurrentSimpleDateDifference(String formatDate) {

        SimpleDateFormat dateFormat = new SimpleDateFormat(DatabaseHelper.SIMPLE_DATE_FORMAT, Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(dateFormat.parse(formatDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar currentCalendar = Calendar.getInstance();
        simplifyCalendar(currentCalendar);
        simplifyCalendar(calendar);

        return (calendar.getTimeInMillis() - currentCalendar.getTimeInMillis()) / (1000L * 60 * 60 * 24);
    }

    /**
     * Calculate the number of days since the current day
     *
     * @param formatDate a date in yy-MM-dd HH:mm:ss format
     * @return the difference in days from the input date to the current date
     */
    static long getCurrentDateDifference(String formatDate) {

        SimpleDateFormat dateFormat = new SimpleDateFormat(DatabaseHelper.DATE_FORMAT, Locale.getDefault());
        Date currentDate = new Date();
        Date date;
        try {
            date = dateFormat.parse(formatDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }

        return (date.getTime() - currentDate.getTime()) / (1000L * 60 * 60 * 24);
    }

    /**
     * Clear the hour, minute, second and millisecond component of a calendar
     *
     * @param calendar the calendar to quickAdd
     */
    private static void simplifyCalendar(Calendar calendar) {

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }
}
