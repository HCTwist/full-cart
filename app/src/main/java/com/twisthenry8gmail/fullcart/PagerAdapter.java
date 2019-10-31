package com.twisthenry8gmail.fullcart;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

class PagerAdapter extends FragmentPagerAdapter {

    private final boolean rtl;

    PagerAdapter(FragmentManager fm, boolean rtl) {

        super(fm);
        this.rtl = rtl;
    }

    @Override
    public Fragment getItem(int position) {

        position = rtl ? 1 - position : position;
        switch (position) {
            case 0:
                return new ListFragmentShopping();
            case 1:
                return new ListFragmentPantry();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {

        return 2;
    }

    static String getFragmentTag(int position) {

        return "android:switcher:" + R.id.fragment_pager + ":" + position;
    }
}
