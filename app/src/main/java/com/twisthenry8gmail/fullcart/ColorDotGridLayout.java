package com.twisthenry8gmail.fullcart;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;

/**
 * Custom {@link GridLayout} to show a grid of color dots
 */
public class ColorDotGridLayout extends GridLayout {

    private static final int ANIMATION_ENTER = 150;
    private static final int ANIMATION_EXIT = 75;

    private int[] colors;

    private int selected = -1;

    private int colorDotWidth;
    private int minMargin;
    private boolean showCustomDot = false;

    private Runnable customDotClickListener;

    private GradientDrawable customDotBackground;
    private GradientDrawable customDotOutline;
    private ImageView customDotIcon;

    public ColorDotGridLayout(Context context) {

        super(context);
        init();
    }

    public ColorDotGridLayout(Context context, AttributeSet attrs) {

        super(context, attrs);
        init();
    }

    public ColorDotGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {

        super.onMeasure(widthSpec, heightSpec);

        int width = MeasureSpec.getSize(widthSpec);
        if (width > 0) {

            int columns = (int) Math.floor(((float) width + minMargin) / (colorDotWidth + minMargin));

            if (columns != getColumnCount()) {

                for (int i = 0; i < getChildCount(); i++) {

                    LayoutParams params = (LayoutParams) getChildAt(i).getLayoutParams();
                    int margin = (int) Math.floor(((float) width - columns * colorDotWidth) / (columns + 1));
                    params.setMarginStart(i % columns == 0 ? margin : 0);
                    params.setMarginEnd(margin);
                    params.bottomMargin = Math.ceil((float) getChildCount() / columns) == Math.ceil((float) (i + 1) / columns) ? 0 : Math.round((float) margin / 1.5F);
                    getChildAt(i).setLayoutParams(params);
                }
                setColumnCount(columns);
            }
        }
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {

        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.selected = selected;
        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {

        SavedState savedState = (SavedState) state;
        selected = savedState.selected;
        select(selected, false);
        super.onRestoreInstanceState(savedState.getSuperState());
    }

    /**
     * Initialisation code called in the constructor
     */
    private void init() {

        setColumnCount(1);
        colorDotWidth = getResources().getDimensionPixelSize(R.dimen.color_dot_width);
        minMargin = getResources().getDimensionPixelSize(R.dimen.color_dot_margin);
    }

    /**
     * Set the colors to be displayed on the grid
     *
     * @param colors            an array of resolved colors
     * @param allowCustomColors whether to have a dot for selecting custom colors
     */
    public void setColors(@ColorInt int[] colors, boolean allowCustomColors) {

        this.showCustomDot = allowCustomColors;
        if (allowCustomColors) {
            this.colors = new int[colors.length + 1];
            System.arraycopy(colors, 0, this.colors, 0, colors.length);
            this.colors[colors.length] = 0;
        }
        else {
            this.colors = colors;
        }

        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (int i = 0; i < colors.length; i++) {

            View dot = inflater.inflate(R.layout.color_dot, this, false);
            ((GradientDrawable) ((LayerDrawable) dot.findViewById(R.id.color_button).getBackground().mutate()).getDrawable(0))
                    .setColor(colors[i]);

            int finalI = i;
            dot.setOnClickListener(v -> select(finalI, true));

            addView(dot);
        }

        if (showCustomDot) {
            View customDot = inflater.inflate(R.layout.color_dot_custom, this, false);
            customDot.setOnClickListener(v -> {
                if (customDotClickListener != null) customDotClickListener.run();
            });
            addView(customDot);

            customDotBackground = (GradientDrawable) ((RippleDrawable) customDot
                    .findViewById(R.id.color_dot_custom_background).getBackground().mutate()).getDrawable(0);
            customDotOutline = (GradientDrawable) customDot.findViewById(R.id.color_dot_custom_outline).getBackground().mutate();
            customDotIcon = customDot.findViewById(R.id.color_dot_custom_icon);
        }
    }

    /**
     * Update the dots to reflect the current selection
     *
     * @param position the position to select
     * @param animate  whether to animate the selection
     */
    public void select(int position, boolean animate) {

        if (position != selected) {
            if (selected >= 0) toggleDot(selected, false, animate);
            toggleDot(position, true, animate);
        }
        selected = position;
    }

    private void toggleDot(int i, boolean select, boolean animate) {

        int alpha = select ? 1 : 0;
        int animationDuration = select ? ANIMATION_ENTER : ANIMATION_EXIT;

        if (showCustomDot && i == colors.length - 1) {

            View outline = getChildAt(i).findViewById(R.id.color_dot_custom_outline);
            if (animate) {
                outline.animate().setDuration(animationDuration).alpha(alpha);
            }
            else {
                outline.setAlpha(alpha);
            }

            if (!select) {
                updateCustomDot(ContextCompat.getColor(getContext(), R.color.custom_color_dot_default), animate);
            }
        }
        else {

            View dot = getChildAt(i).findViewById(R.id.color_button_tick);
            if (animate) {
                dot.animate().setDuration(animationDuration).alpha(alpha);
            }
            else {
                dot.setAlpha(alpha);
            }
        }
    }

    public void selectCustomColor(int color, boolean animate) {

        select(colors.length - 1, animate);
        updateCustomDot(color, animate);
        colors[colors.length - 1] = color;
    }

    private void updateCustomDot(int color, boolean animate) {

        int oldColor = colors[colors.length - 1];
        int oldOutlineColor = getCustomDotOutlineColor(oldColor);
        int outlineColor = getCustomDotOutlineColor(color);

        int oldIconColor = getCustomDotIconColor(oldColor);
        int iconColor = getCustomDotIconColor(color);

        int strokeSize = getResources().getDimensionPixelSize(R.dimen.color_dot_outline_width);

        if (animate) {

            ValueAnimator animatorBackground = ValueAnimator.ofArgb(oldColor, color);
            animatorBackground.setDuration(ANIMATION_ENTER);
            animatorBackground.addUpdateListener(animation -> customDotBackground.setColor((int) animation.getAnimatedValue()));
            animatorBackground.start();

            ValueAnimator animatorOutline = ValueAnimator.ofArgb(oldOutlineColor, outlineColor);
            animatorOutline.setDuration(ANIMATION_ENTER);
            animatorOutline.addUpdateListener(animation -> customDotOutline.setStroke(getResources().getDimensionPixelSize(
                    R.dimen.color_dot_outline_width), (int) animation.getAnimatedValue()));
            animatorOutline.start();

            ValueAnimator animatorIcon = ValueAnimator.ofArgb(oldIconColor, iconColor);
            animatorIcon.setDuration(ANIMATION_ENTER);
            animatorIcon.addUpdateListener(animation -> customDotIcon.setImageTintList(ColorStateList.valueOf((int) animation.getAnimatedValue())));
            animatorIcon.start();
        }
        else {

            if (showCustomDot) {
                customDotBackground.setColor(color);
                customDotOutline.setStroke(strokeSize, outlineColor);
                customDotIcon.setImageTintList(ColorStateList.valueOf(iconColor));
            }
        }
    }

    private int getCustomDotOutlineColor(int color) {

        boolean dark = ColorUtils.calculateLuminance(color) < 0.1;
        int b = dark ? 255 : 0;
        float f = dark ? 0.3F : 0.2F;

        int red = Math.round((1 - f) * Color.red(color) + f * b);
        int green = Math.round((1 - f) * Color.green(color) + f * b);
        int blue = Math.round((1 - f) * Color.blue(color) + f * b);

        return Color.rgb(red, green, blue);
    }

    private int getCustomDotIconColor(int color) {

        return ColorUtils.calculateLuminance(color) > 0.2 ? Color.BLACK : Color.WHITE;
    }

    public void setCustomDotClickListener(Runnable listener) {

        this.customDotClickListener = listener;
    }

    /**
     * @return the selected color
     */
    public int getSelectedColor() {

        return colors[selected];
    }

    public int getCustomColor() {

        return showCustomDot ? colors[colors.length - 1] : 0;
    }

    class SavedState extends BaseSavedState {

        int selected;

        SavedState(Parcelable superState) {

            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {

            super.writeToParcel(out, flags);
            out.writeInt(selected);
        }
    }
}
