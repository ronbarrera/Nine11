package com.ronaldbarrera.nine11.ui.center;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ronaldbarrera.nine11.R;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = SectionsPagerAdapter.class.getSimpleName();

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_all_centers, R.string.tab_saved_centers};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new AllCenterFragment();
            case 1:
                return new SavedCenterFragment();
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }
}