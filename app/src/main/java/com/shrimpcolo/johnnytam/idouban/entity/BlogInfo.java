package com.shrimpcolo.johnnytam.idouban.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Johnny Tam on 2016/9/7.
 */

public class BlogInfo {

    /**
     * count : 9
     * total : 9
     * start : 0
     * blog : [{"id":0,"name":"RecyclerView的重构之路(一)","url":"http://www.jianshu.com/p/99cd83778373"},{"id":1,"name":"RecyclerView的重构之路(二)","url":"http://www.jianshu.com/p/81270f444a6f"},{"id":2,"name":"RecyclerView的重构之路(三)","url":"http://www.jianshu.com/p/a5d60fa90d17"},{"id":3,"name":"RecyclerView的重构之路(四)","url":"http://www.jianshu.com/p/5eac09f5bb92"},{"id":4,"name":"RecyclerView的重构之路(五)","url":"http://www.jianshu.com/p/c4a8bfbe1f40"},{"id":5,"name":"RecyclerView的重构之路(六)","url":"http://www.jianshu.com/p/b1ea462f5602"},{"id":6,"name":"RecyclerView的重构之路(七)","url":"http://www.jianshu.com/p/9665955103c0"},{"id":7,"name":"RecyclerView的重构之路(八)","url":"http://www.jianshu.com/p/98399b00ae78"},{"id":8,"name":"又一个寡头时代的来临","url":"http://www.jianshu.com/p/e9f4736fa08a"}]
     */

    private int count;
    private int total;
    private int start;
    /**
     * id : 0
     * name : RecyclerView的重构之路(一)
     * url : http://www.jianshu.com/p/99cd83778373
     */

    @SerializedName("blog")
    private List<Blog> blog;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public List<Blog> getBlog() {
        return blog;
    }

    public void setBlog(List<Blog> blog) {
        this.blog = blog;
    }
}
