package com.twisthenry8gmail.fullcart;

import android.view.View;

import androidx.annotation.StringRes;

import com.google.android.material.snackbar.Snackbar;

/**
 * A utility class for displaying snackbars above the main {@link com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton}
 */
class AnchoredSnackbar {

    private AnchoredSnackbar() {

    }

    /**
     * Wrapper for {@link Snackbar#make(View, int, int)} which anchors the snackbar above the FAB
     */
    static Snackbar make(View view, @StringRes int resId, int duration) {

        Snackbar snackbar = Snackbar.make(view, resId, duration);
        snackbar.setAnchorView(R.id.fab);
        return snackbar;
    }
}
