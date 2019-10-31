package com.twisthenry8gmail.fullcart;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.ViewHolder> {

    private static final int MAX_RESULTS = 20;

    private final Context context;
    private final View emptyView;

    private final ArrayList<ListItemSearch> items = new ArrayList<>();
    private final ArrayList<ListItemSearch> matches = new ArrayList<>();
    private String searchInput = "";

    private boolean hasQuickAdd = true;
    private final boolean showCategoryLetters;

    SearchRecyclerAdapter(Context context, View emptyView) {

        this.context = context;
        this.emptyView = emptyView;
        showCategoryLetters = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SettingsFragment.KEY_SHOW_CATEGORY_LETTERS, true);

        if (items.isEmpty()) loadItems();
        if (items.isEmpty()) {
            AnimationUtil.push(emptyView, true);
        }
        else {
            forceUpdate();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layout = LayoutInflater.from(context).inflate(R.layout.search_item, parent, false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.generateView(position);
    }

    @Override
    public int getItemCount() {

        return Math.min(matches.size() + (typingItemNeeded() ? 1 : 0), MAX_RESULTS);
    }

    ListItemSearch getItem(int position) {

        if (typingItemNeeded()) {
            return position == 0 ? new ListItemSearch(searchInput, Category.getNoCategory(context), 0, DateUtil.formatCurrentDate()) : matches.get(position - 1);
        }
        else {
            return matches.get(position);
        }
    }

    void update(String search) {

        boolean empty = search.isEmpty() && items.isEmpty();

        if ((!empty && emptyView.getVisibility() == View.VISIBLE) || (empty && emptyView.getVisibility() == View.GONE)) {

            AnimationUtil.push(emptyView, empty);
        }

        if (!search.equals(searchInput)) {

            searchInput = search;
            forceUpdate();
        }
    }

    private void forceUpdate() {

        matches.clear();

        for (ListItemSearch i : items) {

            if (i.getName().startsWith(searchInput.trim())) {
                matches.add(i);
            }
        }
        Collections.sort(matches);
        notifyDataSetChanged();
    }

    private boolean typingItemNeeded() {

        if (searchInput.isEmpty()) {
            return false;
        }
        else if (matches.isEmpty()) {
            return true;
        }
        else {

            Category noCategory = Category.getNoCategory(context);
            for (ListItemSearch item : matches) {

                if (item.getName().equals(searchInput.trim()) && item.getCategory().equals(noCategory)) {
                    return false;
                }
            }

            return true;
        }
    }

    private void loadItems() {

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        Cursor cursor = databaseHelper.getReadableDatabase().query(DatabaseHelper.SEARCH_TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {

            String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LIST_NAME));
            Category category = DatabaseHelper.getCategory(context, cursor.getString(cursor.getColumnIndex(DatabaseHelper.LIST_CATEGORY)));
            double frequency = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.SEARCH_FREQUENCY));
            String lastAdded = cursor.getString(cursor.getColumnIndex(DatabaseHelper.SEARCH_LAST_ADDED));

            items.add(new ListItemSearch(name, category, frequency, lastAdded));
        }
        cursor.close();
    }

    void setHasQuickAdd(boolean has) {

        hasQuickAdd = has;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final TextView dot;
        final TextView name;
        final View quickAdd;

        ViewHolder(View v) {

            super(v);
            dot = v.findViewById(R.id.search_item_dot);
            name = v.findViewById(R.id.search_item_name);
            quickAdd = v.findViewById(R.id.search_item_quick_button);
        }

        void generateView(final int position) {

            final ListItemSearch item = getItem(position);

            ((GradientDrawable) dot.getBackground().mutate()).setColor(item.getCategory().getColor());

            if (showCategoryLetters) {
                dot.setTextColor(ColorUtil.getBlendedBody(item.getCategory().getColor()));
                dot.setText(item.getCategory().equals(Category.getNoCategory(context))
                        ? itemView.getResources().getString(R.string.item_search_no_category_symbol)
                        : String.valueOf(item.getCategory().getName().charAt(0)).toUpperCase());
            }

            name.setText(item.getName());

            quickAdd.setVisibility(hasQuickAdd ? View.VISIBLE : View.GONE);
            if (hasQuickAdd) {
                quickAdd.setOnClickListener(v -> ((Callback) context).onSearchItemQuickAddClicked(item));
            }

            itemView.setOnClickListener(v -> ((Callback) context).onSearchItemClick(item));
        }
    }

    interface Callback {

        void onSearchItemClick(ListItemSearch item);

        void onSearchItemQuickAddClicked(ListItemSearch item);
    }
}
