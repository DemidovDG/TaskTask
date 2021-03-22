package com.example.taskdrom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.taskdrom.networktools.model.GitRepo;

public class IssuesActivity extends AppCompatActivity {
    TextView textViewName;
    TextView textViewDesc;
    ImageView imageView;

    TextView textScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issues);
        textViewName = (TextView) findViewById(R.id.text_issues1);
        textViewDesc = (TextView) findViewById(R.id.text_issues2);
        textScroll = (TextView) findViewById(R.id.text_in_scroll);
        imageView = (ImageView) findViewById(R.id.image_view_issues);

        Intent intent = getIntent();
        textViewName.setText(intent.getStringExtra("full_name"));
        textViewDesc.setText(intent.getStringExtra("desc"));
        textScroll.setText(intent.getStringExtra("issues"));
        Bitmap b = BitmapFactory.decodeByteArray(
                intent.getByteArrayExtra("bitmap"),0,intent.getByteArrayExtra("bitmap").length
        );
        imageView.setImageBitmap(b);

    }
}