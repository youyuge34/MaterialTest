package com.example.yousheng.materialtest_guolin.model;

import android.util.Log;

import com.example.yousheng.materialtest_guolin.bean.Spot;
import com.example.yousheng.materialtest_guolin.http.HttpMethods;
import com.example.yousheng.materialtest_guolin.presenter.IListPresenter;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by yousheng on 17/4/12.
 */

public class ListModel implements IListModel {
    Subscriber<List<Spot>> subscriber;
    List<Spot> list = new ArrayList<>();

    @Override
    public void getSpotList(final IListPresenter listener) {
//        new Handler(Looper.getMainLooper())
//                .postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        //调用传进来的p层里的方法
//                        listener.showRecyclerView(createList());
//                    }
//                }, 2000);
        subscriber = new Subscriber<List<Spot>>() {
            @Override
            public void onCompleted() {
                Log.d("test1", "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(List<Spot> spots) {
                listener.showRecyclerView(spots);
            }
        };
        HttpMethods.getInstance().getSpots(subscriber, 1);
//        listener.showRecyclerView(createList());
    }

//    private List<Spot> createList() {
//        String baseUrl="http://onxlr7bsm.bkt.clouddn.com/api/";
//
//        Retrofit retrofit=new Retrofit.Builder()
//                .baseUrl(baseUrl)
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        SpotService spotService=retrofit.create(SpotService.class);
//
//        spotService.getSpot(1)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<List<Spot>>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(List<Spot> spots) {
//
//                    }
//                });

//        subscriber=new Subscriber<List<Spot>>() {
//            @Override
//            public void onCompleted() {
//                Log.d("test1", "onCompleted: ");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onNext(List<Spot> spots) {
//                list=spots;
//            }
//        };
//        HttpMethods.getInstance().getSpots(subscriber,1);
//        Log.d("test1", "createList: "+list.get(0).name);
//        return list;



}
