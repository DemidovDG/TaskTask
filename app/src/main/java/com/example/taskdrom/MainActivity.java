package com.example.taskdrom;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jcabi.github.RtGithub;
import com.jcabi.github.Github;
import com.jcabi.http.response.JsonResponse;


import org.json.JSONObject;

import java.io.IOException;
import java.util.List;



public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private TextView textView;
    private int page;
    private String str = "";
    private Github github;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editTextTextPersonName);
        textView = (TextView) findViewById(R.id.textView);
    }


    @SuppressLint({"ShowToast", "SetTextI18n"})
    public void push(View view) {
     //   Intent intent = new Intent(this, MessageActivity.class);
       // intent.putExtra("message", editText.getText().toString());
       // startActivity(intent);
        github = new RtGithub();

        Thread thread = new Thread(() -> {
            try {
                //запрос
                JsonResponse resp = github.entry().uri().path("/search/repositories").queryParam("q", "testi").back().fetch().as(JsonResponse.class);
                //сбор всех репозиториев
//                List<JSONObject> items = resp.json().readObject().getJsonArray("items").getValuesAs(Ject.class);
//
//                for(JsonObject item : items) {
//                    str += String.format("repo found: %s", item.get("full_name").toString()) + "\n";
//                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        try { thread.join(); } catch (InterruptedException e) { e.printStackTrace(); }

        textView.setText(str);

        //textView.setText(request.toString() + "\n" + request.getUri() + "\n\n");
        //textView.append(request.generateUri() + " ---URI\n\n");
        //textView.append(request.getUri() + "\n\n");

    }


}