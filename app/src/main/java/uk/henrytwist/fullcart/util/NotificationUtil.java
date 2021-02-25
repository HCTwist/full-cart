package uk.henrytwist.fullcart.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.content.ContextCompat;

import uk.henrytwist.fullcart.R;

public class NotificationUtil {

    public static final String CHANNEL_USE_BY = "use_by";

    /**
     * Setup all notification channels if post Oreo
     */
    public static void registerNotificationChannels(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_USE_BY,
                    context.getResources().getString(R.string.notification_channel_use_by),
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.setLightColor(ContextCompat.getColor(context, R.color.to_use));

            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) manager.createNotificationChannel(channel);
        }
    }
}
