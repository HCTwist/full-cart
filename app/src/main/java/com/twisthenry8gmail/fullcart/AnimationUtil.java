package com.twisthenry8gmail.fullcart;

import android.animation.ValueAnimator;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.transition.TransitionManager;

/**
 * Helper class for providing some compound animations
 */
class AnimationUtil {

    private static final int ANIMATION_ENTER = 150;
    private static final int ANIMATION_EXIT = 75;
    static final int ANIMATION_COMPLEX = 200;

    private AnimationUtil() {

    }

    /**
     * Fades a view out and then in again
     *
     * @param view           The view to animate
     * @param middleCallback Code to run after the view has exited, but before it has entered
     */
    static void fadeOutFadeIn(final View view, final Runnable middleCallback) {

        view.animate().alpha(0).setDuration(ANIMATION_EXIT).withEndAction(() -> {

            middleCallback.run();
            view.animate().alpha(1).setDuration(ANIMATION_ENTER);
        });
    }

    /**
     * Convenience method for {@link #push(View, boolean, boolean)}
     */
    static void push(View view, boolean enter) {

        push(view, enter, true);
    }

    /**
     * Compound animation that translates y and fades
     *
     * @param view  The view to animate
     * @param enter If set to true, the view will rise up and fade in, otherwise it will fade out and fall down
     * @param gone  Sets the view to {@value View#GONE} when exiting instead of {@value View#INVISIBLE}
     */
    static void push(final View view, boolean enter, boolean gone) {

        int translation = (int) Util.dpToPx(view.getContext(), 8);

        if (enter) {
            view.setAlpha(0);
            view.setVisibility(View.VISIBLE);
            view.setTranslationY(translation);
            view.animate().translationY(0).alpha(1).setInterpolator(new FastOutSlowInInterpolator()).setDuration(ANIMATION_ENTER);
        }
        else {
            view.animate().translationY(translation).alpha(0)
                    .setInterpolator(new FastOutSlowInInterpolator())
                    .setDuration(ANIMATION_EXIT)
                    .withEndAction(() -> view.setVisibility(gone ? View.GONE : View.INVISIBLE));
        }
    }

    /**
     * Compound animation that scales the view up and fades in
     *
     * @param view The view to animate
     */
    static void popIn(final View view) {

        view.setAlpha(0);
        view.setScaleX(0.8F);
        view.setScaleY(0.8F);
        view.animate().alpha(1).scaleX(1).scaleY(1).setDuration(AnimationUtil.ANIMATION_ENTER).setInterpolator(new FastOutSlowInInterpolator());
    }

    /**
     * Animate a smooth color change
     * @param startColor the starting color
     * @param endColor the end color
     * @param update the update logic
     */
    static void animateColor(int startColor, int endColor, ColorUpdate update) {

        ValueAnimator animator = ValueAnimator.ofArgb(startColor, endColor);
        animator.addUpdateListener(animation -> update.update((int) animation.getAnimatedValue()));
        animator.start();
    }

    /**
     * Transitions a drawable's background color
     *  @param startColor       The starting color
     * @param endColor         The end colour
     * @param gradientDrawable The drawable to animate
     */
    static void animateDrawableColor(int startColor, int endColor, final GradientDrawable gradientDrawable) {

        animateColor(startColor, endColor, gradientDrawable::setColor);
    }

    /**
     * Increases and decreases a view's size
     * @param view the view to be animated
     */
    static void scaleHighlight(View view) {

        view.animate().scaleX(1.2F).scaleY(1.2F).withEndAction(() -> view.animate().scaleX(1).scaleY(1));
    }

    static void animateConstraintVisibility(View view, int visibility) {

        if(view.getVisibility() != visibility) {
            ConstraintSet cs = new ConstraintSet();

            ConstraintLayout parent = (ConstraintLayout) view.getParent();
            cs.clone(parent);
            cs.setVisibility(view.getId(), visibility);
            TransitionManager.beginDelayedTransition(parent);
            cs.applyTo(parent);
        }
    }

    interface ColorUpdate {

        /**
         * Update views
         * @param color the current animated color value
         */
        void update(int color);
    }
}
