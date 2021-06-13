package com.example.taskdrom.networktools.model;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.List;

public class GitRepo {
    private String name;
    private String owner;
    private String desc;

    private Bitmap avatar;
    private List<Issue> issues;

    public GitRepo(String name, String owner, String desc, Bitmap avatar, List<Issue> issues) {
        this.name = name;
        this.owner = owner;
        this.desc = desc;
        //avatar.compress(Bitmap.CompressFormat.PNG, 90, new ByteArrayOutputStream());
        this.avatar = avatar;
        this.issues = issues;
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

    public List<Issue> getIssues() {
        return issues;
    }

    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }

    public String getStringIssues() {
        String str = "";

        if( issues == null || issues.isEmpty())
            return "Empty!";

        for(int i = 0; i < issues.size(); i++) {
            str += i + "\nTitle: " + issues.get(i).getTitle() + "\nLogin: " + issues.get(i).getLogin() + "\n\n";
        }

        return str;
    }
}
