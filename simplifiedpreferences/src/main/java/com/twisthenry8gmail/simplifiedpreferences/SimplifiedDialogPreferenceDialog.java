package com.twisthenry8gmail.simplifiedpreferences;

import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceFragmentCompat;

@Deprecated
abstract class SimplifiedDialogPreferenceDialog extends DialogFragment {

    static final String KEY = "key";

    void persist(String s) {

        SimplifiedDialogPreference preference = getPreference();

        if(preference != null) preference.persist(s);
    }

    void persist(int i) {

        SimplifiedDialogPreference preference = getPreference();

        if(preference != null) preference.persist(i);
    }

    private SimplifiedDialogPreference getPreference() {

        if (getTargetFragment() != null && getArguments() != null) {
            return ((SimplifiedDialogPreference) ((PreferenceFragmentCompat) getTargetFragment()).findPreference(getArguments().getString(KEY)));
        }
        else {
            return null;
        }
    }
}
