package com.twisthenry8gmail.fullcart;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class StrikeTextView extends AppCompatTextView {

    private float mStrikeX = 0;
    private final Paint mStrikePaint;

    public StrikeTextView(Context context, AttributeSet attrs) {

        super(context, attrs);
        mStrikePaint = new Paint();
        mStrikePaint.setColor(getCurrentTextColor());
        mStrikePaint.setStrokeWidth(1.5F);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        boolean rtl = Util.isRTL(getContext());
        int startX = rtl ? getWidth() : 0;
        float stopX = rtl ? getWidth() - mStrikeX : mStrikeX;

        canvas.drawLine(startX, (float) getHeight() / 2, stopX, (float) getHeight() / 2, mStrikePaint);
    }

    public void strikeThrough(final boolean strikeThrough) {

        post(() -> {
            mStrikeX = strikeThrough ? getWidth() : 0;
            postInvalidate();
        });
    }

    public void animateStrikeThrough(boolean strikeThrough) {

        int duration = (int) (250 * ((getWidth() - mStrikeX)) / getWidth());
        if (!strikeThrough) {
            duration = 250 - duration;
        }

        ValueAnimator animator = ValueAnimator.ofFloat(mStrikeX, strikeThrough ? getWidth() : 0);
        animator.setDuration(duration);
        animator.addUpdateListener(valueAnimator -> {

            mStrikeX = (float) valueAnimator.getAnimatedValue();
            postInvalidate();
        });
        animator.start();
    }
}
