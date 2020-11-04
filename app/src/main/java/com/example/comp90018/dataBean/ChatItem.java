package com.example.comp90018.dataBean;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * The item of message record list
 */
public class ChatItem {
    //The id of a user
    private String id;
    //The image of a user
    private String image;
    //A message record text
    private String text;
    //Whether this message is from the user himself,
    //if so, the view will use different structure
    private boolean isSelf;
    //The time millis of a message record, used for sort
    private long date;

    public ChatItem(){}

    public ChatItem(String id, String image, String text, boolean isSelf,long date){
        this.id=id;
        this.image=image;
        this.text=text;
        this.isSelf=isSelf;
        this.date=date;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSelf(boolean self) {
        isSelf = self;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage(){
        return this.image;
    }
    public String getText(){
        return this.text;
    }
    public boolean isSelf(){
        return this.isSelf;
    }
    public long getDate(){return this.date;}
}
