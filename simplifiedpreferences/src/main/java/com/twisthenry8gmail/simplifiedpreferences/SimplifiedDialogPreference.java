package com.twisthenry8gmail.simplifiedpreferences;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;

@Deprecated
abstract class SimplifiedDialogPreference extends Preference {

    private static final String FRAGMENT_TAG = "settings_fragment";
    private static final String TAG = "dialog_preference";

    SimplifiedDialogPreference(Context context, AttributeSet attrs) {

        super(context, attrs);
    }

    protected abstract SimplifiedDialogPreferenceDialog onCreateDialog();

    @Override
    protected void onClick() {

        SimplifiedDialogPreferenceDialog fragment = onCreateDialog();
        FragmentManager fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
        if (fragment.getArguments() != null) {
            fragment.getArguments().putString(SimplifiedDialogPreferenceDialog.KEY, getKey());
        }
        fragment.setTargetFragment(fragmentManager.findFragmentByTag(FRAGMENT_TAG), 0);
        fragment.show(fragmentManager, TAG);
    }

    void persist(String s) {
        persistString(s);
    }

    void persist(int i) {
        persistInt(i);
    }
}
