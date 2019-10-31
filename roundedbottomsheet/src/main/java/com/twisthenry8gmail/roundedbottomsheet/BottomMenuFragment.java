package com.twisthenry8gmail.roundedbottomsheet;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Bottom sheet to display a simple menu. This should not be manually instantiated and should instead
 * use {@link #getInstance(int[], int[], int, int)}
 */
public class BottomMenuFragment extends BottomRoundedFragment {

    private static final String ICONS = "icons";
    private static final String TEXTS = "texts";
    private static final String ROW_TINT = "icon_color";
    private static final String RIPPLE_COLOR = "ripple_color";

    /**
     * Creates an instance of this class
     *
     * @param icons       array of drawable resources icons to display alongside the text
     * @param texts       array of string resources to display alongside the icons
     * @param rowTint     the icon tint
     * @param rippleColor a guideline ripple color when a row is highlighted. This will be lightened
     * @return an instance of this class
     */
    public static BottomMenuFragment getInstance(@DrawableRes int[] icons, @DrawableRes int[] texts, @ColorRes int rowTint, @ColorRes int rippleColor) {

        BottomMenuFragment dialogFragment = new BottomMenuFragment();
        Bundle arguments = new Bundle();
        arguments.putIntArray(ICONS, icons);
        arguments.putIntArray(TEXTS, texts);
        arguments.putInt(ROW_TINT, rowTint);
        arguments.putInt(RIPPLE_COLOR, rippleColor);
        dialogFragment.setArguments(arguments);
        return dialogFragment;
    }

    @Override
    protected View getContent(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (getContext() == null || getArguments() == null) {

            throw new RuntimeException("BottomMenuFragment not instantiated correctly");
        }

        RecyclerView recyclerView = new RecyclerView(getContext());
        recyclerView.setAdapter(new Adapter(
                getArguments().getIntArray(ICONS), getArguments().getIntArray(TEXTS),
                getArguments().getInt(ROW_TINT), getArguments().getInt(RIPPLE_COLOR)
        ));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        return recyclerView;
    }

    public interface MenuClickListener {

        void menuItemSelected(String tag, int position);
    }

    class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        final int[] icons;
        final int[] texts;
        @ColorRes
        final int rowTint;
        @ColorInt
        int rippleColor;

        Adapter(int[] icons, int[] texts, @ColorRes int rowTint, @ColorRes int rippleColor) {

            this.icons = icons;
            this.texts = texts;
            this.rowTint = rowTint;

            if (getContext() != null) {
                int rColor = ContextCompat.getColor(getContext(), rippleColor);
                float factor = 0.8F;
                int r = Color.red(rColor);
                int g = Color.green(rColor);
                int b = Color.blue(rColor);
                r = Math.round(r + (255 - r) * factor);
                g = Math.round(g + (255 - g) * factor);
                b = Math.round(b + (255 - b) * factor);
                this.rippleColor = Color.rgb(Math.min(r, 255), Math.min(g, 255), Math.min(b, 255));
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View row = LayoutInflater.from(getContext()).inflate(R.layout.row, parent, false);
            return new ViewHolder(row);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            holder.generateView(position);
        }

        @Override
        public int getItemCount() {

            return Math.min(icons.length, texts.length);
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            final ImageView icon;
            final TextView text;

            ViewHolder(View v) {

                super(v);
                icon = v.findViewById(R.id.icon);
                text = v.findViewById(R.id.text);
            }

            void generateView(final int position) {

                if (getContext() != null) {

                    ((RippleDrawable) itemView.getBackground().mutate()).setColor(ColorStateList.valueOf(rippleColor));
                    icon.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), rowTint)));
                    icon.setImageDrawable(ContextCompat.getDrawable(getContext(), icons[position]));
                    text.setTextColor(ContextCompat.getColor(getContext(), rowTint));
                }
                text.setText(texts[position]);
                if (getActivity() != null) {
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            ((MenuClickListener) getActivity()).menuItemSelected(getTag(), position);
                            BottomMenuFragment.this.dismiss();
                        }
                    });
                }
            }
        }
    }
}

