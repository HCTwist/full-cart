package uk.henrytwist.fullcart.data.settings

import java.time.LocalTime

interface SettingsRepository {

    fun getNotificationTime(): LocalTime

    fun getUseSoonThreshold(): Int

    fun getCheckedItemAutoDelete(): Int

    companion object {

        val DEFAULT_NOTIFICATION_TIME: LocalTime = LocalTime.of(9, 0)
        const val DEFAULT_USE_SOON_THRESHOLD = 2
        const val DEFAULT_AUTO_DELETE_CHECKED = 0
    }
}