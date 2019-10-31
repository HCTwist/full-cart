package com.twisthenry8gmail.fullcart;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

class PantryContentViewHolder extends RecyclerView.ViewHolder implements ModelAdapter.Update {

    private final View dot;
    private final TextView quantity;
    private final ContentTextSwitcher main;
    private final ContentTextSwitcher extra;
    private final ImageButton increment;
    private final ImageButton decrement;

    private final ListFragmentInterface<ListItemPantry> listInterface;

    private ListItemPantry item;

    private boolean animatingQuantity = false;
    private int displayedQuantity = 0;
    private int displayQuantityTarget = 0;

    PantryContentViewHolder(View v, ListFragmentInterface<ListItemPantry> listInterface) {

        super(v);
        this.dot = v.findViewById(R.id.pantry_content_progress_dot);
        this.quantity = v.findViewById(R.id.pantry_content_quantity);
        this.main = v.findViewById(R.id.pantry_content_main);
        this.extra = v.findViewById(R.id.pantry_content_extra);
        this.increment = v.findViewById(R.id.pantry_content_increment);
        this.decrement = v.findViewById(R.id.pantry_content_decrement);

        this.listInterface = listInterface;
    }

    @Override
    public void generate(Model model) {

        item = (ListItemPantry) model.getItem();

        displayedQuantity = displayQuantityTarget = item.getQuantity();

        //Dot color
        int color = item.getCategory().getColor();
        ((GradientDrawable) dot.getBackground().mutate()).setColor(color);

        quantity.setTextColor(ColorUtil.getBlendedBody(color));
        quantity.setText(getQuantityText(item.getQuantity()));

        main.setCurrentText(item.getMainString());
        if(getExtraSpannable(item).length() == 0) {
            extra.setVisibility(View.GONE);
        }
        else {
            extra.setVisibility(View.VISIBLE);
            extra.setCurrentText(getExtraSpannable(item));
        }

        registerListeners();
    }

    @Override
    public void changed(Object payload) {

        ModelAdapter.ItemChange change = (ModelAdapter.ItemChange) payload;
        final ListItemPantry newItem = (ListItemPantry) change.item;

        if (!item.getMainString().equals(newItem.getMainString())) {
            main.setText(newItem.getMainString());
        }
        if (!item.getCategory().equals(newItem.getCategory()) || change.forceCategoryUpdate) {
            int oldColor = item.getCategory().getColor();
            int newColor = newItem.getCategory().getColor();
            AnimationUtil.animateDrawableColor(oldColor, newColor, (GradientDrawable) dot.getBackground());
            AnimationUtil.animateColor(ColorUtil.getBlendedBody(oldColor),
                    ColorUtil.getBlendedBody(newColor), quantity::setTextColor);
        }
        if (item.getQuantity() != newItem.getQuantity()) {
            animateQuantity(newItem.getQuantity());
        }

        //TODO Improve if comparing Spannable objects becomes feasible
        Spannable extraSpannable = getExtraSpannable(newItem);
        if(!getExtraSpannable(item).toString().equals(extraSpannable.toString())) {
            if(extraSpannable.length() == 0) {
                AnimationUtil.animateConstraintVisibility(extra, View.GONE);
            }
            else {
                AnimationUtil.animateConstraintVisibility(extra, View.VISIBLE);
                extra.setText(extraSpannable);
            }
        }
        else {
            extra.setCurrentText(extraSpannable);
        }

        this.item = newItem;
    }

    private void registerListeners() {

        decrement.setOnClickListener(v -> {

            if (item.getQuantity() > 1) {
                ListItemPantry newItem = new ListItemPantry(item.getName(), item.getCategory(), item.getNotes(), item.getQuantity() - 1, item.getDate(), item.getByDateType(), item.getByDate());
                listInterface.editItem(item, newItem);
            }
            else {

                listInterface.removeItem(item);
                AnchoredSnackbar.make(((Activity) itemView.getContext()).findViewById(R.id.fragment_pager), R.string.pantry_removed_prompt, Snackbar.LENGTH_LONG).setAction(R.string.undo, v1 -> {

                    ArrayList<ListItemPantry> items = new ArrayList<>(1);
                    items.add(item);
                    listInterface.addItems(items, false);
                }).show();
            }
        });

        increment.setOnClickListener(v -> {
            ListItemPantry newItem = new ListItemPantry(item.getName(), item.getCategory(), item.getNotes(), item.getQuantity() + 1, item.getDate(), item.getByDateType(), item.getByDate());
            listInterface.editItem(item, newItem);
        });

        itemView.setOnClickListener(v -> {

            Intent intent = ListItemActivityEditPantry.buildIntent(itemView.getContext(), item);
            ((MainActivity) itemView.getContext()).getPantryFragment().startActivityForResult(intent, RequestCodes.REQUEST_ITEM_EDIT);
        });
    }

    private Spannable getExtraSpannable(ListItemPantry item) {

        if (item.hasByDate()) {

            String notesString = item.getNotes().isEmpty() ? "" : " - " + item.getNotes();
            String byDateTypeString = item.getByDateTypeString(itemView.getContext());
            String byDateString = DateUtil.displayPantryDate(itemView.getContext(), item.getByDate());

            Spannable s = new SpannableString(byDateTypeString + byDateString + notesString);

            int diffPreference = SettingsFragment.getPreferredDateDifference(itemView.getContext());
            long diff = DateUtil.getCurrentSimpleDateDifference(item.getByDate());

            if (diff <= diffPreference) {

                int colorStart = byDateTypeString.length();
                int colorEnd = colorStart + byDateString.length();
                s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(itemView.getContext(), R.color.to_use)), colorStart, colorEnd, 0);
            }

            return s;
        }
        else {

            return new SpannableString(item.getNotes());
        }
    }

    private String getQuantityText(int q) {

        Rect bounds = new Rect();
        String qString = String.valueOf(q);
        quantity.getPaint().getTextBounds(qString, 0, qString.length(), bounds);
        if (bounds.width() + bounds.left <= itemView.getResources().getDimensionPixelSize(R.dimen.list_progress_dot_width)) {

            return qString;
        }
        else {

            return itemView.getResources().getString(R.string.large_quantity_placeholder);
        }
    }

    private static final int QUANTITY_ANIMATION_DURATION = 120;

    private void animateQuantity(int q) {

        displayQuantityTarget = q;

        if (!animatingQuantity) {

            int translation = quantity.getHeight() / 2;
            animatingQuantity = true;
            boolean incrementing = displayQuantityTarget > displayedQuantity;

            quantity.animate().translationY(incrementing ? translation : -translation).alpha(0)
                    .setDuration(QUANTITY_ANIMATION_DURATION / 2)
                    .setInterpolator(new AccelerateInterpolator())
                    .withEndAction(() -> {

                        quantity.setText(getQuantityText(displayQuantityTarget));
                        displayedQuantity = displayQuantityTarget;

                        quantity.setTranslationY(incrementing ? -translation : translation);
                        quantity.animate().translationY(0).alpha(1)
                                .setDuration(QUANTITY_ANIMATION_DURATION / 2)
                                .setInterpolator(new DecelerateInterpolator())
                                .withEndAction(() -> {

                                    animatingQuantity = false;

                                    if (displayedQuantity != displayQuantityTarget) {
                                        animateQuantity(displayQuantityTarget);
                                    }
                                });
                    });
        }
    }
}


