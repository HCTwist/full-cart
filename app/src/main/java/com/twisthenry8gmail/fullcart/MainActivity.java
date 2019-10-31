package com.twisthenry8gmail.fullcart;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.twisthenry8gmail.roundedbottomsheet.BottomMenuFragment;

import java.util.ArrayList;
import java.util.Set;

/**
 * The main activity for the app, most notably controlling a {@link ViewPager} loaded with a
 * {@link ListFragmentShopping} and {@link ListFragmentPantry}
 */

public class MainActivity extends AppCompatActivity implements ByDatePicker.Callback, BottomMenuFragment.MenuClickListener, ShoppingContentViewHolder.TransferInterface {

    @SuppressWarnings("unused")
    public static final String TAG = "FullCart";

    private static final String FIRST_TIME = "first_time";
    private static final String OPEN_COUNT = "open_count";

    private static final String NAV_MENU_TAG = "nav_menu";

    private boolean rtl;

    private Toolbar toolbar;
    private ViewPager pager;
    private TextView listButton;
    private TextView pantryButton;

    //Stored for animation
    private GradientDrawable navHeaderDrawable;
    private Guideline leftGuide;
    private Guideline rightGuide;
    private int navHeaderTextColor;
    private int mNavHeaderCorners;
    private final ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    private int colorText;
    private int screenWidth;
    private int listWidth;
    private int pantryWidth;

    private View pantryTransferDot;
    private final Handler pantryTransferDotHandler = new Handler();
    private boolean showingPantryTransferDot = false;

    //Extra from notification
    public static final String SHOW_PANTRY = "show_pantry";

    //Extra from savedInstanceState
    private static final String PAGE = "page";

    private ListItemShopping transferItem;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        new PremiumBillingHelper(this).resolveInconsistencies();

        Util.lockOrientation(this);
        AppCompatDelegate.setDefaultNightMode(SettingsFragment.getDefaultNightMode(this));

        //Set up notification channels
        AlarmUtil.registerNotificationChannels(this);
        AlarmUtil.scheduleUseByAlarm(this);

        //Check whether it's a users first time, if so run the tutorial
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean firstTime = sharedPreferences.getBoolean(FIRST_TIME, true);
        if (firstTime) {
            sharedPreferences.edit().putBoolean(FIRST_TIME, false).apply();

            Intent intent = new Intent(this, TutorialActivity.class);
            startActivity(intent);
        }

        rtl = Util.isRTL(this);

        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(view -> showNavMenu());
        toolbar.inflateMenu(R.menu.list);

        toolbar.setOnMenuItemClickListener(item -> {

            if (item.getItemId() == R.id.overflow) {
                if (pager.getCurrentItem() == getStartPageIndex()) {
                    getShoppingFragment().showOverflowMenu();
                }
                else {
                    getPantryFragment().showOverflowMenu();
                }
                return true;
            }
            else if (item.getItemId() == R.id.premium_menu_button) {

                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                settingsIntent.putExtra(SettingsActivity.FROM_PREMIUM_POPUP, true);
                startActivityForResult(settingsIntent, RequestCodes.REQUEST_SETTINGS);
            }

            return false;
        });

        //Maintain number of times someone has opened the app
        int nOpens = sharedPreferences.getInt(OPEN_COUNT, 1);
        sharedPreferences.edit().putInt(OPEN_COUNT, nOpens + 1).apply();
        //Check if nOpens is 15, 45, 135 etc.
        for (int i = 15; i <= nOpens; i *= 3) {

            if (nOpens == i) {
                //TODO Improve, animation/dot?
                toolbar.getMenu().findItem(R.id.premium_menu_button).setIcon(R.drawable.outline_lock_highlighted_24);
            }
        }

        //Initialise view variables
        listButton = findViewById(R.id.button_list);
        pantryButton = findViewById(R.id.button_pantry);
        pantryTransferDot = findViewById(R.id.pantry_transfer_dot);
        pager = findViewById(R.id.fragment_pager);

