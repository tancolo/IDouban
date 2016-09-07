package com.shrimpcolo.johnnytam.idouban.net;

import com.shrimpcolo.johnnytam.idouban.entity.BlogInfo;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by Johnny Tam on 2016/7/10.
 */
public interface IShrimpColoService {
    String BASE_URL = "http://shrimpcolo.com:3000/";

    @GET("jianshu")
    Observable<BlogInfo> getBlog();
}
