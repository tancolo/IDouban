package com.shrimpcolo.johnnytam.idouban.entity;

import java.io.Serializable;

/**
 * Created by Johnny Tam on 2016/8/27.
 */
public class QQEntity implements Serializable{
    private static final long serialVersionUID = -4105891348205797257L;

    private String nickname;
    //private String city;
    private String gender;
    //private String province;
    private String figureurl_qq_2;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

//    public String getCity() {
//        return city;
//    }
//
//    public void setCity(String city) {
//        this.city = city;
//    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

//    public String getProvince() {
//        return province;
//    }
//
//    public void setProvince(String province) {
//        this.province = province;
//    }

    public String getFigureurl_qq_2() {
        return figureurl_qq_2;
    }

    public void setFigureurl_qq_2(String figureurl_qq_2) {
        this.figureurl_qq_2 = figureurl_qq_2;
    }
}
