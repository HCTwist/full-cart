package com.twisthenry8gmail.fullcart;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;

import androidx.fragment.app.Fragment;
import androidx.preference.DialogPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceDialogFragmentCompat;

public abstract class SimplifiedDialogPreference extends DialogPreference {

    public SimplifiedDialogPreference(Context context, AttributeSet attrs) {

        super(context, attrs);
    }

    public static boolean onDisplayPreferenceDialog(Preference preference, Fragment settingsFragment) {

        if (preference instanceof SimplifiedDialogPreference) {
            PreferenceDialogFragmentCompat dialog = ((SimplifiedDialogPreference) preference).getDialog(settingsFragment);
            dialog.setTargetFragment(settingsFragment, 0);

            if(dialog.getArguments() == null) {
                Bundle bundle = new Bundle(1);
                dialog.setArguments(bundle);
            }

            dialog.getArguments().putString(PreferenceDialogFragmentCompatExposer.ARG_KEY, preference.getKey());

            if (settingsFragment.getFragmentManager() != null) {

                dialog.show(settingsFragment.getFragmentManager(), preference.getKey());
            }
            return true;
        }

        return false;
    }

    protected abstract PreferenceDialogFragmentCompat getDialog(Fragment settingsFragment);

    abstract static class PreferenceDialogFragmentCompatExposer extends PreferenceDialogFragmentCompat {

        static String ARG_KEY = PreferenceDialogFragmentCompat.ARG_KEY;
    }
}