        pager.setAdapter(new PagerAdapter(getSupportFragmentManager(), rtl));
        final ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                animatePageScroll(positionOffset, position);
            }

            @Override
            public void onPageSelected(int position) {

                if (position == getEndPageIndex()) {
                    hidePantryDot();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        pager.addOnPageChangeListener(pageChangeListener);

        //Register navigation button presses
        listButton.setOnClickListener(v -> pager.setCurrentItem(0));
        pantryButton.setOnClickListener(v -> pager.setCurrentItem(1));

        ExtendedFloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> ((FABClick) getCurrentPagerFragment()).onFABClick());
        fab.setOnLongClickListener(v -> ((FABClick) getCurrentPagerFragment()).onFABLongClick());

        //Set correct page
        int pantryPage = getEndPageIndex();
        int shoppingPage = getStartPageIndex();

        final int page;
        if (getIntent().getExtras() != null && getIntent().getExtras().getBoolean(SHOW_PANTRY, false)) {
            page = pantryPage;
        }
        else if (savedInstanceState != null) {
            page = savedInstanceState.getInt(PAGE);
        }
        else {
            page = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getBoolean(SettingsFragment.KEY_SHOW_PANTRY, false) ? pantryPage : shoppingPage;
        }
        pager.setCurrentItem(page, false);

        //Store for animation
        View navHeaderBackground = findViewById(R.id.navigation_background);
        leftGuide = findViewById(R.id.nav_header_guide_left);
        rightGuide = findViewById(R.id.nav_header_guide_right);
        navHeaderDrawable = (GradientDrawable) navHeaderBackground.getBackground().mutate();
        mNavHeaderCorners = getResources().getDimensionPixelSize(R.dimen.nav_header_corner_radii);
        navHeaderTextColor = ContextCompat.getColor(MainActivity.this, R.color.primary);
        colorText = ContextCompat.getColor(MainActivity.this, R.color.body);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;

        final View content = findViewById(android.R.id.content);
        content.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                ((AdapterLoader) getPageFragment(page)).loadItems(() ->
                        ((AdapterLoader) getPageFragment(page == 0 ? 1 : 0)).loadItems(null));

                listWidth = listButton.getWidth();
                pantryWidth = pantryButton.getWidth();

                pageChangeListener.onPageSelected(page);
                pageChangeListener.onPageScrolled(page, 0, 0);

                content.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    protected void onResume() {

        super.onResume();
        toolbar.getMenu().findItem(R.id.premium_menu_button).setVisible(!SettingsFragment.isPremium(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == RequestCodes.REQUEST_SETTINGS) {

                ArrayList<String> keysChanged = data.getStringArrayListExtra(SettingsFragment.SETTINGS_DATA);
                onSettingChanged(keysChanged);
            }
        }

        //Propagate to fragments
        getShoppingFragment().onActivityResult(requestCode, resultCode, data);
        getPantryFragment().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void startTransfer(ListItemShopping item) {

        transferItem = item;
    }

    @Override
    public void completeTransfer(String date) {

        ListItemPantry pantryItem = new ListItemPantry(transferItem.getName(),
                transferItem.getCategory(),
                transferItem.getNotes(),
                transferItem.getQuantity(),
                DateUtil.formatCurrentDate(),
                SettingsFragment.getDefaultByDateType(this),
                date);

        final ListFragmentShopping listFragment = getShoppingFragment();
        listFragment.transfer(getPantryFragment(), transferItem, pantryItem);

        showPantryDot();

        Snackbar snackbar = AnchoredSnackbar.make(listFragment.getView(), R.string.shopping_transfer_prompt_message, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.undo, v -> {

            listFragment.undoTransfer(getPantryFragment(), transferItem, pantryItem);
            hidePantryDot();
        });
        snackbar.show();
    }

    @Override
    public void onDateSet(String date) {

        completeTransfer(date);
    }

    @Override
    protected void onNewIntent(Intent intent) {

        if (intent.getBooleanExtra(SHOW_PANTRY, false)) {
            pager.setCurrentItem(getEndPageIndex(), true);
        }
        super.onNewIntent(intent);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        outState.putInt(PAGE, pager.getCurrentItem());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void menuItemSelected(String tag, int position) {

        switch (tag) {
            case NAV_MENU_TAG:

                switch (position) {
                    case 0:

                        openCategoryActivity();
                        break;
                    case 1:
                        Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivityForResult(settingsIntent, RequestCodes.REQUEST_SETTINGS);
                        break;
                }
                break;
            case ListFragmentShopping.OVERFLOW_MENU_TAG:

                getShoppingFragment().menuClick(position);
                break;
            case ListFragmentPantry.OVERFLOW_MENU_TAG:

                getPantryFragment().menuClick(position);
                break;
        }
    }

    /**
     * React to keys being changed in settings
     *
     * @param keysChanged the changed keys
     */
    private void onSettingChanged(ArrayList<String> keysChanged) {

        if (keysChanged.contains(SettingsFragment.KEY_LOCK_ROTATION)) {

            Util.lockOrientation(this);
        }
    }

    /**
     * Show an indicator that a new item has been added to the pantry whilst on the shopping page
     */
    void showPantryDot() {

        if (!showingPantryTransferDot) {
            pantryTransferDot.animate().scaleX(1).scaleY(1).setInterpolator(new OvershootInterpolator());
        }
        else {
            pantryTransferDotHandler.removeCallbacksAndMessages(null);
        }

        showingPantryTransferDot = true;
        pantryTransferDotHandler.postDelayed(this::hidePantryDot, getResources().getInteger(R.integer.transfer_pantry_exit_delay));
    }

    /**
     * Hide the pantry dot
     *
     * @see #showPantryDot()
     */
    void hidePantryDot() {

        showingPantryTransferDot = false;
        pantryTransferDot.animate().scaleX(0).scaleY(0).setInterpolator(new AnticipateInterpolator());
    }

    /**
     * Get a fragment from the {@link ViewPager}
     *
     * @param position the position of the fragment, either 0 or 1
     * @return the fragment on the page determined by the position parameter
     */
    private ListFragment getPageFragment(int position) {

        return (ListFragment) getSupportFragmentManager().findFragmentByTag(PagerAdapter.getFragmentTag(position));
    }

    /**
     * Convenience method for {@link #getPageFragment(int)} that returns the instance of
     * {@link ListFragmentShopping} in the {@link ViewPager}
     *
     * @return the shopping fragment
     */
    ListFragmentShopping getShoppingFragment() {

        return (ListFragmentShopping) getPageFragment(getStartPageIndex());
    }

    /**
     * @see #getShoppingFragment()
     */
    ListFragmentPantry getPantryFragment() {

        return (ListFragmentPantry) getPageFragment(getEndPageIndex());
    }

    /**
     * Convenience method for {@link #getPageFragment(int)}
     *
     * @return the fragment on the page currently being shown
     */
    private Fragment getCurrentPagerFragment() {

        return getPageFragment(pager.getCurrentItem());
    }

    /**
     * Show the navigation menu when the hamburger icon is clicked
     */
    private void showNavMenu() {

        int[] icons = {
                R.drawable.outline_list_alt_24,
                R.drawable.outline_settings_24
        };

        int[] texts = {
                R.string.title_categories,
                R.string.title_settings
        };

        BottomMenuFragment menuFragment = BottomMenuFragment.getInstance(icons, texts, R.color.body, R.color.accent);
        menuFragment.show(getSupportFragmentManager(), NAV_MENU_TAG);
    }

    /**
     * Open a {@link CategoriesActivity}
     */
    private void openCategoryActivity() {

        getPageFragment(getEndPageIndex()).getAdapter().post(() -> {
            Set<Category> usedCategories = getShoppingFragment().getUsedCategories();
            usedCategories.addAll(getPantryFragment().getUsedCategories());

            Intent categoriesIntent = CategoriesActivity.buildIntent(MainActivity.this, new ArrayList<>(usedCategories));
            startActivityForResult(categoriesIntent, RequestCodes.REQUEST_CATEGORIES);
        });
    }

    /**
     * Animate the page indicator
     *
     * @param o        the current offset
     * @param position the page position
     * @see ViewPager.OnPageChangeListener#onPageScrolled(int, float, int)
     */
    private void animatePageScroll(float o, int position) {

        if (rtl) {
            o = position == 1 ? 0 : 1 - o;
        }
        else {
            o = position == 1 ? 1 : o;
        }

        ConstraintLayout.LayoutParams leftParams = (ConstraintLayout.LayoutParams) leftGuide.getLayoutParams();
        ConstraintLayout.LayoutParams rightParams = (ConstraintLayout.LayoutParams) rightGuide.getLayoutParams();

        float r1, r2;
        int cList, cPantry;

        float cornerToWidthRatio = (float) mNavHeaderCorners / (float) screenWidth;
        float part1 = 0.5F - cornerToWidthRatio / 2;
        float part2 = 0.5F + cornerToWidthRatio / 2;

        if (o < part1) {

            o /= part1;

            leftParams.guideBegin = 0;
            rightParams.guideBegin = (int) (listWidth + o * (screenWidth - listWidth));

            r1 = rtl ? mNavHeaderCorners : 0;
            r2 = rtl ? 0 : mNavHeaderCorners;

            cList = navHeaderTextColor;
            cPantry = (int) argbEvaluator.evaluate(o, colorText, navHeaderTextColor);
        }
        else if (o < part2) {

            o -= part1;
            o /= (part2 - part1);

            leftParams.guideBegin = 0;
            rightParams.guideBegin = screenWidth;

            r1 = mNavHeaderCorners * (rtl ? 1 - o : o);
            r2 = mNavHeaderCorners * (rtl ? o : 1 - o);

            cList = navHeaderTextColor;
            cPantry = navHeaderTextColor;
        }
        else {

            o -= part2;
            o /= (1 - part2);

            leftParams.guideBegin = (int) (o * (screenWidth - pantryWidth));
            rightParams.guideBegin = screenWidth;

            r1 = rtl ? 0 : mNavHeaderCorners;
            r2 = rtl ? mNavHeaderCorners : 0;

            cList = (int) argbEvaluator.evaluate(o, navHeaderTextColor, colorText);
            cPantry = navHeaderTextColor;
        }

        leftGuide.setLayoutParams(leftParams);
        rightGuide.setLayoutParams(rightParams);

        navHeaderDrawable.setCornerRadii(new float[]{r1, r1, r2, r2, r2, r2, r1, r1});

        listButton.setTextColor(cList);
        pantryButton.setTextColor(cPantry);
    }

    int getStartPageIndex() {

        return rtl ? 1 : 0;
    }

    int getEndPageIndex() {

        return rtl ? 0 : 1;
    }

    interface FABClick {

        void onFABClick();

        boolean onFABLongClick();
    }

    interface AdapterLoader {

        void loadItems(Runnable callback);
    }
}