package com.twisthenry8gmail.fullcart;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

class ShoppingContentViewHolder extends RecyclerView.ViewHolder implements ModelAdapter.Update {

    private final View dot;
    private final View filledDot;
    private final ImageView tick;
    private final ViewGroup contentBackground;
    private final ContentTextSwitcher main;
    private final ContentTextSwitcher extra;
    private final ImageButton removeButton;
    private final ImageButton transferButton;

    private final ListFragmentInterface<ListItemShopping> fragmentInterface;
    private final TransferInterface transferInterface;

    private ListItemShopping item;

    ShoppingContentViewHolder(View v, ListFragmentInterface<ListItemShopping> fragmentInterface, TransferInterface transferInterface) {

        super(v);

        this.fragmentInterface = fragmentInterface;
        this.transferInterface = transferInterface;

        dot = v.findViewById(R.id.list_content_progress_dot);
        filledDot = v.findViewById(R.id.list_content_progress_dot_filled);
        tick = v.findViewById(R.id.list_content_tick);
        contentBackground = v.findViewById(R.id.list_content_background);
        main = v.findViewById(R.id.list_content_main);
        extra = v.findViewById(R.id.list_content_extra);
        removeButton = v.findViewById(R.id.list_content_remove);
        transferButton = v.findViewById(R.id.list_content_pantry);
    }

    @Override
    public void generate(Model model) {

        this.item = (ListItemShopping) model.getItem();

        //Content
        main.setCurrentText(item.getMainString());

        if(item.getExtraString().isEmpty()) {
            extra.setVisibility(View.GONE);
        }
        else {
            extra.setVisibility(View.VISIBLE);
            extra.setCurrentText(item.getExtraString());
        }

        //Dot color
        int color = item.getCategory().getColor();
        LayerDrawable dotBackground = (LayerDrawable) dot.getBackground().mutate();
        ((GradientDrawable) dotBackground.getDrawable(0)).setColor(color);
        ((GradientDrawable) filledDot.getBackground().mutate()).setColor(color);

        filledDot.setAlpha(item.isChecked() ? 1 : 0);

        removeButton.setAlpha(1F);
        removeButton.setVisibility(item.isChecked() ? View.INVISIBLE : View.VISIBLE);
        transferButton.setAlpha(1F);
        transferButton.setVisibility(item.isChecked() ? View.VISIBLE : View.INVISIBLE);

        tick.getDrawable().mutate().setColorFilter(ColorUtil.getBlendedBody(color), PorterDuff.Mode.SRC_ATOP);
        tick.setScaleY(item.isChecked() ? 1 : 0);
        tick.setScaleX(item.isChecked() ? 1 : 0);

        registerListeners();
    }

    @Override
    public void changed(Object payload) {

        ModelAdapter.ItemChange itemChange = (ModelAdapter.ItemChange) payload;
        final ListItemShopping newItem = (ListItemShopping) itemChange.item;

        if (item.isChecked() != newItem.isChecked()) {
            animateCheckItem(newItem.isChecked());
        }
        if (!item.getCategory().equals(newItem.getCategory()) || itemChange.forceCategoryUpdate) {

            int oldColor = item.getCategory().getColor();
            int newColor = newItem.getCategory().getColor();
            AnimationUtil.animateDrawableColor(oldColor, newColor,
                    ((GradientDrawable) ((LayerDrawable) dot.getBackground().mutate()).getDrawable(0)));
            AnimationUtil.animateDrawableColor(oldColor, newColor, (GradientDrawable) filledDot.getBackground());
            AnimationUtil.animateColor(ColorUtil.getBlendedBody(oldColor),
                    ColorUtil.getBlendedBody(newColor),
                    color -> tick.getDrawable().mutate().setColorFilter(color, PorterDuff.Mode.SRC_ATOP));
        }
        if (!item.getMainString().equals(newItem.getMainString())) {
            main.setText(newItem.getMainString());
        }
        if (!item.getExtraString().equals(newItem.getExtraString())) {

            if(newItem.getExtraString().isEmpty()) {
                AnimationUtil.animateConstraintVisibility(extra, View.GONE);
            }
            else {
                AnimationUtil.animateConstraintVisibility(extra, View.VISIBLE);
                extra.setText(newItem.getNotes());
            }
        }

        this.item = newItem;
    }

    private void registerListeners() {

        dot.setOnClickListener(v -> {

            ListItemShopping checkedItem = new ListItemShopping(item);
            checkedItem.setChecked(!item.isChecked());
            fragmentInterface.editItem(item, checkedItem);
        });

        removeButton.setOnClickListener(v -> fragmentInterface.removeItem(item));

        transferButton.setOnClickListener(v -> {

            transferInterface.startTransfer(item);

            ListItemPantry.ByDateType type = SettingsFragment.getDefaultByDateType(itemView.getContext());

            if (PreferenceManager.getDefaultSharedPreferences(itemView.getContext()).getBoolean(SettingsFragment.KEY_ASK_USE_BY, true)) {

                ByDatePicker datePickerDialog = ByDatePicker.getInstance("", type);
                datePickerDialog.show(((AppCompatActivity) itemView.getContext()).getSupportFragmentManager(), ByDatePicker.TAG);
            }
            else {

                transferInterface.completeTransfer("");
            }
        });

        //Edit
        contentBackground.setOnClickListener(v -> {

            Intent intent = ListItemActivityEditShopping.buildIntent(itemView.getContext(), item);
            ((MainActivity) itemView.getContext()).getShoppingFragment().startActivityForResult(intent, RequestCodes.REQUEST_ITEM_EDIT);
        });
    }

    private void animateCheckItem(final boolean checked) {

        int tension = itemView.getContext().getResources().getInteger(R.integer.shopping_list_tick_animation_tension);
        int translation = itemView.getContext().getResources().getDimensionPixelSize(R.dimen.shopping_list_button_animation_translation);

        if (checked) {
            filledDot.setAlpha(0);
            filledDot.animate().alpha(1).setListener(null);
            tick.animate().scaleY(1).scaleX(1).setInterpolator(new OvershootInterpolator(tension)).setStartDelay(100);
        }
        else {
            filledDot.animate().alpha(0);
            tick.animate().scaleY(0).scaleX(0).setInterpolator(new AnticipateInterpolator(tension)).setStartDelay(0);
        }

        View exitButton = checked ? removeButton : transferButton;
        View entryButton = checked ? transferButton : removeButton;

        exitButton.animate().alpha(0).translationX(translation).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                exitButton.setVisibility(View.INVISIBLE);
                exitButton.setTranslationX(0);
            }
        });

        entryButton.setAlpha(0F);
        entryButton.setVisibility(View.VISIBLE);
        entryButton.setTranslationX(translation);
        entryButton.animate().alpha(1).translationX(0).setListener(null);
    }

    interface TransferInterface {

        void startTransfer(ListItemShopping item);

        void completeTransfer(String date);
    }
}

