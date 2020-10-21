package com.example.comp90018.dataBean;

import android.graphics.Bitmap;

public class MessageItem {
    private Bitmap image;
    private String name;
    private String content;

    public MessageItem(Bitmap image,String name, String content){
        this.image=image;
        this.name=name;
        this.content=content;
    }

    public Bitmap getImage(){
        return this.image;
    }
    public String getName(){
        return this.name;
    }
    public String getContent(){
        return this.content;
    }
}
