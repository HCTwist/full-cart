package com.twisthenry8gmail.fullcart;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.LayoutDirection;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

abstract class Util {

    static float dpToPx(Context context, int dp) {

        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    static boolean isRTL(Context context) {

        return context.getResources().getConfiguration().getLayoutDirection() == LayoutDirection.RTL;
    }

    static void showSoftKeyboard(final View view) {

        new Handler().postDelayed(() -> {

            InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            view.requestFocus();
            if (inputMethodManager != null) inputMethodManager.showSoftInput(view, 0);
        }, 200);
    }

    static void lockOrientation(AppCompatActivity activity) {

        activity.setRequestedOrientation(PreferenceManager.getDefaultSharedPreferences(activity)
                .getBoolean(SettingsFragment.KEY_LOCK_ROTATION, false)
                ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                : ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }
}
