package com.twisthenry8gmail.fullcart;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A {@link RecyclerView.Adapter} and {@link SpinnerAdapter} to provide a list of categories
 */
class CategoriesAdapter extends EmptyRecyclerAdapter<CategoriesAdapter.ViewHolder> implements SpinnerAdapter {

    private final Context context;
    private final boolean showDefaultCategory;
    private final boolean showDefaultCategoryTag;
    private final ArrayList<Category> categories;

    CategoriesAdapter(Context context, boolean showDefaultCategory, boolean showDefaultCategoryTag) {

        this.context = context;
        this.showDefaultCategory = showDefaultCategory;
        this.showDefaultCategoryTag = showDefaultCategoryTag;
        categories = DatabaseHelper.getInstance(this.context).getCategories();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.category_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final Category category = (Category) getItem(position);

        initialiseView(holder, category);

        holder.itemView.setOnClickListener(view -> {

            CategoriesActivity activity = ((CategoriesActivity) context);

            CategoryBottomSheet dialogFragment = CategoryBottomSheet.getEditInstance(category);
            dialogFragment.show(activity.getSupportFragmentManager(), CategoryBottomSheet.TAG);
        });

        holder.itemView.setOnLongClickListener(view -> {

            int adapterPosition = holder.getAdapterPosition();
            if(!(showDefaultCategory && adapterPosition == 0)) {
                String message = context.getString(R.string.delete_category_message, category.getName());

                DeleteDialogFragment deleteDialogFragment = DeleteDialogFragment.getInstance(message, adapterPosition);
                deleteDialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), DeleteDialogFragment.TAG);
                return true;
            }
            else {
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {

        return categories.size() + (showDefaultCategory ? 1 : 0);
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {

        return getItemCount();
    }

    @Override
    public Object getItem(int i) {

        if(showDefaultCategory) {
            if (i == 0) {
                return Category.getNoCategory(context);
            }
            else {
                return categories.get(i - 1);
            }
        }
        else {
            return categories.get(i);
        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = attachViewHolder(i, R.layout.category_row_no_margins, view, viewGroup);

        return view;
    }

    @Override
    public View getDropDownView(int i, View view, ViewGroup viewGroup) {

        view = attachViewHolder(i, R.layout.category_row, view, viewGroup);

        return view;
    }

    @Override
    public int getViewTypeCount() {

        return 1;
    }

    @Override
    public boolean isEmpty() {

        return false;
    }

    /**
     * @return the list of categories being used
     */
    ArrayList<Category> getCategories() {

        return categories;
    }

    /**
     * Attach a view holder to a view and initialise that view
     * (calls {@link #initialiseView(ViewHolder, Category)})
     *
     * @param layout    a layout resource for a category row
     * @param view      the view to attach a view holder to
     * @param viewGroup the parent
     * @return the view
     */
    private View attachViewHolder(int i, @LayoutRes int layout, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(context).inflate(layout, viewGroup, false);
            view.setTag(new ViewHolder(view));
        }

        ViewHolder holder = (ViewHolder) view.getTag();

        Category category = (Category) getItem(i);
        initialiseView(holder, category);

        return view;
    }

    /**
     * Initialises a view holder with respect to a category
     *
     * @param holder   The view holder to initialise
     * @param category The category to display
     */
    private void initialiseView(ViewHolder holder, Category category) {

        ((GradientDrawable) holder.dot.getBackground().mutate()).setColor(category.getColor());
        holder.text.setText(category.getName());
        if(holder.defaultTag != null) {
            holder.defaultTag.setVisibility(showDefaultCategory && showDefaultCategoryTag && category.equals(Category.getNoCategory(context)) ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * Find out the adapter position a new category should be added in
     *
     * @param category The category to add
     * @return The position it belongs in
     */
    private int mockAddCategory(Category category) {

        for (int i = 0; i < categories.size(); i++) {
            Category c = categories.get(i);
            if (category.compareTo(c) <= 0) {
                return resolveCategoryAdapterPosition(i);
            }
        }
        return getItemCount();
    }

    /**
     * Add a category to the list
     *
     * @param category the category to add
     * @return the position the category was added in the data set (not necessarily in the list)
     */
    int addCategory(Category category) {

        int position = mockAddCategory(category);
        categories.add(resolveCategoryArrayPosition(position), category);
        notifyItemInserted(position);
        return position;
    }

    /**
     * Remove a category from the list
     *
     * @param position The position it is located
     */
    void deleteCategory(int position) {

        categories.remove(showDefaultCategory ? position - 1 : position);
        notifyItemRemoved(position);
    }

    /**
     * Edit a category in the list and animate it to it's new position
     *
     * @param oldCategory The category to quickAdd
     * @param newCategory The edited category
     */
    void editCategory(Category oldCategory, Category newCategory) {

        int oldArrayPosition = categories.indexOf(oldCategory);
        categories.remove(oldArrayPosition);

        int newPosition = mockAddCategory(newCategory);

        categories.add(resolveCategoryArrayPosition(newPosition), newCategory);
        notifyItemMoved(resolveCategoryAdapterPosition(oldArrayPosition), newPosition);
        notifyItemChanged(newPosition);
    }

    void editDefaultCategory() {

        if(showDefaultCategory) {

            notifyItemChanged(0);
        }
    }

    boolean showsDefaultCategory() {

        return showDefaultCategory;
    }

    private int resolveCategoryArrayPosition(int adapterPosition) {

        return adapterPosition - (showDefaultCategory ? 1 : 0);
    }

    private int resolveCategoryAdapterPosition(int arrayPosition) {

        return arrayPosition + (showDefaultCategory ? 1 : 0);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final ImageView dot;
        final TextView text;
        final View defaultTag;

        ViewHolder(View v) {

            super(v);
            dot = v.findViewById(R.id.category_row_dot);
            text = v.findViewById(R.id.category_row_text);
            defaultTag = v.findViewById(R.id.category_row_def_tag);
        }
    }
}
