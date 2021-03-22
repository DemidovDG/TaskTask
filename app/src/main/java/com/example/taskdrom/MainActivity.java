package com.example.taskdrom;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.taskdrom.api.GitRequest;
import com.example.taskdrom.model.GitRepo;
import com.example.taskdrom.model.Issue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private TextView textView;
    private ImageView imageView;
    private int page;
    private String str = "";


    private String testResponse;
    private OkHttpClient client = new OkHttpClient().newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.MINUTES)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editTextTextPersonName);
        textView = (TextView) findViewById(R.id.textView);
        imageView = (ImageView) findViewById(R.id.imageView);


    }


    @SuppressLint("SetTextI18n")
    public void push() {

        Thread thread = new Thread(() -> {
            try {
                HttpUrl.Builder httpBuilder = HttpUrl.parse("https://api.github.com/repos/android/testing-samples/issues").newBuilder();
                Request request = new Request.Builder().url(httpBuilder.build()).build();
                Response response = client.newCall(request).execute();
                testResponse = response.body().string();
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

        JSONArray object = new JSONArray();
        List<Issue> list = new ArrayList<>();
        try {
            object = new JSONArray(testResponse);
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


        list.size();
        object.length();

        textView.setText("");
    }

    public void push(View view) {
        Map<String, String> params = new HashMap<>();
        params.put(GitRequest.PARAM_QUERY, "testing-sample in:name");
        params.put(GitRequest.PARAM_PER_PAGE, "5");
        params.put(GitRequest.PARAM_PAGE, "1");


        GitRequest gitRequest = new GitRequest();
        str = gitRequest.get(GitRequest.SEARCH_REPO, params);
        List<GitRepo> repos = gitRequest.repoFromJSON(str);
        str = "";
        for(GitRepo repo : repos) {

            str += repo.getName() + "\n" + repo.getOwner() + "\n" + repo.getDesc() + "\n\n";
        }

        imageView.setImageBitmap(repos.get(0).getAvatar());
//        push();


        textView.setText(str);

    }

//https://avatars.githubusercontent.com/u/32689599?v=4
}