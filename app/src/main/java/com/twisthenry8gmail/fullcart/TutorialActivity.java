package com.twisthenry8gmail.fullcart;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class TutorialActivity extends AppCompatActivity {

    private static final int PAGES = 6;
    private static float BASE_TRANSLATION;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        BASE_TRANSLATION = Util.dpToPx(this, 72);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tutorial);

        ViewPager viewPager = findViewById(R.id.tutorial_view_pager);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {

                switch (position) {

                    case 0:
                        return TutorialFragment.getFirstInstance();
                    case 1:
                        return TutorialFragment.getMiddleInstance(R.drawable.tutorial_categories, R.string.tutorial_1_title, R.string.tutorial_1_body);
                    case 2:
                        return TutorialFragment.getMiddleInstance(R.drawable.tutorial_shopping_list, R.string.tutorial_2_title, R.string.tutorial_2_body);
                    case 3:
                        return TutorialFragment.getMiddleInstance(R.drawable.tutorial_pantry, R.string.tutorial_3_title, R.string.tutorial_3_body);
                    case 4:
                        return TutorialFragment.getMiddleInstance(R.drawable.tutorial_notifications, R.string.tutorial_4_title, R.string.tutorial_4_body);
                    case 5:
                        return TutorialFragment.getLastInstance();
                }

                return new Fragment();
            }

            @Override
            public int getCount() {

                return PAGES;
            }
        });

        viewPager.setPageTransformer(true, (page, position) -> {

            View image = page.findViewById(R.id.tutorial_page_image);
            View title = page.findViewById(R.id.tutorial_page_title);
            View body = page.findViewById(R.id.tutorial_page_body);

            if (position < -1) {
                if (image != null) image.setAlpha(0F);
            }
            else if (position <= 1) {
                if (image != null) {
                    image.setAlpha(1 - Math.abs(position));
                    image.setTranslationX(3 * BASE_TRANSLATION * position);
                }
                if (title != null) title.setTranslationX(BASE_TRANSLATION * position);
                if (body != null) body.setTranslationX(2 * BASE_TRANSLATION * position);
            }
            else {
                if (image != null) image.setAlpha(0F);
            }
        });
    }
}
