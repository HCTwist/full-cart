package com.twisthenry8gmail.fullcart;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ColorPickerDialogFragment extends DialogFragment {

    public static final String TAG = "color_picker";

    private static final String COLOR = "color";
    private static final String BAR_COLOR = "bar_color";

    private int[] rgb = {0, 0, 0};
    @ColorInt
    private int seekBarColor = 0;

    private boolean ignoreTextChange = false;

    private View palette;

    public static <F extends Fragment & ColorPickerSelectListener> ColorPickerDialogFragment getInstance(@ColorInt int color, @ColorInt int barColor, F fragment) {

        Bundle bundle = new Bundle();
        bundle.putInt(COLOR, color);
        bundle.putInt(BAR_COLOR, barColor);

        ColorPickerDialogFragment dialogFragment = new ColorPickerDialogFragment();
        dialogFragment.setArguments(bundle);
        dialogFragment.setTargetFragment(fragment, 0);

        return dialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        if (getContext() == null) {

            return super.onCreateDialog(savedInstanceState);
        }

        if (getArguments() != null) {
            int color = getArguments().getInt(COLOR);
            rgb[0] = Color.red(color);
            rgb[1] = Color.green(color);
            rgb[2] = Color.blue(color);

            seekBarColor = getArguments().getInt(BAR_COLOR);
        }

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());

        View layout = View.inflate(getContext(), R.layout.material_color_picker, null);
        ViewGroup pickerContainer = layout.findViewById(R.id.picker_container);
        palette = pickerContainer.getChildAt(0);
        updatePalette();

        for (int i = 1; i < 4; i++) {

            ViewGroup c = (ViewGroup) pickerContainer.getChildAt(i);

            final SeekBar bar = (SeekBar) c.getChildAt(1);
            final EditText input = (EditText) ((ViewGroup) ((ViewGroup) c.getChildAt(2)).getChildAt(0)).getChildAt(0);

            ColorStateList barColor = ColorStateList.valueOf(seekBarColor);
            bar.setThumbTintList(barColor);
            bar.setProgressTintList(barColor);
            bar.setMax(255);
            bar.setProgress(rgb[i - 1]);
            final int finalI = i;
            bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    if (fromUser) {
                        input.requestFocus();
                        ignoreTextChange = true;
                        input.setText("");
                        ignoreTextChange = true;
                        input.append(String.valueOf(progress));
                        rgb[finalI - 1] = progress;
                        updatePalette();
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            input.setFilters(new InputFilter[]{new ColorComponentFilter()});
            input.setText(String.valueOf(rgb[i - 1]));
            input.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    int n = s.toString().isEmpty() ? 0 : Integer.parseInt(s.toString());
                    if (!shouldIgnoreChange()) {
                        bar.setProgress(n);
                    }

                    rgb[finalI - 1] = n;
                    updatePalette();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        builder.setView(layout);

        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {

            if (getTargetFragment() != null) {
                ((ColorPickerSelectListener) getTargetFragment()).onConfirmColor(Color.rgb(rgb[0], rgb[1], rgb[2]));
            }
        });

        builder.setNegativeButton(android.R.string.cancel, null);

        return builder.create();
    }

    private boolean shouldIgnoreChange() {

        boolean ret = ignoreTextChange;
        ignoreTextChange = false;
        return ret;
    }

    private void updatePalette() {

        palette.setBackgroundColor(Color.rgb(rgb[0], rgb[1], rgb[2]));
    }

    interface ColorPickerSelectListener {

        void onConfirmColor(@ColorInt int color);
    }

    abstract class ColorPickerFragment extends Fragment implements ColorPickerSelectListener {}
}
