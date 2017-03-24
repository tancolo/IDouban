package com.shrimpcolo.johnnytam.idouban.mobileapi;

import com.shrimpcolo.johnnytam.idouban.entity.BooksInfo;
import com.shrimpcolo.johnnytam.idouban.entity.HotMoviesInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Johnny Tam on 2016/7/10.
 */
public interface IDoubanService {
    String BASE_URL = "https://api.douban.com/v2/";

    @GET("movie/in_theaters")
    Call<HotMoviesInfo> searchHotMovies(@Query("start") int start);

    @GET("book/search")
    Call<BooksInfo> searchBooks(@Query("q") String name, @Query("start") int start);
}