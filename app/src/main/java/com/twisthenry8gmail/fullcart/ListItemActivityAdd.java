package com.twisthenry8gmail.fullcart;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.HorizontalScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link ListItemActivity} for adding items
 *
 * @param <I>
 */
abstract class ListItemActivityAdd<I extends ListItem> extends ListItemActivity implements SearchRecyclerAdapter.Callback {

    static final String ITEMS = "items";
    private static final String CURRENT = "current";
    private static final String SEARCH_SHOWN = "search_shown";

    private static final int MINIMUM_ITEMS_FOR_EXIT_PROMPT = 1;

    private View itemEditContainer;

    private View searchContainer;
    private EditText searchBox;

    private SearchRecyclerAdapter searchAdapter;
    private LinearLayoutManager searchLayoutManager;

    private boolean searchVisible = false;

    private HorizontalScrollView horizontalScrollView;
    private ChipGroup chipGroup;

    private FloatingActionButton addButton;

    ArrayList<I> items = new ArrayList<>();

    int current = 0;

    private boolean ignoreNextSearchTextChange = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        itemEditContainer = findViewById(R.id.item_edit_container);

        searchContainer = findViewById(R.id.item_search_container);
        searchBox = findViewById(R.id.item_search_box);
        View closeSearchView = findViewById(R.id.item_search_close);
        RecyclerView searchResults = findViewById(R.id.item_search_results);

        View emptySearchView = findViewById(R.id.item_search_empty);
        searchAdapter = new SearchRecyclerAdapter(this, emptySearchView);
        searchLayoutManager = new LinearLayoutManager(this);
        searchResults.setLayoutManager(searchLayoutManager);
        searchResults.setHasFixedSize(true);
        searchResults.setAdapter(searchAdapter);

        horizontalScrollView = findViewById(R.id.item_chip_scroll);
        chipGroup = findViewById(R.id.item_chip_group);
        addButton = findViewById(R.id.item_add);

        if (savedInstanceState != null) {

            ignoreNextSearchTextChange = true;
            ignoreNextCategoryChange = true;
            items = savedInstanceState.getParcelableArrayList(ITEMS);
            current = savedInstanceState.getInt(CURRENT);

            if (!items.isEmpty()) {

                for (I item : items) {
                    addChip(item);
                }
                highlightChip(current);
                load(false);
                toggleSearch(savedInstanceState.getBoolean(SEARCH_SHOWN));
            }
            else {
                toggleSearch(true);
            }
        }
        else {

            toggleSearch(true);
        }

        nameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (items.size() > 0) {
                    getCurrentChip().setText(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        addButton.setOnClickListener(v -> toggleSearch(true));

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (ignoreNextSearchTextChange) {
                    ignoreNextSearchTextChange = false;
                }
                else {
                    searchAdapter.update(s.toString().trim());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchBox.setOnKeyListener((view, keyCode, event) -> {

            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER && searchAdapter.getItemCount() > 0) {

                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (manager != null) {
                        manager.hideSoftInputFromWindow(searchBox.getWindowToken(), 0);
                    }
                }
                onSearchItemClick(searchAdapter.getItem(0));
                return true;
            }

            return false;
        });

        closeSearchView.setOnClickListener(v -> {
            if (!items.isEmpty()) {
                toggleSearch(false);
            }
            searchBox.setText("");
        });

        finishButton.setOnClickListener(v -> saveAndClose());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        save();
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(ITEMS, items);
        outState.putInt(CURRENT, current);
        outState.putBoolean(SEARCH_SHOWN, searchContainer.getVisibility() == View.VISIBLE);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
        searchAdapter.update(searchBox.getText().toString());
    }

