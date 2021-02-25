package uk.henrytwist.fullcart.services

import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import uk.henrytwist.fullcart.R
import uk.henrytwist.fullcart.models.PantryNotification
import uk.henrytwist.fullcart.usecases.GetPantryNotifications
import uk.henrytwist.fullcart.util.NotificationUtil
import uk.henrytwist.fullcart.view.ListItemFormatter
import uk.henrytwist.fullcart.view.main.MainActivity
import javax.inject.Inject

@AndroidEntryPoint
class UseByReceiver : BroadcastReceiver() {

    @Inject
    lateinit var getPantryNotifications: GetPantryNotifications

    override fun onReceive(context: Context?, intent: Intent?) {

        context ?: return

        GlobalScope.launch(Dispatchers.IO) {

            val notificationData = getPantryNotifications()

            if (notificationData.isNotEmpty()) {

                val notificationManager = NotificationManagerCompat.from(context)

                notificationData.forEach {

                    val notification = buildNotification(context, it)
                    notificationManager.notify(BASE_NOTIFICATION_ID + 1 + it.listId, notification)
                }

                notificationManager.notify(BASE_NOTIFICATION_ID, buildSummaryNotification(context, notificationData))
            }
        }
    }

    private fun buildNotification(context: Context, data: PantryNotification): Notification {

        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra(MainActivity.FROM_NOTIFICATION_LIST_ID, data.listId)
        val pendingIntent = PendingIntent.getActivity(context, data.listId, intent, 0)
        return NotificationCompat.Builder(context, NotificationUtil.CHANNEL_USE_BY)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(data.listName)
                .setStyle(buildStyle(context, data))
                .setGroup(GROUP_KEY)
                .setContentText(buildContentText(data))
                .setContentIntent(pendingIntent)
                .build()
    }

    private fun buildStyle(context: Context, data: PantryNotification): NotificationCompat.Style {

        val style = NotificationCompat.InboxStyle()

        data.items.forEach {

            val useByFormatted = ListItemFormatter.getUseByDateSummary(context, it.useByDate)!!
            style.addLine(context.getString(R.string.use_by_notification_item, it.name, useByFormatted))
        }

        return style
    }

    private fun buildContentText(data: PantryNotification): String {

        return data.items.joinToString(" · ") { it.name }
    }

    private fun buildSummaryNotification(context: Context, data: List<PantryNotification>): Notification {

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        return NotificationCompat.Builder(context, NotificationUtil.CHANNEL_USE_BY)
                .setSmallIcon(R.drawable.notification_icon)
                .setSubText(context.getString(R.string.use_by_notification_title))
                .setGroup(GROUP_KEY)
                .setStyle(buildSummaryStyle(data))
                .setContentIntent(pendingIntent)
                .setGroupSummary(true)
                .build()
    }

    private fun buildSummaryStyle(data: List<PantryNotification>): NotificationCompat.Style {

        val style = NotificationCompat.InboxStyle()

        data.forEach {

            style.addLine(it.listName)
        }

        return style
    }

    companion object {

        const val BASE_NOTIFICATION_ID = 0
        const val GROUP_KEY = "uk.henrytwist.fullcart.USE_BY_GROUP"
    }
}