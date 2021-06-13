package com.example.taskdrom.networktools.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Issue implements Parcelable {
    private String title;
    private String login;

    public Issue(String title, String login) {
        this.title = title;
        this.login = login;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(login);
    }


}


