package com.twisthenry8gmail.fullcart;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import androidx.core.content.ContextCompat;

/**
 * SQLite helper class that adds implementation for adding/deleting/editing {@link ListItemShopping},
 * {@link ListItemPantry} and {@link Category} classes
 */
class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "full_cart_db";
    private static final int DATABASE_VERSION = 1;

    private static final String TEXT = "TEXT";
    private static final String INTEGER = "INTEGER";
    private static final String REAL = "REAL";

    private static final String CATEGORIES_TABLE_NAME = "categories_table";
    static final String CATEGORIES_NAME = "categories_name";
    static final String CATEGORIES_COLOR = "categories_color";

    private static final String CREATE_CATEGORIES = "CREATE TABLE " + CATEGORIES_TABLE_NAME
            + "("
            + CATEGORIES_NAME + " " + TEXT + ","
            + CATEGORIES_COLOR + " " + INTEGER + ")";

    static final String LIST_TABLE_NAME = "list_table";
    static final String LIST_NAME = "list_name";
    static final String LIST_CATEGORY = "list_category";
    static final String LIST_NOTES = "list_notes";
    static final String LIST_QUANTITY = "list_quantity";
    static final String LIST_DATE_ADDED = "list_date_added";
    static final String LIST_CHECKED = "list_checked";

    private static final String CREATE_LIST = "CREATE TABLE " + LIST_TABLE_NAME
            + "("
            + LIST_NAME + " " + TEXT + ","
            + LIST_CATEGORY + " " + TEXT + ","
            + LIST_NOTES + " " + TEXT + ","
            + LIST_QUANTITY + " " + INTEGER + ","
            + LIST_DATE_ADDED + " " + TEXT + ","
            + LIST_CHECKED + " " + INTEGER + ")";

    static final String PANTRY_TABLE_NAME = "pantry_table";
    static final String PANTRY_BY_DATE = "pantry_by_date";

    private static final String CREATE_PANTRY = "CREATE TABLE " + PANTRY_TABLE_NAME
            + "("
            + LIST_NAME + " " + TEXT + ","
            + LIST_CATEGORY + " " + TEXT + ","
            + LIST_NOTES + " " + TEXT + ","
            + LIST_QUANTITY + " " + INTEGER + ","
            + LIST_DATE_ADDED + " " + TEXT + ","
            + PANTRY_BY_DATE + " " + TEXT + ")";

    static final String SEARCH_TABLE_NAME = "search";

    static final String SEARCH_FREQUENCY = "search_score";
    static final String SEARCH_LAST_ADDED = "search_last_added";

    private static final String CREATE_SEARCH = "CREATE TABLE " + SEARCH_TABLE_NAME
            + "("
            + LIST_NAME + " " + TEXT + ","
            + LIST_CATEGORY + " " + TEXT + ","
            + SEARCH_FREQUENCY + " " + REAL + ","
            + SEARCH_LAST_ADDED + " " + TEXT + ")";

    static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    static final String SIMPLE_DATE_FORMAT = "yyyy-MM-dd";

    private DatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static DatabaseHelper instance;

    /**
     * A getter to enforce a singleton pattern
     *
     * @return the sole instance of the {@link DatabaseHelper}
     */
    static DatabaseHelper getInstance(Context context) {

        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(CREATE_CATEGORIES);
        sqLiteDatabase.execSQL(CREATE_LIST);
        sqLiteDatabase.execSQL(CREATE_PANTRY);
        sqLiteDatabase.execSQL(CREATE_SEARCH);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    /**
     * Add a category to the database
     *
     * @param category the category to add
     */
    void addCategory(Category category) {

        getWritableDatabase().insert(CATEGORIES_TABLE_NAME, null, category.toContentValues());
    }

    /**
     * Delete a category from the database
     *
     * @param category the category to delete
     */
    void deleteCategory(Category category) {

        getWritableDatabase().delete(CATEGORIES_TABLE_NAME, CATEGORIES_NAME + "=?", new String[]{category.getName()});
    }

    /**
     * Fetch the entire list of categories from the database
     *
     * @return the full list of categories
     */
    ArrayList<Category> getCategories() {

        Cursor cursor = getReadableDatabase().query(CATEGORIES_TABLE_NAME, null, null, null, null, null, CATEGORIES_NAME + " ASC");
        ArrayList<Category> categories = new ArrayList<>();

        while (cursor.moveToNext()) {

            categories.add(new Category(cursor.getString(cursor.getColumnIndex(CATEGORIES_NAME)), cursor.getInt(cursor.getColumnIndex(CATEGORIES_COLOR))));
        }

        cursor.close();
        return categories;
    }

    /**
     * Utility method to get a category with a given name and list of categories
     *
     * @param name       the category name
     * @param categories a list of categories to search
     * @return a category with a matching name
     */
    static Category getCategory(Context context, String name, ArrayList<Category> categories) {

        if (name.equals(Category.getNoCategoryName(context))) {
            return Category.getNoCategory(context);
        }
        else {

            for (Category category : categories) {

                if (category.getName().equals(name)) {
                    return category;
                }
            }

            return new Category(name, ContextCompat.getColor(context, R.color.default_category));
        }
    }

    /**
     * Utility for matching category with it's name
     *
     * @param name the category name
     * @return a category with a matching name
     */
    static Category getCategory(Context context, String name) {

        DatabaseHelper helper = DatabaseHelper.getInstance(context);
        ArrayList<Category> categories = helper.getCategories();

        return DatabaseHelper.getCategory(context, name, categories);
    }

    /**
     * Utility for checking whether a category name is already being used, including the default category
     *
     * @param categories the full list of categories to check against
     * @param name       the name to check. The name is checked exactly so it should be pre-trimmed etc.
     * @return true if the category is in use
     */
    static boolean hasCategory(Context context, ArrayList<Category> categories, String name) {

        String nUpper = name.toUpperCase();
        if (nUpper.equalsIgnoreCase(Category.getNoCategoryName(context).toUpperCase())) {
            return true;
        }

        for (Category category : categories) {

            if (category.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Add a list item to the database
     *
     * @param item the item to add
     */
    void addShoppingItem(ListItemShopping item) {

        getWritableDatabase().insert(LIST_TABLE_NAME, null, item.toContentValues());
    }

    /**
     * Delete a list item from the database
     *
     * @param item the item to delete
     */
    void deleteShoppingItem(ListItemShopping item) {

        getWritableDatabase().delete(LIST_TABLE_NAME, item.getWhereClause(), item.getWhereArgs());
    }

    /**
     * Add a transferButton item to the database
     *
     * @param item the item to add
     */
    void addPantryItem(ListItemPantry item) {

        getWritableDatabase().insert(PANTRY_TABLE_NAME, null, item.toContentValues());
    }

    /**
     * Delete a transferButton item from the database
     *
     * @param item the item to delete
     */
    void deletePantryItem(ListItemPantry item) {

        getWritableDatabase().delete(PANTRY_TABLE_NAME, item.getWhereClause(), item.getWhereArgs());
    }

    /**
     * Add an item to the search database
     *
     * @param name     the name of the oldItem
     * @param category the category of the oldItem
     */
    void addSearchItem(String name, String category) {

        String[] columns = {LIST_NAME, LIST_CATEGORY, SEARCH_FREQUENCY};
        String selection = LIST_NAME + " = ? AND " + LIST_CATEGORY + " = ?";
        String[] selectionArgs = {name, category};

        Cursor cursor = getReadableDatabase().query(SEARCH_TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        ContentValues contentValues = new ContentValues();
        contentValues.put(LIST_NAME, name);
        contentValues.put(LIST_CATEGORY, category);
        contentValues.put(SEARCH_LAST_ADDED, DateUtil.formatCurrentDate());

        if (cursor.getCount() == 0) {
            contentValues.put(SEARCH_FREQUENCY, 1);
            getWritableDatabase().insert(SEARCH_TABLE_NAME, null, contentValues);
        }
        else {

            cursor.moveToFirst();
            int f = cursor.getInt(cursor.getColumnIndex(SEARCH_FREQUENCY)) + 1;
            contentValues.put(SEARCH_FREQUENCY, f);
            getWritableDatabase().update(SEARCH_TABLE_NAME, contentValues, selection, selectionArgs);
        }
        cursor.close();
    }
}