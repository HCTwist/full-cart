package com.twisthenry8gmail.fullcart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    //TODO Changing notify time 'leaks receiver' in Logcat?

    static final String SETTINGS_DATA = "request_settings_data";

    //Notifications
    private static final String KEY_NOTIFICATIONS = "notifications";
    static final String KEY_USE_BY_NOTIFY_TIME = "use_by_notify_time";

    //General
    static final String KEY_SHOW_PANTRY = "show_pantry";
    static final String KEY_LOCK_ROTATION = "lock_rotation";
    private static final String KEY_DARK_MODE = "dark_mode";
    private static final String KEY_SHOW_TUTORIAL = "show_tutorial";

    //Shopping
    static final String KEY_AUTO_DELETE_CHECKED = "auto_delete_checked";

    //Pantry
    private static final String KEY_BEST_BEFORE_DATE_DEFAULT = "best_before_default";
    static final String KEY_ASK_USE_BY = "ask_use_by";
    static final String KEY_MARK_USE_BY = "mark_use_by";
    static final String KEY_PANTRY_DATE_FORMAT = "pantry_date_formatting";

    //Search
    static final String KEY_SHOW_CATEGORY_LETTERS = "show_category_letters";
    private static final String KEY_CLEAR_SEARCH_DB = "clear_search_db";

    //Feedback
    private static final String KEY_EMAIL = "email";

    private final ArrayList<String> keysChanged = new ArrayList<>();
    private boolean clearSearchDB = false;

    private PremiumPreference premiumPreference;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        addPreferencesFromResource(R.xml.preferences);

        premiumPreference = findPreference(PremiumBillingHelper.PREMIUM_PRODUCT_ID);
        togglePremiumPreferences(isPremium(getContext()));

        premiumPreference.setPremiumListener(premium -> {

            togglePremiumPreferences(premium);
            onDisplayPreferenceDialog(premiumPreference);
        });
    }

    //Attach and detach listeners
    @Override
    public void onPause() {

        //Clear the search DB if it was queued
        if (clearSearchDB) {
            DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getContext());
            databaseHelper.getWritableDatabase().delete(DatabaseHelper.SEARCH_TABLE_NAME, null, null);
        }

        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);

        super.onPause();
    }

    @Override
    public void onResume() {

        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);

        super.onResume();
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {

        //TODO Abstract to custom preferencefragmentcompat implementation
        if (!SimplifiedDialogPreference.onDisplayPreferenceDialog(preference, this)) {
            super.onDisplayPreferenceDialog(preference);
        }
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {

        if (!preference.hasKey()) {

            return super.onPreferenceTreeClick(preference);
        }

        switch (preference.getKey()) {

            case KEY_NOTIFICATIONS:
                if (getContext() == null) break;

                Intent notificationsIntent = new Intent();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationsIntent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                    notificationsIntent.putExtra(Settings.EXTRA_APP_PACKAGE, getContext().getPackageName());
                }
                else {
                    notificationsIntent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                    notificationsIntent.putExtra("app_package", getContext().getPackageName());
                    notificationsIntent.putExtra("app_uid", getContext().getApplicationInfo().uid);
                }
                startActivity(notificationsIntent);
                return true;
            case KEY_SHOW_TUTORIAL:
                startActivity(new Intent(getActivity(), TutorialActivity.class));
                return true;
            case KEY_CLEAR_SEARCH_DB:
                if (getView() != null) {

                    clearSearchDB = true;
                    Snackbar.make(getView(), R.string.setting_search_db_prompt, Snackbar.LENGTH_LONG).setAction(R.string.undo, v -> clearSearchDB = false).show();
                    return true;
                }
                break;
            case KEY_EMAIL:

                if (getContext() != null) {
                    FeedbackUtil.startFeedback(getContext(), FeedbackUtil.FEEDBACK_SUBJECT);
                    return true;
                }
                break;
        }

        return super.onPreferenceTreeClick(preference);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        //Keep a list of keys changed to send back to the main activity
        keysChanged.add(key);

        Intent data = new Intent();
        data.putExtra(SETTINGS_DATA, keysChanged);

        if (getActivity() != null) {
            getActivity().setResult(Activity.RESULT_OK, data);
        }

        switch (key) {
            case KEY_DARK_MODE:
                AppCompatDelegate.setDefaultNightMode(SettingsFragment.getDefaultNightMode(getContext()));
                break;
            case KEY_USE_BY_NOTIFY_TIME:
                AlarmUtil.cancelAlarm(getActivity(), UseByReceiver.class, 0);
                AlarmUtil.scheduleUseByAlarm(getActivity());
                break;
        }
    }

    /**
     * Sets up the premium preference
     * @param premium whether the preference should display itself as 'premium'
     */
    private void togglePremiumPreferences(boolean premium) {

        String[] premiumPreferenceKeys = {KEY_DARK_MODE};

        for (String k : premiumPreferenceKeys) {
            Preference p = findPreference(k);
            if (p != null) {
                if(premium) {
                    p.setIcon(null);
                }
                else {
                    p.setIcon(R.drawable.outline_lock_24);
                }
                p.setEnabled(premium);
            }
        }

        premiumPreference.setPremium(premium);
    }

    /**
     * Scrolls to the premium preference and opens the dialog
     */
    void highlightPremium() {

        scrollToPreference(PremiumBillingHelper.PREMIUM_PRODUCT_ID);
        new Handler().post(() -> {
            Preference premiumPreference = findPreference(PremiumBillingHelper.PREMIUM_PRODUCT_ID);
            if (premiumPreference != null) {
                premiumPreference.performClick();
            }
        });
    }

    /**
     * Checks the by date type as specified in the settings by the user
     * @param context the context
     * @return either {@link ListItemPantry.ByDateType#BEST_BEFORE} or {@link ListItemPantry.ByDateType#USE_BY}
     */
    static ListItemPantry.ByDateType getDefaultByDateType(Context context) {

        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(SettingsFragment.KEY_BEST_BEFORE_DATE_DEFAULT, false)
                ? ListItemPantry.ByDateType.BEST_BEFORE
                : ListItemPantry.ByDateType.USE_BY;
    }

    /**
     * Checks the number of days until an item should be marked as used, as specified in the
     * settings by the user
     * @param context the context
     * @return the number of days
     */
    static int getPreferredDateDifference(Context context) {

        String dateDifferenceString = PreferenceManager.getDefaultSharedPreferences(context).getString(SettingsFragment.KEY_MARK_USE_BY, "2");
        int dateDifference;
        try {
            dateDifference = Integer.parseInt(dateDifferenceString);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            dateDifference = 2;
        }

        return dateDifference;
    }

    /**
     * Check when checked shopping list items should be automatically deleted, as specified in
     * the settings by the user
     * @param context the context
     * @return the number of days until they should be deleted, 0 meaning never
     */
    static int getCheckedAutoDelete(Context context) {

        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context)
                .getString(SettingsFragment.KEY_AUTO_DELETE_CHECKED, "0"));
    }

    /**
     * Check the night mode settings, as specified by the user
     * @param context the context
     * @return one of {@link AppCompatDelegate#MODE_NIGHT_NO}, {@link AppCompatDelegate#MODE_NIGHT_YES}
     * and then either {@link AppCompatDelegate#MODE_NIGHT_FOLLOW_SYSTEM} for Android P and above or
     * {@link AppCompatDelegate#MODE_NIGHT_AUTO_BATTERY} otherwise
     */
    static int getDefaultNightMode(Context context) {

        int def = Build.VERSION.SDK_INT > Build.VERSION_CODES.P ? AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM : AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY;

        int[] modes = {
                AppCompatDelegate.MODE_NIGHT_NO,
                AppCompatDelegate.MODE_NIGHT_YES,
                def
        };

        return modes[Integer.parseInt(
                PreferenceManager.getDefaultSharedPreferences(context)
                        .getString(SettingsFragment.KEY_DARK_MODE, "0")
        )];
    }

    /**
     * Static wrapper for {@link PremiumBillingHelper#isPremium()}
     * @param context the context
     * @return whether the user is considered premium
     */
    static boolean isPremium(Context context) {

        PremiumBillingHelper billingHelper = new PremiumBillingHelper(context);
        return billingHelper.isPremium();
    }
}