package uk.henrytwist.fullcart.framework.settings

import android.content.SharedPreferences
import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import uk.henrytwist.androidbasics.preferences.getTime
import uk.henrytwist.fullcart.R
import uk.henrytwist.fullcart.data.settings.SettingsRepository
import uk.henrytwist.fullcart.data.settings.SettingsRepository.Companion.DEFAULT_AUTO_DELETE_CHECKED
import uk.henrytwist.fullcart.data.settings.SettingsRepository.Companion.DEFAULT_NOTIFICATION_TIME
import uk.henrytwist.fullcart.data.settings.SettingsRepository.Companion.DEFAULT_USE_SOON_THRESHOLD
import uk.henrytwist.fullcart.framework.SharedPreferencesModule
import java.time.LocalTime
import javax.inject.Inject

class SettingsRepositoryAndroid @Inject constructor(private val resources: Resources, @SharedPreferencesModule.Settings private val sharedPreferences: SharedPreferences) : SettingsRepository {

    fun getDarkMode(): Int {

        return getDarkMode(resources, sharedPreferences)
    }

    override fun getNotificationTime(): LocalTime {

        return getNotificationTime(resources, sharedPreferences)
    }

    override fun getUseSoonThreshold(): Int {

        return sharedPreferences.getString(key(R.string.setting_mark_use_by_key), null)?.toInt()
                ?: DEFAULT_USE_SOON_THRESHOLD
    }

    override fun getCheckedItemAutoDelete(): Int {

        return sharedPreferences.getString(key(R.string.setting_auto_delete_checked_key), null)?.toInt()
                ?: DEFAULT_AUTO_DELETE_CHECKED
    }

    private fun key(@StringRes keyRes: Int) = resources.getString(keyRes)

    companion object {

        fun getDarkMode(resources: Resources, sharedPreferences: SharedPreferences): Int {

            return sharedPreferences.getString(resources.getString(R.string.setting_dark_mode_key), null)?.toInt()?.let { value ->

                when (value) {

                    0 -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    1 -> AppCompatDelegate.MODE_NIGHT_NO
                    2 -> AppCompatDelegate.MODE_NIGHT_YES
                    else -> null
                }
            } ?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }

        fun getNotificationTime(resources: Resources, sharedPreferences: SharedPreferences): LocalTime {

            return sharedPreferences.getTime(resources.getString(R.string.setting_use_by_notify_time_key), DEFAULT_NOTIFICATION_TIME)
        }
    }
}