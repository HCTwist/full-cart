package com.twisthenry8gmail.fullcart;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.AlphaAnimation;
import android.widget.TextSwitcher;
import android.widget.TextView;

/**
 * A custom {@link TextSwitcher} that generates {@link TextView}s for use on the shopping/pantry lists
 */
public class ContentTextSwitcher extends TextSwitcher {

    public ContentTextSwitcher(Context context, AttributeSet attrs) {

        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ContentTextSwitcher, 0, 0);

        setFactory(() -> {

            int layoutRes = typedArray.getResourceId(R.styleable.ContentTextSwitcher_textViewLayout, -1);
            if (layoutRes == -1) {
                return new TextView(context);
            }
            else {
                return LayoutInflater.from(context).inflate(layoutRes,this,false);
            }
        });

        AlphaAnimation in = new AlphaAnimation(0, 1);
        in.setDuration(AnimationUtil.ANIMATION_COMPLEX);
        AlphaAnimation out = new AlphaAnimation(1, 0);
        out.setDuration(AnimationUtil.ANIMATION_COMPLEX);

        setInAnimation(in);
        setOutAnimation(out);

        typedArray.recycle();
    }
}
