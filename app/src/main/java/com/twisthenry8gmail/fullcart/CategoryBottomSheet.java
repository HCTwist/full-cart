package com.twisthenry8gmail.fullcart;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.twisthenry8gmail.roundedbottomsheet.BottomRoundedFragment;

import java.util.ArrayList;

/**
 * The bottom sheet displayed when a category is either added or edited. This should not be
 * instantiated manually and the activity the bottom sheet is shown from should implement {@link Callback}
 */
public class CategoryBottomSheet extends BottomRoundedFragment implements ColorPickerDialogFragment.ColorPickerSelectListener {

    static final String TAG = "category_sheet";

    private static final String EDIT = "edit";
    private static final String CATEGORY = "category";

    private EditText nameView;
    private TextView errorView;
    private ColorDotGridLayout colorGrid;
    private View confirmButton;

    private ArrayList<Category> existingCategories;
    private String nameException = "";

    /**
     * Instantiates the class to edit a given category
     *
     * @param category the category to edit
     * @return an instance of {@link CategoryBottomSheet}
     */
    static CategoryBottomSheet getEditInstance(Category category) {

        CategoryBottomSheet categoryDialogFragment = new CategoryBottomSheet();

        Bundle bundle = new Bundle();
        bundle.putBoolean(EDIT, true);
        bundle.putParcelable(CATEGORY, category);

        categoryDialogFragment.setArguments(bundle);

        return categoryDialogFragment;
    }

    /**
     * Instantiates the class to add a new category
     *
     * @return an instance of {@link CategoryBottomSheet}
     */
    static CategoryBottomSheet getAddInstance() {

        CategoryBottomSheet categoryDialogFragment = new CategoryBottomSheet();

        Bundle bundle = new Bundle();
        bundle.putBoolean(EDIT, false);

        categoryDialogFragment.setArguments(bundle);

        return categoryDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CategoryBottomSheet);
    }

    @Override
    protected View getContent(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layout = LayoutInflater.from(getContext()).inflate(R.layout.fragment_category_sheet, container, false);

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getContext());
        existingCategories = databaseHelper.getCategories();

        nameView = layout.findViewById(R.id.category_sheet_name);
        errorView = layout.findViewById(R.id.category_sheet_error);
        colorGrid = layout.findViewById(R.id.category_sheet_color_grid);
        confirmButton = layout.findViewById(R.id.category_sheet_confirm);

        //Handle whether input is invalid
        nameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                updateValidity(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        TypedArray typedArray = getResources().obtainTypedArray(R.array.category_colors);
        int[] colors = new int[typedArray.length()];

        if (getContext() != null) {
            int defaultColor = ContextCompat.getColor(getContext(), R.color.default_category);
            for (int i = 0; i < colors.length; i++) {
                colors[i] = typedArray.getColor(i, defaultColor);
            }
        }
        typedArray.recycle();

        boolean allowCustomColors = SettingsFragment.isPremium(getContext());
        colorGrid.setColors(colors, allowCustomColors);
        if (allowCustomColors) {

            colorGrid.setCustomDotClickListener(() -> {
                ColorPickerDialogFragment df = ColorPickerDialogFragment.getInstance(colorGrid.getCustomColor(), ContextCompat.getColor(getContext(), R.color.body), this);
                if (getFragmentManager() != null) {
                    df.show(getFragmentManager(), ColorPickerDialogFragment.TAG);
                }
            });
        }

        final boolean editRequest = getArguments() != null && getArguments().getBoolean(EDIT);

        if (editRequest) {

            Category category = getArguments().getParcelable(CATEGORY);
            if (category != null) {
                nameException = category.getName();
                nameView.append(category.getName());

                boolean foundColor = false;
                for (int i = 0; i < colors.length; i++) {

                    if (category.getColor() == colors[i]) {
                        colorGrid.select(i, false);
                        foundColor = true;
                        break;
                    }
                }
                if (!foundColor) {
                    colorGrid.selectCustomColor(category.getColor(), false);
                }

                ((TextView) layout.findViewById(R.id.category_sheet_editing)).setText(getString(R.string.category_editing, category.getName()));
            }
        }
        else {

            colorGrid.select(0, false);
            confirmButton.setEnabled(false);
        }

        confirmButton.setOnClickListener(v -> {
            if (getActivity() != null && nameView.getText() != null) {

                Category newCategory = new Category(nameView.getText().toString().trim(), colorGrid.getSelectedColor());
                if (editRequest) {
                    Category oldCategory = getArguments().getParcelable(CATEGORY);
                    if (oldCategory != null && !oldCategory.equals(newCategory)) {
                        ((Callback) getActivity()).editCategory(oldCategory, newCategory);
                    }
                }
                else {
                    ((Callback) getActivity()).addCategory(newCategory);
                }

                dismiss();
            }
        });

        return layout;
    }

    @Override
    public void onConfirmColor(int color) {

        colorGrid.selectCustomColor(color, true);
    }

    /**
     * Check whether the input is valid and update errors
     *
     * @param s the string to check
     */
    private void updateValidity(String s) {

        if (s.isEmpty()) {
            errorView.setText(R.string.field_blank);
            AnimationUtil.push(errorView, true);
            confirmButton.setEnabled(false);
        }
        //Name already exists
        else if ((DatabaseHelper.hasCategory(getContext(), existingCategories, s) && !s.equals(nameException))) {
            errorView.setText(R.string.add_category_name_error);
            AnimationUtil.push(errorView, true);
            confirmButton.setEnabled(false);
        }
        else {
            AnimationUtil.push(errorView, false, false);
            confirmButton.setEnabled(true);
        }
    }

    interface Callback {

        void addCategory(Category category);

        void editCategory(Category oldCategory, Category newCategory);
    }
}
