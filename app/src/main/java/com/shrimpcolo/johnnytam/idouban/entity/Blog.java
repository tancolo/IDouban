package com.shrimpcolo.johnnytam.idouban.entity;

import java.io.Serializable;

/**
 * Created by Johnny Tam on 2016/9/7.
 */

public class Blog implements Serializable {

    private int id;
    private String name;
    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
