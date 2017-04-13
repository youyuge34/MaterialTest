package com.example.yousheng.materialtest_guolin.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.yousheng.materialtest_guolin.fragment.SpotListFragment;
import com.example.yousheng.materialtest_guolin.view.MainActivity;

/**
 * Created by yousheng on 17/4/13.
 */

public class ViewpagerAdapter extends FragmentPagerAdapter {
    public ViewpagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        //创建碎片并且传递position
        return SpotListFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return MainActivity.PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String[] strings={"    街道    ","    地名    ","    景点    "};
        return strings[position];
    }
}
