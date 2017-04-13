package com.example.yousheng.materialtest_guolin.presenter;

import com.example.yousheng.materialtest_guolin.bean.Spot;
import com.example.yousheng.materialtest_guolin.model.IListModel;
import com.example.yousheng.materialtest_guolin.model.ListModel;
import com.example.yousheng.materialtest_guolin.view.IListFragment;

import java.util.List;

/**
 * Created by yousheng on 17/4/12.
 */

public class ListPresenter implements IListPresenter {
    //获取v层和m层的接口实例
    private IListFragment listFragment;
    private IListModel listModel;

    public ListPresenter(IListFragment listFragment) {
        this.listFragment = listFragment;
        listModel=new ListModel();
    }

    //让m层去获取数据，并让v层显示进度条，m层获取到后会调用showRecyclerView方法
    @Override
    public void getSpotList() throws InterruptedException {
        listFragment.showProgressBar();
        listModel.getSpotList(this);
    }

    //防止内存泄漏
    @Override
    public void onDestroy() {
        listFragment=null;
    }

    @Override
    public int getFragPosition() {
        return listFragment.getFragmentPosition();
    }

    //m层回调此接口方法，在得到数据后执行，把数据交给v层让它显示出来，并且隐藏进度条
    @Override
    public void showRecyclerView(List<Spot> list) {
        listFragment.showSpots(list);
        listFragment.hideProgressBar();
    }
}
