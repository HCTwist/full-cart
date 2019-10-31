package com.twisthenry8gmail.fullcart;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import java.util.ArrayList;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

public class UseByReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        showNotification(context, 3, true);
    }

    static void showNotification(Context context, int maxItems, boolean autoCancel) {

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        String[] columns = {DatabaseHelper.LIST_NAME, DatabaseHelper.LIST_CATEGORY, DatabaseHelper.PANTRY_BY_DATE};
        String selection = "length(" + DatabaseHelper.PANTRY_BY_DATE + ")!=1";
        Cursor cursor = databaseHelper.getReadableDatabase().query(DatabaseHelper.PANTRY_TABLE_NAME, columns, selection, null, null, null, DatabaseHelper.PANTRY_BY_DATE);

        ArrayList<String> items = new ArrayList<>();

        int diffPreference = SettingsFragment.getPreferredDateDifference(context);

        while (cursor.moveToNext()) {
            String byDate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.PANTRY_BY_DATE)).substring(1);

            long diff = DateUtil.getCurrentSimpleDateDifference(byDate);

            if (diff <= diffPreference) {

                String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LIST_NAME));
                String daysLeftString;

                if (diff == 0) {
                    daysLeftString = context.getString(R.string.notification_use_by_content_pattern_present, name);
                }
                else {
                    String unit = Math.abs(diff) == 1 ? context.getString(R.string.day) : context.getString(R.string.days);
                    if (diff < 0) {
                        daysLeftString = context.getString(R.string.notification_use_by_content_pattern_past, name, -diff, unit);
                    }
                    else {
                        daysLeftString = context.getString(R.string.notification_use_by_content_pattern, name, diff, unit);
                    }
                }

                items.add(daysLeftString);

                if (items.size() == maxItems) {
                    break;
                }
            }
        }
        cursor.close();

        if (items.size() > 0) {

            Intent contentIntent = new Intent(context, MainActivity.class);
            contentIntent.putExtra(MainActivity.SHOW_PANTRY, true);
            PendingIntent contentPendingIntent = PendingIntent.getActivity(context, 0, contentIntent, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, AlarmUtil.CHANNEL_USE_BY);

            if (items.size() > 1) {

                NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
                for (String item : items) {
                    style.addLine(item);
                }

                builder.setStyle(style);
            }

            builder.setSmallIcon(R.drawable.notification_icon)
                    .setColor(ContextCompat.getColor(context, R.color.primary))
                    .setContentTitle(context.getString(R.string.notification_use_by_title))
                    .setContentText(items.get(0))
                    .setContentIntent(contentPendingIntent).setAutoCancel(autoCancel);

            NotificationManagerCompat.from(context).notify(0, builder.build());
        }
    }
}
