package com.example.yousheng.materialtest_guolin.http;

import com.example.yousheng.materialtest_guolin.bean.Spot;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yousheng on 17/4/12.
 * 接下来我们把创建Retrofit的过程封装一下，然后希望Activity创建Subscriber对象传进来。
 */

public class HttpMethods {
    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;
    private SpotService movieService;

    //构造方法私有
    private HttpMethods(String BASE_URL) {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        movieService = retrofit.create(SpotService.class);
    }

    //在访问HttpMethods时创建单例
//    private static class SingletonHolder{
//        private static final HttpMethods INSTANCE = new HttpMethods();
//    }

    //获取单例
    public static HttpMethods getInstance(String BASE_URL){
        return new HttpMethods(BASE_URL);
    }

    public void getSpots(Subscriber<List<Spot>> subscriber,int position,int page){
        movieService.getSpot(position,page)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}

