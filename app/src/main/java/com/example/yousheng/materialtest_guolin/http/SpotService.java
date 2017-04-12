package com.example.yousheng.materialtest_guolin.http;

import com.example.yousheng.materialtest_guolin.bean.Spot;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by yousheng on 17/4/12.
 * retrofit2需要
 */

public interface SpotService {
    @GET("spot/{id}.json")
    Observable<List<Spot>> getSpot(@Path("id") int id);
}
