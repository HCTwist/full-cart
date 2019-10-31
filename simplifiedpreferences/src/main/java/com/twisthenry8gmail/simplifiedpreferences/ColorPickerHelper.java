package com.twisthenry8gmail.simplifiedpreferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;

@Deprecated
class ColorPickerHelper {

    private final Context context;

    private int startingColor = 0;
    private final int[] rgb = {0, 0, 0};

    private View dot;
    private final SeekBar[] seekBars = new SeekBar[3];

    ColorPickerHelper(Context context) {
        this.context = context;
    }

    void setRGB(int color) {

        rgb[0] = Color.red(color);
        rgb[1] = Color.green(color);
        rgb[2] = Color.blue(color);
    }

    int getStartingColor() {

        return startingColor;
    }

    void setStartingColor(int startingColor) {

        this.startingColor = startingColor;
        setRGB(startingColor);
    }

    int getColor() {

        return Color.rgb(rgb[0], rgb[1], rgb[2]);
    }

    View getDialogView() {

        @SuppressLint("InflateParams") View layout = LayoutInflater.from(context).inflate(R.layout.color_picker, null);

        dot = layout.findViewById(R.id.dot);
        seekBars[0] = layout.findViewById(R.id.r);
        seekBars[1] = layout.findViewById(R.id.g);
        seekBars[2] = layout.findViewById(R.id.b);

        for (int i = 0; i < 3; i++) {

            SeekBar seekBar = seekBars[i];

            final int finalI = i;
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    rgb[finalI] = progress;
                    updateDot();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            seekBar.setProgress(rgb[finalI]);
        }

        return layout;
    }

    void updateDot() {

        ((GradientDrawable) dot.getBackground().mutate()).setColor(getColor());
    }

    void updateSeekBars() {

        for (int i = 0; i < seekBars.length; i++) {
            seekBars[i].setProgress(rgb[i]);
        }
    }
}
