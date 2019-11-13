package com.twisthenry8gmail.fullcart;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Activity for displaying a list of categories with delete and add actions. This should be instantiated
 * with the {@link #buildIntent(Context, ArrayList)} method
 */
public class CategoriesActivity extends AppCompatActivity implements CategoryBottomSheet.Callback, DeleteDialogFragment.Callback {

    private static final String USED_CATEGORIES = "used_categories";

    static final String CHANGE_KEYS = "keys";
    static final String CHANGE_VALUES = "values";

    private CategoriesAdapter adapter;

    private ArrayList<Category> usedCategories;

    private final ArrayList<Category> changeKeys = new ArrayList<>();
    private final ArrayList<Category> changeValues = new ArrayList<>();

    /**
     * Instantiates the class
     *
     * @param usedCategories a list of categories that are in use by items, so that they cannot be
     *                       deleted
     * @return an intent for the activity
     */
    static Intent buildIntent(Context context, ArrayList<Category> usedCategories) {

        Intent intent = new Intent(context, CategoriesActivity.class);
        intent.putParcelableArrayListExtra(USED_CATEGORIES, usedCategories);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (getIntent().getExtras() != null)
            usedCategories = getIntent().getExtras().getParcelableArrayList(USED_CATEGORIES);

        super.onCreate(savedInstanceState);

        Util.lockOrientation(this);

        setContentView(R.layout.activity_categories);

        Toolbar toolbar = findViewById(R.id.categories_toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.inflateMenu(R.menu.categories_actions);
        toolbar.setOnMenuItemClickListener(item -> {

            if (item.getItemId() == R.id.add_category) {
                CategoryBottomSheet sheet = CategoryBottomSheet.getAddInstance();
                sheet.show(getSupportFragmentManager(), CategoryBottomSheet.TAG);

                return true;
            }

            return false;
        });

        RecyclerView recyclerView = findViewById(R.id.categories_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new CategoriesAdapter(this, SettingsFragment.isPremium(this), true);
        adapter.setEmptyView(findViewById(R.id.categories_empty));
        recyclerView.setAdapter(adapter);

        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(CHANGE_KEYS, changeKeys);
        intent.putParcelableArrayListExtra(CHANGE_VALUES, changeValues);
        setResult(RESULT_OK, intent);
    }

    @Override
    public void addCategory(Category category) {

        DatabaseHelper.getInstance(this).addCategory(category);
        adapter.addCategory(category);
    }

    @Override
    public void editCategory(Category oldCategory, Category newCategory) {

        DatabaseHelper helper = DatabaseHelper.getInstance(this);

        boolean editingDefaultCategory = oldCategory.getName().equals(Category.getNoCategoryName(this));

        if(!editingDefaultCategory) {
            helper.deleteCategory(oldCategory);
            helper.addCategory(newCategory);
            adapter.editCategory(oldCategory, newCategory);
        }
        else {
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
            editor.putString(Category.DEFAULT_NAME, newCategory.getName());
            editor.putInt(Category.DEFAULT_COLOR, newCategory.getColor());
            editor.apply();
            adapter.editDefaultCategory();
        }

        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.LIST_CATEGORY, newCategory.getName());
        String where = DatabaseHelper.LIST_CATEGORY + "=?";
        String[] whereArgs = {oldCategory.getName()};
        helper.getWritableDatabase().update(DatabaseHelper.LIST_TABLE_NAME, contentValues, where, whereArgs);
        helper.getWritableDatabase().update(DatabaseHelper.PANTRY_TABLE_NAME, contentValues, where, whereArgs);
        helper.getWritableDatabase().update(DatabaseHelper.SEARCH_TABLE_NAME, contentValues, where, whereArgs);

        if (changeValues.contains(oldCategory)) {
            changeValues.set(changeValues.indexOf(oldCategory), newCategory);
        }
        else {
            changeKeys.add(oldCategory);
            changeValues.add(newCategory);
        }
    }

    @Override
    public void delete(int position) {

        Category category = (Category) adapter.getItem(position);

        if (usedCategories.contains(category) || (changeValues.contains(category) && usedCategories.contains(changeKeys.get(changeValues.indexOf(category))))) {
            Snackbar.make(findViewById(android.R.id.content), R.string.category_used, Snackbar.LENGTH_SHORT).show();
            return;
        }

        DatabaseHelper helper = DatabaseHelper.getInstance(this);
        helper.deleteCategory(category);

        String where = DatabaseHelper.LIST_CATEGORY + "=?";
        String[] whereArgs = {category.getName()};
        helper.getWritableDatabase().delete(DatabaseHelper.SEARCH_TABLE_NAME, where, whereArgs);

        adapter.deleteCategory(position);
    }
}
