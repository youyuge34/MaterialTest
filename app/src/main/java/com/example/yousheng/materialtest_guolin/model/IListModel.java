package com.example.yousheng.materialtest_guolin.model;

import com.example.yousheng.materialtest_guolin.presenter.IListPresenter;

/**
 * Created by yousheng on 17/4/12.
 */

public interface IListModel {
    //p层调用此方法来获取到spot数据
    void getSpotList(IListPresenter listener) throws InterruptedException;
    //p层调用此方法获取下一页数据
    void loadNextPage(IListPresenter listPresenter,int page);
}
