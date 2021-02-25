package uk.henrytwist.fullcart.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.preference.PreferenceManager;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

import uk.henrytwist.fullcart.framework.settings.SettingsRepositoryAndroid;
import uk.henrytwist.fullcart.services.UseByReceiver;

/**
 * Helper class for scheduling alarms, specifically ones that trigger notifications
 */
public class AlarmUtil {

    private AlarmUtil() {

    }

    /**
     * Safely schedules an alarm. If the alarm is already scheduled then this method will not execute
     *
     * @param receiver Broadcast receiver to handle the alarm
     * @param start    When to trigger the alarm
     * @param interval How often to trigger the alarm, passing 0 will assume it's non repeating
     */
    private static void scheduleAlarm(Context context, Class<?> receiver, int requestCode, Instant start, long interval) {

        Intent alarmIntent = new Intent(context, receiver);

        if (PendingIntent.getBroadcast(context, requestCode, alarmIntent, PendingIntent.FLAG_NO_CREATE) == null) {

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, alarmIntent, 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            if (alarmManager != null) {

                if (interval > 0) {

                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, start.toEpochMilli(), interval, pendingIntent);
                }
                else {

                    alarmManager.set(AlarmManager.RTC_WAKEUP, start.toEpochMilli(), pendingIntent);
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
    public static void cancelAlarm(Context context, Class<?> receiver, int requestCode) {

        Intent alarmIntent = new Intent(context, receiver);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, alarmIntent, 0);
        pendingIntent.cancel();

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) alarmManager.cancel(pendingIntent);
    }

    public static void cancelUseByAlarm(Context context) {

        cancelAlarm(context, UseByReceiver.class, 0);
    }

    /**
     * Wrapper for the {@link #scheduleAlarm(Context, Class, int, Instant, long)}
     */
    public static void scheduleUseByAlarm(Context context) {

        LocalDateTime now = LocalDateTime.now();
        LocalTime notificationTime = SettingsRepositoryAndroid.Companion.getNotificationTime(context.getResources(), PreferenceManager.getDefaultSharedPreferences(context));

        LocalDateTime nextNotificationTime = now.withHour(notificationTime.getHour()).withMinute(notificationTime.getMinute());
        if (nextNotificationTime.isBefore(now)) {

            nextNotificationTime = nextNotificationTime.plusDays(1);
        }

        scheduleAlarm(context, UseByReceiver.class, 0, nextNotificationTime.atZone(ZoneId.systemDefault()).toInstant(), AlarmManager.INTERVAL_DAY);
    }
}
