package com.example.taskdrom;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MessageActivity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        String message = getIntent().getStringExtra("message");
        textView = (TextView) findViewById(R.id.textView2);
        textView.setText(message);
    }
}