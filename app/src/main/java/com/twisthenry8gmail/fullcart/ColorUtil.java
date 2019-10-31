package com.twisthenry8gmail.fullcart;

import android.graphics.Color;

import androidx.annotation.ColorInt;
import androidx.core.graphics.ColorUtils;

/**
 * Utility class to help with colors
 */
class ColorUtil {

    private ColorUtil() {

    }

    /**
     * Check whether a color is dark. A color is deemed dark if it would need a white foreground to
     * stand out
     *
     * @param color the color to check
     * @return true if the color is dark
     */
    private static boolean isDark(@ColorInt int color) {

        return ColorUtils.calculateLuminance(color) < 0.2;
    }

    /**
     * @param background the background to check
     * @return resolved black or white, depending which contrasts the background the best
     */
    static @ColorInt
    int getContrastingWhiteBlack(@ColorInt int background) {

        return ColorUtils.calculateLuminance(background) > 0.25 ? Color.BLACK : Color.WHITE;
    }

    static @ColorInt
    int getBlendedBody(@ColorInt int background) {

        boolean dark = isDark(background);
        int b = dark ? 255 : 0;
        float f = dark ? 0.8F : 0.7F;

        int red = Math.round((1 - f) * Color.red(background) + f * b);
        int green = Math.round((1 - f) * Color.green(background) + f * b);
        int blue = Math.round((1 - f) * Color.blue(background) + f * b);

        return Color.rgb(red, green, blue);
    }

    /**
     * Calculates a color 20% darker
     *
     * @param color the color to darken
     * @return the darkened color
     */
    static @ColorInt
    int darkenColor(@ColorInt int color) {

        return brightenColor(color, 0.8F);
    }

    /**
     * Change the brightness of a color
     *
     * @param color  the color to change
     * @param factor how much to brighten the color, above 1 to brighten or below to darken
     * @return the edited color
     */
    private @ColorInt
    static int brightenColor(@ColorInt int color, float factor) {

        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.rgb(Math.min(r, 255), Math.min(g, 255), Math.min(b, 255));
    }

    /**
     * Calculate a color to use on a text field for the hint that contrasts with a background color
     *
     * @param color the background color
     * @return the hint color
     */
    static @ColorInt
    int getHintColor(@ColorInt int color) {

        float lum = (float) ColorUtils.calculateLuminance(color);

        if (lum < 0.5) {
            return Color.HSVToColor(new float[]{0, 0, lum + 0.4F});
        }
        else {
            return Color.HSVToColor(new float[]{0, 0, lum - 0.4F});
        }
    }

    /**
     * Darken a color if it is dark as determined by {@link #isDark(int)}
     *
     * @param color the color to potentially darken
     * @return the perhaps darkened color
     */
    static @ColorInt
    int darkenColorIfTooLight(@ColorInt int color) {

        return isDark(color) ? color : darkenColor(darkenColor(color));
    }
}
