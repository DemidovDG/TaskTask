package com.example.taskdrom.model;

import android.graphics.Bitmap;

public class GitRepo {
    private String name;
    private String owner;
    private String desc;
    private Bitmap avatar;

    public GitRepo(String name, String owner, String desc) {
        this.name = name;
        this.owner = owner;
        this.desc = desc;
    }



    public Bitmap getAvatar() {
        return avatar;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
