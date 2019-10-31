package com.twisthenry8gmail.fullcart;

import android.view.animation.Interpolator;

/**
 * Interpolator that simulates bouncing when set to a translation animation
 */
class BallBounceInterpolator implements Interpolator {

    private final int n;

    BallBounceInterpolator(int n) {

        this.n = n;
    }

    @Override
    public float getInterpolation(float x) {

        return (float) Math.abs(1.628 * Math.exp(-(0.5 * n * x + 0.25)) * Math.cos(2 * Math.PI * (0.5 * n * x + 0.25)));
    }
}
