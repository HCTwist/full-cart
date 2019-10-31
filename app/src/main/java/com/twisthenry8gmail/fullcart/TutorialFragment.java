package com.twisthenry8gmail.fullcart;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

public class TutorialFragment extends Fragment {

    private static final String TYPE = "type";

    private static final int FIRST = 0;
    private static final int MIDDLE = 1;
    private static final int LAST = 2;

    private static final String IMAGE = "image";
    private static final String TITLE = "title";
    private static final String BODY = "body";

    static TutorialFragment getFirstInstance() {

        TutorialFragment tutorialFragment = new TutorialFragment();

        Bundle args = new Bundle();
        args.putInt(TYPE, FIRST);
        args.putInt(TITLE, R.string.tutorial_0_title);

        tutorialFragment.setArguments(args);

        return tutorialFragment;
    }

    static TutorialFragment getMiddleInstance(@DrawableRes int image, @StringRes int title, @StringRes int body) {

        TutorialFragment tutorialFragment = new TutorialFragment();

        Bundle args = new Bundle();
        args.putInt(TYPE, MIDDLE);
        args.putInt(TITLE, title);
        args.putInt(IMAGE, image);
        args.putInt(BODY, body);

        tutorialFragment.setArguments(args);

        return tutorialFragment;
    }

    static TutorialFragment getLastInstance() {

        TutorialFragment tutorialFragment = new TutorialFragment();

        Bundle args = new Bundle();
        args.putInt(TYPE, LAST);
        args.putInt(TITLE, R.string.tutorial_5_title);
        args.putInt(BODY, R.string.tutorial_5_body);

        tutorialFragment.setArguments(args);

        return tutorialFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getArguments() == null) {

            return null;
        }

        int res;

        int type = getArguments().getInt(TYPE);
        if (type == FIRST) {
            res = R.layout.tutorial_first_page;
        }
        else if (type == MIDDLE) {
            res = R.layout.tutorial_page;
        }
        else {
            res = R.layout.tutorial_last_page;
        }

        View layout = inflater.inflate(res, container, false);

        TextView titleView = layout.findViewById(R.id.tutorial_page_title);
        int titleRes = getArguments().getInt(TITLE);
        titleView.setText(titleRes);

        if (type == FIRST && getActivity() != null) {

            final View arrow = layout.findViewById(R.id.tutorial_page_arrow);
            final View text = layout.findViewById(R.id.tutorial_page_arrow_text);

            int margin = getResources().getDimensionPixelSize(R.dimen.tutorial_arrow_margin);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int screenSize = displayMetrics.widthPixels;

            final int duration = 2000;
            final float alphaInFraction = (float) 0.1;
            final float alphaOutFraction = (float) 0.7;

            ObjectAnimator translateAnimator = ObjectAnimator.ofFloat(arrow, "translationX", (float) screenSize / 2 - margin, margin - (float) screenSize / 2);
            translateAnimator.setRepeatCount(ValueAnimator.INFINITE);
            translateAnimator.setDuration(duration);
            translateAnimator.setInterpolator(new FastOutSlowInInterpolator());

            translateAnimator.addUpdateListener(valueAnimator -> {

                float fraction = valueAnimator.getAnimatedFraction();
                if (fraction < alphaInFraction) {

                    arrow.setAlpha(fraction / alphaInFraction);
                }
                else if (fraction > alphaOutFraction) {

                    arrow.setAlpha((1 - ((1 / (1 - alphaOutFraction)) * (fraction - alphaOutFraction))));
                }
            });

            final int[] i = {0};

            translateAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                    animator.setStartDelay(duration);
                }

                @Override
                public void onAnimationEnd(Animator animator) {

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(final Animator animator) {

                    i[0]++;
                    if (i[0] == 4) {

                        AnimationUtil.push(text, true);
                        animator.pause();

                        new Handler().postDelayed(() -> {

                            AnimationUtil.push(text, false);
                            animator.start();
                        }, duration * 2);

                        i[0] = 0;
                    }
                }
            });

            translateAnimator.start();
        }

        if (type == MIDDLE && getContext() != null) {
            ImageView image = layout.findViewById(R.id.tutorial_page_image);
            image.setImageDrawable(ContextCompat.getDrawable(getContext(), getArguments().getInt(IMAGE)));
            image.setContentDescription(getString(titleRes));
        }

        if (type == MIDDLE || type == LAST) {
            TextView body = layout.findViewById(R.id.tutorial_page_body);
            body.setText(getArguments().getInt(BODY));
        }

        if (type == LAST) {
            Button getStarted = layout.findViewById(R.id.tutorial_page_get_started);
            getStarted.setOnClickListener(view -> {

                if (getActivity() != null) {
                    getActivity().finish();
                }
            });
        }

        return layout;
    }
}
