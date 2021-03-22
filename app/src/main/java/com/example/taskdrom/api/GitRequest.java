package com.example.taskdrom.api;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.taskdrom.model.GitRepo;
import com.example.taskdrom.model.Issue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GitRequest {
    private String responseString;
    private Request request;

    private Request reqAvatar;
    private Response responseAvatar;

    private String requestStringIssues;
    private String responseStringIssues;

    private static final OkHttpClient client = new OkHttpClient().newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.MINUTES)
            .build();


    //https://api.github.com/search/repositories?q=aserf%20in:name&per_page=50&page=1
    public final static String FOR_ISSUES = "https://api.github.com/repos/";
    public final static String SEARCH_REPO = "https://api.github.com/search/repositories";
    public final static String PARAM_QUERY = "q";
    //этот параметр идет в q
    //?q=test in:name
    //public final static String PARAM_IN = "in";
    public final static String PARAM_PER_PAGE = "per_page";
    public final static String PARAM_PAGE = "page";

//    Map<String, String> params = new HashMap<>();
//        params.put(GitRequest.PARAM_QUERY, "testing-sample in:name");
//        params.put(GitRequest.PARAM_PER_PAGE, "5");
//        params.put(GitRequest.PARAM_PAGE, "1");
    public String get(String url, Map<String,String> params) {
        HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
        if (params != null) {
            for(Map.Entry<String, String> param : params.entrySet()) {
                httpBuilder.addQueryParameter(param.getKey(),param.getValue());
            }
        }

        request = new Request.Builder().url(httpBuilder.build()).build();

        Thread thread = new Thread(() -> {
            try {
                Response response = client.newCall(request).execute();
                responseString = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return responseString;
    }

    public List<GitRepo> repoFromJSON(String jsonResponse) {
        List<GitRepo> list = new ArrayList<>();

        try {
            JSONObject json = new JSONObject(jsonResponse);
            JSONArray items = json.getJSONArray("items");

            //для каждого репозитория
            for (int i = 0; i < items.length(); i++) {
                JSONObject obj = items.getJSONObject(i);
                String name = obj.getString("name");
                String owner = obj.getJSONObject("owner").getString("login");
                String desc = obj.getString("description");
                if(desc.length() > 100)
                    desc = desc.substring(0, 100) + "...";
                String url_avatar = obj.getJSONObject("owner").getString("avatar_url");
                String url_issues = FOR_ISSUES + owner + "/" + name + "/issues";

                Bitmap bitmap = getAvatar(url_avatar);
                List<Issue> issues = getListIssue(url_issues);

                GitRepo repo = new GitRepo(name, owner, desc, bitmap, issues);

                list.add(repo);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }


    private Bitmap getAvatar(String url) {
        reqAvatar = new Request.Builder().url(url).build();

        Thread thread = new Thread(() -> {
            try {
                responseAvatar = client.newCall(reqAvatar).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Bitmap mIcon11 = null;
        if (responseAvatar.isSuccessful()) {
            try {
                mIcon11 = BitmapFactory.decodeStream(responseAvatar.body().byteStream());
            } catch (Exception e) { e.printStackTrace(); }
        }

        return mIcon11;
    }

    public List<Issue> getListIssue(String url) {
        requestStringIssues = url;

        Thread thread = new Thread(() -> {
            try {
                HttpUrl.Builder httpBuilder = HttpUrl.parse(requestStringIssues).newBuilder();
                Request request = new Request.Builder().url(httpBuilder.build()).build();
                Response response = client.newCall(request).execute();
                responseStringIssues = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<Issue> list = new ArrayList<>();
        try {
            JSONArray object = new JSONArray(responseStringIssues);
            for(int i = 0; i < object.length(); i++) {
                JSONObject item = object.getJSONObject(i);
                String title = item.getString("title");
                String login = item.getJSONObject("user").getString("login");
                Issue issue = new Issue(title, login);
                list.add(issue);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return list;
    }
}
