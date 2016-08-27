package com.shrimpcolo.johnnytam.idouban.entity;

import java.io.Serializable;

public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1317025437668609819L;

    private String userIcon;
    private String userName;
    private Gender userGender;
    private String userNote;

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Gender getUserGender() {
        return userGender;
    }

    public void setUserGender(Gender userGender) {
        this.userGender = userGender;
    }

    public String getUserNote() {
        return userNote;
    }

    public void setUserNote(String userNote) {
        this.userNote = userNote;
    }

    public static enum Gender {MALE, FEMALE}

}
