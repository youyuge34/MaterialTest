package com.example.yousheng.materialtest_guolin.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.yousheng.materialtest_guolin.fragment.SpotListFragment;

public class MainActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return SpotListFragment.newInstance();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        //设置下拉刷新

//        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refreshFruits();
//            }
//        });
    }

//    private void refreshFruits() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
////                        init();
////                        adapter.notifyDataSetChanged();
//                        //隐藏刷新图标
////                        refreshLayout.setRefreshing(false);
//                    }
//                });
//            }
//        }).start();
//    }

}
