package com.shrimpcolo.johnnytam.idouban.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Johnny Tam on 2016/7/18.
 */
    public class HotMoviesInfo {

    /**
     * count : 20
     * start : 0
     * total : 23
     */

    private int count;
    private int start;
    private int total;
    private String title;
    /**
     * rating : {"max":10,"average":5.3,"stars":"30","min":0}
     * genres : ["喜剧","爱情"]
     * title : 陆垚知马俐
     * casts : [{"alt":"https://movie.douban.com/celebrity/1315866/","avatars":{"small":"https://img3.doubanio.com/img/celebrity/small/33945.jpg","large":"https://img3.doubanio.com/img/celebrity/large/33945.jpg","medium":"https://img3.doubanio.com/img/celebrity/medium/33945.jpg"},"name":"包贝尔","id":"1315866"},{"alt":"https://movie.douban.com/celebrity/1042341/","avatars":{"small":"https://img3.doubanio.com/img/celebrity/small/23241.jpg","large":"https://img3.doubanio.com/img/celebrity/large/23241.jpg","medium":"https://img3.doubanio.com/img/celebrity/medium/23241.jpg"},"name":"宋佳","id":"1042341"},{"alt":"https://movie.douban.com/celebrity/1312699/","avatars":{"small":"https://img1.doubanio.com/img/celebrity/small/1414489207.88.jpg","large":"https://img1.doubanio.com/img/celebrity/large/1414489207.88.jpg","medium":"https://img1.doubanio.com/img/celebrity/medium/1414489207.88.jpg"},"name":"朱亚文","id":"1312699"}]
     * collect_count : 8153
     * original_title : 陆垚知马俐
     * subtype : movie
     * directors : [{"alt":"https://movie.douban.com/celebrity/1011513/","avatars":{"small":"https://img1.doubanio.com/img/celebrity/small/1218.jpg","large":"https://img1.doubanio.com/img/celebrity/large/1218.jpg","medium":"https://img1.doubanio.com/img/celebrity/medium/1218.jpg"},"name":"文章","id":"1011513"}]
     * year : 2016
     * images : {"small":"https://img1.doubanio.com/view/movie_poster_cover/ipst/public/p2361036748.jpg","large":"https://img1.doubanio.com/view/movie_poster_cover/lpst/public/p2361036748.jpg","medium":"https://img1.doubanio.com/view/movie_poster_cover/spst/public/p2361036748.jpg"}
     * alt : https://movie.douban.com/subject/25849006/
     * id : 25849006
     */

    @SerializedName("subjects")
    private List<Movies> movies;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Movies> getMovies() {
        return movies;
    }

    public void setMovies(List<Movies> movies) {
        this.movies = movies;
    }

}
