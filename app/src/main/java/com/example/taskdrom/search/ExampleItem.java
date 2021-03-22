package com.example.taskdrom.search;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

//Элемент в RecyclerView
public class ExampleItem {
    private Bitmap imageResource;
    private String text1;
    private String text2;
    public ExampleItem(Bitmap imageResource, String text1, String text2) {
        this.imageResource = imageResource;
        this.text1 = text1;
        this.text2 = text2;
    }
    public Bitmap getImageResource() {
        return imageResource;
    }
    public String getText1() {
        return text1;
    }
    public String getText2() {
        return text2;
    }
}
