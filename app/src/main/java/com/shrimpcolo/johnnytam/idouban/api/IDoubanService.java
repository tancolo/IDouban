package com.shrimpcolo.johnnytam.idouban.api;

import com.shrimpcolo.johnnytam.idouban.beans.HotMoviesInfo;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Johnny Tam on 2016/7/10.
 */
public interface IDoubanService {
    String BASE_URL = "https://api.douban.com/v2/";

    @GET("movie/in_theaters")
    Call<HotMoviesInfo> searchHotMovies();
}
