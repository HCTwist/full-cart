package com.twisthenry8gmail.fullcart;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import java.math.BigInteger;
import java.util.Locale;

abstract class ListItemActivity extends AppCompatActivity implements CategoryBottomSheet.Callback {

    private int currentAccentColor;

    Toolbar toolbar;
    EditText nameView;
    View errorView;
    Spinner categoryView;
    private TextInputLayout notesContainer;
    private TextInputEditText notesView;
    private TextInputLayout numberContainer;
    private TextInputEditText numberView;

    Button byDateTitle;
    Button byDateView;

    FloatingActionButton finishButton;

    private CategoriesAdapter categoriesAdapter;

    boolean ignoreNextNameChange = false;
    boolean ignoreNextCategoryChange = false;

    private Evaluator evaluator;
    private final ValueAnimator animator = ValueAnimator.ofFloat(0, 1);

    public ListItemActivity() {

        animator.addUpdateListener(animation -> {

            float fraction = (float) animation.getAnimatedValue();
            evaluator.setColors(fraction);
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Util.lockOrientation(this);

        setContentView(R.layout.activity_item);

        toolbar = findViewById(R.id.item_toolbar);
        ((TextView) toolbar.findViewById(R.id.item_toolbar_title)).setText(getToolbarTitleResource());
        toolbar.setNavigationOnClickListener(v -> onNavigationBack());
        currentAccentColor = ContextCompat.getColor(this, R.color.primary);

        nameView = findViewById(R.id.item_name);
        errorView = findViewById(R.id.item_error);
        categoryView = findViewById(R.id.item_category);
        ImageButton addCategoryView = findViewById(R.id.item_add_category);
        notesContainer = findViewById(R.id.item_notes_container);
        notesView = findViewById(R.id.item_notes);
        numberContainer = findViewById(R.id.item_number_container);
        numberView = findViewById(R.id.item_number);

        byDateTitle = findViewById(R.id.item_by_date_title);
        byDateView = findViewById(R.id.item_by_date);

        finishButton = findViewById(R.id.item_finish);

        nameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (ignoreNextNameChange) {
                    ignoreNextNameChange = false;
                }
                else {
                    if (s.toString().trim().isEmpty()) {
                        toggleError(true);
                    }
                    else if (before == 0) {
                        toggleError(false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        categoriesAdapter = new CategoriesAdapter(this, true, false);
        categoryView.setAdapter(categoriesAdapter);
        categoryView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (ignoreNextCategoryChange) {
                    ignoreNextCategoryChange = false;
                }
                else {
                    animateColors(((Category) categoriesAdapter.getItem(position)).getColor());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addCategoryView.setOnClickListener(v -> {

            CategoryBottomSheet dialogFragment = CategoryBottomSheet.getAddInstance();
            dialogFragment.show(getSupportFragmentManager(), CategoryBottomSheet.TAG);
        });

        InputFilter numberFilter = (source, start, end, dest, dstart, dend) -> {

            String candidateString = dest.toString().substring(0, dstart)
                    + source.toString().substring(start, end)
                    + dest.toString().substring(dend, dest.length());

            if(candidateString.isEmpty()) {
                return null;
            }

            try {
                BigInteger candidate = new BigInteger(candidateString);

                if(candidate.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0 || candidate.intValue() == 0) {
                    return "";
                }
                else {
                    return null;
                }
            } catch (NumberFormatException e) {
                return null;
            }
        };
        InputFilter digitsFilter = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                DigitsKeyListener.getInstance(Locale.getDefault(), false, false) :
                DigitsKeyListener.getInstance(false, false);

        numberView.setFilters(new InputFilter[]{numberFilter, digitsFilter});
    }

    @Override
    public void addCategory(Category category) {

        DatabaseHelper.getInstance(this).addCategory(category);
        int position = categoriesAdapter.addCategory(category);
        categoryView.setSelection(position);
    }

    @Override
    public void editCategory(Category oldCategory, Category newCategory) {

    }

    abstract int getToolbarTitleResource();

    String getName() {

        return nameView.getText().toString();
    }

    Category getCategory() {

        return (Category) categoryView.getSelectedItem();
    }

    String getNotes() {

        return notesView.getText() == null ? "" : notesView.getText().toString().trim();
    }

    int getQuantity() {

        try {
            return numberView.getText() == null || numberView.getText().toString().isEmpty() ? 1 : Integer.parseInt(numberView.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 1;
        }
    }

    void onNavigationBack() {

        finish();
    }

    void fillFields(String name, Category category, String notes, int quantity, boolean animate) {

        // Setting this to !name.isEmpty() instead of true ensures that the text change triggers at least once
        ignoreNextNameChange = !name.isEmpty();

        nameView.setText("");
        nameView.append(name);

        setCategory(category, animate);

        notesView.setText("");
        notesView.append(notes);

        numberView.setText("");
        numberView.append(String.valueOf(quantity));
    }

    private void setCategory(Category category, boolean animate) {

        for (int i = 0; i < categoriesAdapter.getCount(); i++) {

            if (categoriesAdapter.getItem(i).equals(category)) {

                if (categoryView.getSelectedItemPosition() == i) {

                    if (animate) {
                        animateColors(category.getColor());
                    }
                    else {
                        setColors(category.getColor());
                    }
                }
                else {

                    if (!animate) {
                        ignoreNextCategoryChange = true;
                        setColors(category.getColor());
                    }
                    categoryView.setSelection(i);
                }
            }
        }
    }

    void setColors(int accent) {

        currentAccentColor = accent;
        notesContainer.setBoxStrokeColor(currentAccentColor);
        notesContainer.setDefaultHintTextColor(ColorStateList.valueOf(currentAccentColor));
        numberContainer.setBoxStrokeColor(currentAccentColor);
        numberContainer.setDefaultHintTextColor(ColorStateList.valueOf(currentAccentColor));
    }

    private void animateColors(int accent) {

        animator.pause();

        evaluator = new Evaluator(accent);

        animator.setCurrentPlayTime(0);
        animator.start();
    }

    void toggleError(boolean error) {

        if (errorView.getVisibility() != (error ? View.VISIBLE : View.INVISIBLE)) {
            AnimationUtil.push(errorView, error, false);
        }
    }

    class Evaluator extends ArgbEvaluator {

        private final int accentFrom;
        private final int accentTo;

        Evaluator(int accent) {

            accentFrom = currentAccentColor;
            accentTo = accent;
        }

        void setColors(float fraction) {

            ListItemActivity.this.setColors((int) evaluate(fraction, accentFrom, accentTo));
        }
    }
}
