package uk.henrytwist.fullcart.view.settings

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import uk.henrytwist.androidbasics.preferences.onDisplaySelfHandlingPreferenceDialog
import uk.henrytwist.fullcart.R
import uk.henrytwist.fullcart.framework.settings.SettingsRepositoryAndroid
import uk.henrytwist.fullcart.util.AlarmUtil
import uk.henrytwist.fullcart.util.FeedbackUtil
import uk.henrytwist.fullcart.util.RatingUtil
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    @Inject
    lateinit var settingsRepository: SettingsRepositoryAndroid

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        setPreferencesFromResource(R.xml.preferences, rootKey)

        if (!FeedbackUtil.canSendFeedback(context)) {

            preferenceScreen.removePreference(findPreference(getString(R.string.setting_email_key)))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        listView.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        listView.isNestedScrollingEnabled = false
    }

    override fun onResume() {
        super.onResume()

        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()

        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {

        if (sharedPreferences == null) return

        when (key) {

            getString(R.string.setting_dark_mode_key) -> {

                AppCompatDelegate.setDefaultNightMode(settingsRepository.getDarkMode())
            }

            getString(R.string.setting_use_by_notify_time_key) -> {

                AlarmUtil.cancelUseByAlarm(context)
                AlarmUtil.scheduleUseByAlarm(context)
            }
        }
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {

        when (preference?.key) {

            getString(R.string.setting_notifications_key) -> {

                val intent = Intent()
                intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
                startActivity(intent)
            }

            getString(R.string.setting_email_key) -> {

                FeedbackUtil.startFeedback(context)
            }

            getString(R.string.setting_about_key) -> {

                findNavController().navigate(R.id.action_settingsFragment_to_aboutFragment)
            }

            getString(R.string.setting_rating_key) -> {

                RatingUtil.openPlayStoreListing(requireContext())
            }

            else -> return false
        }

        return true
    }

    override fun onDisplayPreferenceDialog(preference: Preference?) {

        val handled = onDisplaySelfHandlingPreferenceDialog(preference)

        if (!handled) super.onDisplayPreferenceDialog(preference)
    }
}