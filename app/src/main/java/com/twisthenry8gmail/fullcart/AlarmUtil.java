package com.twisthenry8gmail.fullcart;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.Calendar;

import androidx.core.content.ContextCompat;

/**
 * Helper class for scheduling alarms, specifically ones that trigger notifications
 */
class AlarmUtil {

    static final String CHANNEL_USE_BY = "use_by";

    private AlarmUtil() {

    }

    /**
     * Setup all notification channels if post Oreo
     */
    static void registerNotificationChannels(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_USE_BY,
                    context.getResources().getString(R.string.notification_channel_use_by),
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.setLightColor(ContextCompat.getColor(context, R.color.to_use));

            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) manager.createNotificationChannel(channel);
        }
    }

    /**
     * Safely schedules an alarm. If the alarm is already scheduled then this method will not execute
     *
     * @param receiver Broadcast receiver to handle the alarm
     * @param start    When to trigger the alarm
     * @param interval How often to trigger the alarm, passing 0 will assume it's non repeating
     */
    private static void scheduleAlarm(Context context, Class<?> receiver, int requestCode, Calendar start, long interval) {

        Intent alarmIntent = new Intent(context, receiver);

        if (PendingIntent.getBroadcast(context, requestCode, alarmIntent, PendingIntent.FLAG_NO_CREATE) == null) {

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, alarmIntent, 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            if (alarmManager != null) {
                if (interval > 0) {
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, start.getTimeInMillis(), interval, pendingIntent);
                }
                else {

                    alarmManager.set(AlarmManager.RTC_WAKEUP, start.getTimeInMillis(), pendingIntent);
                }
            }
        }
    }

    /**
     * Safely cancels an alarm
     *
     * @param receiver    The original broadcast receiver
     * @param requestCode The original request code
     */
    @SuppressWarnings("SameParameterValue")
    static void cancelAlarm(Context context, Class<?> receiver, int requestCode) {

        Intent alarmIntent = new Intent(context, receiver);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, alarmIntent, 0);
        pendingIntent.cancel();

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) alarmManager.cancel(pendingIntent);
    }

    /**
     * Wrapper for the {@link #scheduleAlarm(Context, Class, int, Calendar, long)}
     */
    static void scheduleUseByAlarm(Context context) {

        Calendar calendar = Calendar.getInstance();

        String[] time = PreferenceManager.getDefaultSharedPreferences(context).getString(SettingsFragment.KEY_USE_BY_NOTIFY_TIME, TimePickerPreference.DEFAULT_USE_BY_NOTIFICATION_TIME).split(":");
        int hour = 0, minute = 0;
        try {
            hour = Integer.parseInt(time[0]);
            minute = Integer.parseInt(time[1]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), hour, minute, 0);
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        scheduleAlarm(context, UseByReceiver.class, 0, calendar, AlarmManager.INTERVAL_DAY);
    }
}
