package com.example.yousheng.materialtest_guolin.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.yousheng.materialtest_guolin.fragment.SquareListFragment;

/**
 * Created by yousheng on 17/5/3.
 */

public class SquarePagerAdapter extends FragmentPagerAdapter {
    public SquarePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return SquareListFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String[] strings={"广场","我的二维码"};
        return strings[position];
    }
}
