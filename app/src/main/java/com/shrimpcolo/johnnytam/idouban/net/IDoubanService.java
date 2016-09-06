package com.shrimpcolo.johnnytam.idouban.net;

import com.shrimpcolo.johnnytam.idouban.entity.BookInfo;
import com.shrimpcolo.johnnytam.idouban.entity.HotMoviesInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Johnny Tam on 2016/7/10.
 */
public interface IDoubanService {
    String BASE_URL = "https://api.douban.com/v2/";

    @GET("movie/in_theaters")
    Observable<HotMoviesInfo> searchHotMovies();

    @GET("book/search")
    Observable<BookInfo> searchBooks(@Query("q") String name);
}
