package uk.henrytwist.fullcart.view.settings

import android.content.Context
import android.util.AttributeSet
import uk.henrytwist.androidbasics.preferences.TimePickerPreference
import uk.henrytwist.fullcart.R
import uk.henrytwist.fullcart.data.settings.SettingsRepository
import java.time.LocalTime

class NotificationTimePickerPreference(context: Context, attributeSet: AttributeSet) : TimePickerPreference(context, attributeSet) {

    override fun getPickerTitle(): CharSequence {

        return context.getString(R.string.setting_use_by_notify_time)
    }

    override fun getDefaultTime(): LocalTime {

        return SettingsRepository.DEFAULT_NOTIFICATION_TIME
    }
}