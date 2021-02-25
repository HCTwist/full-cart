package uk.henrytwist.fullcart.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import uk.henrytwist.fullcart.util.AlarmUtil;

/**
 * Handles rescheduling reboot.
 */
public class UseByRescheduler extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction() != null && (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED) || intent.getAction().equals(Intent.ACTION_MY_PACKAGE_REPLACED))) {
            AlarmUtil.scheduleUseByAlarm(context);
        }
    }
}