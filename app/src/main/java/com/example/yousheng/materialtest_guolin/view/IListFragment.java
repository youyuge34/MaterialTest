package com.example.yousheng.materialtest_guolin.view;

import com.example.yousheng.materialtest_guolin.bean.Spot;

import java.util.List;

/**
 * Created by yousheng on 17/4/12.
 * 列表fragment应该实现的方法的接口,供p层调用
 */

public interface IListFragment {
    void showSpots(List<Spot> spots);
    void showProgressBar();
    void hideProgressBar();
    //返回position给p层
    int getFragmentPosition();
}
