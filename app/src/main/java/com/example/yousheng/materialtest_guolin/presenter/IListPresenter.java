package com.example.yousheng.materialtest_guolin.presenter;

import com.example.yousheng.materialtest_guolin.bean.Spot;

import java.util.List;

/**
 * Created by yousheng on 17/4/12.
 */

public interface IListPresenter {
    //让p层用此方法与m层打交道，v层初始化时候会调用此方法命令p层去m层取得数据
    void getSpotList() throws InterruptedException;
    //而p获得数据后返回给v层显示
    //实际是m层获得p层的引用，在得到数据后回调p层里的此方法
    void showRecyclerView(List<Spot> list);
    void onDestroy();
    //返回当前fragment在viewpager里的位置信息，从而让m层根据此方法发送不同的网络请求
    int getFragPosition();
}
