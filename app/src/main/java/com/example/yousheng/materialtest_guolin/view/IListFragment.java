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
    //p层返回下一页数据给v层，v层显示出来
    void showNextPage(List<Spot> spots);
    //p层返回了一个消息说：没有下一页了，请停止加载更多并告诉用户
    void showNoNextPage();
}
