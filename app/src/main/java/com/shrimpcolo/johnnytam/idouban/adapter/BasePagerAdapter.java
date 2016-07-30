package com.shrimpcolo.johnnytam.idouban.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johnny Tam on 2016/7/30.
 */
public abstract class BasePagerAdapter extends FragmentPagerAdapter {
    protected final List<Fragment> mFragments = new ArrayList<>();
    protected final List<String> mFrgmentTitles = new ArrayList<>();

    public BasePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public abstract void addFragment(Fragment fragment, String title);

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFrgmentTitles.get(position);
    }
}
