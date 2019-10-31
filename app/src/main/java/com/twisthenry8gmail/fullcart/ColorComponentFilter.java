package com.twisthenry8gmail.fullcart;

import android.text.InputFilter;
import android.text.Spanned;

class ColorComponentFilter implements InputFilter {

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        String input = dest.toString().substring(0, dstart) + source.toString() + dest.toString().substring(dend, dest.length());

        if(input.isEmpty()) {
            return null;
        }

        if(input.startsWith("-")) {
            return "";
        }

        int n = Integer.parseInt(input);

        if(n > 255) {
            return "";
        }

        return null;
    }
}
