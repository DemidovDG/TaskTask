package com.example.taskdrom;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.example.taskdrom.api.GitRequest;
import com.example.taskdrom.model.GitRepo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private TextView textView;
    private int page;
    private String str = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editTextTextPersonName);
        textView = (TextView) findViewById(R.id.textView);
    }


    public void push(View view) {
        Map<String, String> params = new HashMap<>();
        params.put(GitRequest.PARAM_QUERY, "test in:name");
        params.put(GitRequest.PARAM_PER_PAGE, "5");
        params.put(GitRequest.PARAM_PAGE, "1");


        GitRequest gitRequest = new GitRequest();
        str = gitRequest.get(GitRequest.SEARCH_REPO, params);
        List<GitRepo> repos = gitRequest.repoFromJSON(str);

        str = "";
        for(GitRepo repo : repos) {
            str += repo.getName() + "\n" + repo.getOwner() + "\n" + repo.getDesc() + "\n\n";
        }

        textView.setText(str);

    }


}