    @Override
    public void onBackPressed() {

        if(items.size() > MINIMUM_ITEMS_FOR_EXIT_PROMPT) {
            showExitDialog();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    void onNavigationBack() {

        if(items.size() > MINIMUM_ITEMS_FOR_EXIT_PROMPT) {
            showExitDialog();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    int getToolbarTitleResource() {

        return R.string.title_add_item;
    }

    @Override
    public void onSearchItemClick(ListItemSearch item) {

        save();

        addNewItem(item.getName(), item.getCategory());
        addChip(item);

        loadChip(items.size() - 1);

        toggleSearch(false);
    }

    @Override
    public void onSearchItemQuickAddClicked(ListItemSearch item) {

        addNewItem(item.getName(), item.getCategory());
        sendResult();
    }

    @Override
    void setColors(int accent) {

        super.setColors(accent);
        getCurrentChip().setChipBackgroundColor(ColorStateList.valueOf(accent));
        getCurrentChip().setChipStrokeColor(ColorStateList.valueOf(ColorUtil.darkenColor(accent)));
        getCurrentChip().setTextColor(ColorUtil.getBlendedBody(accent));
    }

    private void saveAndClose() {

        save();

        if (!checkAndDisplayError()) {

            combineItems();
            sendResult();
        }
    }

    private void showExitDialog() {

        ExitDialogFragment exitDialogFragment = ExitDialogFragment.getInstance(new ExitDialogFragment.ExitCallback() {
            @Override
            public void discardChanges() {

                finish();
            }

            @Override
            public void saveChanges() {

                saveAndClose();
            }
        });
        exitDialogFragment.show(getSupportFragmentManager(), ExitDialogFragment.TAG);
    }

    /**
     * Hide or show the search screen and hide all other views. This also shows the soft keyboard if
     * showing the search
     *
     * @param show if set to true the search will be shown, otherwise the search will be hidden
     */
    private void toggleSearch(boolean show) {

        searchVisible = show;

        itemEditContainer.setVisibility(show ? View.GONE : View.VISIBLE);
        horizontalScrollView.setVisibility(show ? View.GONE : View.VISIBLE);
        searchContainer.setVisibility(show ? View.VISIBLE : View.GONE);

        if (show) {
            searchLayoutManager.scrollToPositionWithOffset(0, 0);
            searchAdapter.setHasQuickAdd(items.isEmpty());
            searchBox.requestFocus();
            Util.showSoftKeyboard(searchBox);

            finishButton.hide();
            addButton.hide();
        }
        else {
            nameView.requestFocus();

            addButton.show();
            finishButton.show();
        }
    }

    /**
     * Set the current date on all the added items, then send the result back to the list fragment
     * with the key {@value ITEMS}
     *
     * @see androidx.fragment.app.Fragment#onActivityResult(int, int, Intent)
     */
    void sendResult() {

        String formatDate = DateUtil.formatCurrentDate();
        for (I item : items) {

            item.setDate(formatDate);
        }

        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(ITEMS, items);

        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Add a chip for a certain item
     *
     * @param item the item to style the chip for
     */
    private void addChip(ListItemBase item) {

        final Chip chip = (Chip) LayoutInflater.from(this).inflate(R.layout.item_chip, chipGroup, false);

        chip.setText(item.getName());
        int color = item.getCategory().getColor();
        chip.setChipBackgroundColor(ColorStateList.valueOf(color));
        chip.setChipStrokeColor(ColorStateList.valueOf(ColorUtil.darkenColor(color)));
        chip.setTextColor(ColorUtil.getBlendedBody(color));

        chip.setOnClickListener(v -> {

            if (searchVisible) {

                toggleSearch(false);
            }

            switchToChip(chipGroup.indexOfChild(chip));
        });

        chip.setCloseIconVisible(true);
        chip.setOnCloseIconClickListener(view -> delete(chip));

        chipGroup.addView(chip);

        AnimationUtil.popIn(chip);
        searchBox.setText("");

        chipGroup.post(() -> horizontalScrollView.fullScroll(View.FOCUS_RIGHT));
    }

    /**
     * @return the chip that is currently selected
     */
    private Chip getCurrentChip() {

        return (Chip) chipGroup.getChildAt(current);
    }

    /**
     * Highlight a chip and load the content associated with it
     *
     * @param position the position of the item to select
     */
    private void loadChip(int position) {

        current = position;
        highlightChip(current);
        load(false);
    }

    /**
     * Save the current content and then load the new chip
     *
     * @param position the position of the item to switch to
     * @see #loadChip(int)
     */
    private void switchToChip(int position) {

        save();
        loadChip(position);
    }

    /**
     * Highlight a chip
     *
     * @param position the position of the chip to highlight
     */
    private void highlightChip(int position) {

        for (int i = 0; i < chipGroup.getChildCount(); i++) {

            Chip chip = ((Chip) chipGroup.getChildAt(i));
            chip.setChipStrokeWidthResource(position == i ? R.dimen.item_chip_stroke : R.dimen.item_chip_no_stroke);
        }
    }

    /**
     * Delete an item. If the item is currently selected then the previous chip is selected, and if
     * the chip is the only one, the search is toggled
     *
     * @param chip the chip associated with the item to delete
     */
    private void delete(Chip chip) {

        int position = chipGroup.indexOfChild(chip);
        items.remove(position);
        chipGroup.removeView(chip);

        if (chipGroup.getChildCount() == 0) {
            toggleSearch(true);
            searchAdapter.setHasQuickAdd(true);
            searchAdapter.notifyDataSetChanged();
        }
        else {
            boolean wasChecked = position == current;
            current = position == 0 ? 0 : position - 1;
            if (wasChecked) {
                highlightChip(current);
                load(false);
            }
        }
    }

    /**
     * Save the current content
     */
    private void save() {

        if (!items.isEmpty()) {
            I item = items.get(current);
            item.setName(getName());
            item.setCategory(getCategory());
            item.setNotes(getNotes());
            item.setQuantity(getQuantity());
        }
    }

    /**
     * Load the content of the item currently set as selected
     *
     * @param animate whether to animate the change
     */
    private void load(boolean animate) {

        I item = items.get(current);
        fillFields(item.getName(), item.getCategory(), item.getNotes(), item.getQuantity(), animate);
    }

    /**
     * Display an error if necessary
     *
     * @return whether an error was displayed
     */
    private boolean checkAndDisplayError() {

        if (items.get(current).getName().trim().isEmpty()) {
            AnimationUtil.scaleHighlight(errorView);
            return true;
        }

        for (int i = 0; i < items.size(); i++) {

            I item = items.get(i);

            if (item.getName().trim().isEmpty()) {
                if (i != current) switchToChip(i);
                AnimationUtil.scaleHighlight(errorView);
                return true;
            }
        }

        return false;
    }

    /**
     * Combine similar items in the list to remove the unnecessary work later on. This mutates the
     * items list so should only be called when the activity is finishing
     */
    private void combineItems() {

        List<Integer> toRemove = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {

            I item1 = items.get(i);

            for (int j = i + 1; j < items.size(); j++) {

                I item2 = items.get(j);

                if (item1.isSimilarTo(item2)) {

                    item2.setQuantity(item2.getQuantity() + item1.getQuantity());
                    toRemove.add(i);
                    break;
                }
            }
        }

        for (int i = toRemove.size() - 1; i >= 0; i--) {

            items.remove((int) toRemove.get(i));
        }
    }

    /**
     * Should add a blank item
     *
     * @param name     the name for the item
     * @param category the category for the item
     */
    abstract void addNewItem(String name, Category category);
}
