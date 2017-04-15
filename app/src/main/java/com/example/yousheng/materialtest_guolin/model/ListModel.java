package com.example.yousheng.materialtest_guolin.model;

import android.util.Log;

import com.example.yousheng.materialtest_guolin.bean.Spot;
import com.example.yousheng.materialtest_guolin.http.HttpMethods;
import com.example.yousheng.materialtest_guolin.presenter.IListPresenter;

import java.util.List;

import rx.Subscriber;

/**
 * Created by yousheng on 17/4/12.
 */

public class ListModel implements IListModel {
    public static final String BASE_URL = "http://onxlr7bsm.bkt.clouddn.com/";
    Subscriber<List<Spot>> subscriber;

    @Override
    public void getSpotList(final IListPresenter presenter) {
        subscriber = new Subscriber<List<Spot>>() {
            @Override
            public void onCompleted() {
                Log.d("test1", "onCompleted: " + presenter.getFragPosition());
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(List<Spot> spots) {
                presenter.showRecyclerAndBanner(spots);
            }
        };
        HttpMethods.getInstance(BASE_URL).getSpots(subscriber, presenter.getFragPosition(), 1);
    }

    @Override
    public void loadNextPage(final IListPresenter presenter,int page) {
        subscriber = new Subscriber<List<Spot>>() {
            @Override
            public void onCompleted() {
                Log.d("test1", "onCompleted: loadNextPage" );
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                presenter.showNoNextPage();
            }

            @Override
            public void onNext(List<Spot> spots) {
                presenter.showNextPage(spots);
            }
        };
        HttpMethods.getInstance(BASE_URL).getSpots(subscriber, presenter.getFragPosition(), page);
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
