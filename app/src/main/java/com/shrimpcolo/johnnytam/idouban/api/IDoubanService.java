package com.shrimpcolo.johnnytam.idouban.api;

import com.shrimpcolo.johnnytam.idouban.book.BookInfo;
import com.shrimpcolo.johnnytam.idouban.movie.HotMoviesInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Johnny Tam on 2016/7/10.
 */
public interface IDoubanService {
    String BASE_URL = "https://api.douban.com/v2/";

    @GET("movie/in_theaters")
    Call<HotMoviesInfo> searchHotMovies();

    @GET("book/search")
    Call<BookInfo> searchBooks(@Query("q") String name);
}
