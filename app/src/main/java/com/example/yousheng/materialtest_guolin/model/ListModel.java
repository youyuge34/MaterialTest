package com.example.yousheng.materialtest_guolin.model;

import android.os.Handler;
import android.os.Looper;

import com.example.yousheng.materialtest_guolin.R;
import com.example.yousheng.materialtest_guolin.bean.Spot;
import com.example.yousheng.materialtest_guolin.presenter.IListPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yousheng on 17/4/12.
 */

public class ListModel implements IListModel {
    @Override
    public void getSpotList(final IListPresenter listener)  {
        new Handler(Looper.getMainLooper())
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //调用传进来的p层里的方法
                        listener.showRecyclerView(createList());
                    }
                }, 2000);

    }

    private List<Spot> createList() {
        List<Spot> list=new ArrayList<>();
        for(int i=0;i<5;i++) {
            Spot spot1 = new Spot();
            spot1.name = "1";
            spot1.picUrl = R.drawable.chigua_1;
            list.add(spot1);
            Spot spot2 = new Spot();
            spot2.name = "2";
            spot2.picUrl = R.drawable.chigua_2;
            list.add(spot2);
            Spot spot3 = new Spot();
            spot3.name = "3";
            spot3.picUrl = R.drawable.chigua_3;
            list.add(spot3);
        }

        return list;
    }


}
