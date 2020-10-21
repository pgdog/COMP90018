package com.example.comp90018.dataBean;

import android.graphics.Bitmap;

public class ChatItem {
    private Bitmap image;
    private String text;
    private boolean isSelf;

    public ChatItem(Bitmap image, String text, boolean isSelf){
        this.image=image;
        this.text=text;
        this.isSelf=isSelf;
    }

    public Bitmap getImage(){
        return this.image;
    }
    public String getText(){
        return this.text;
    }
    public boolean isSelf(){
        return this.isSelf;
    }
}
