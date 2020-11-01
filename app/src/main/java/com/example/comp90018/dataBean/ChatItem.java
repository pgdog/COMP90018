package com.example.comp90018.dataBean;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * The item of message record list
 */
public class ChatItem {
    //The image of a user
    private Bitmap image;
    //A message record text
    private String text;
    //Whether this message is from the user himself,
    //if so, the view will use different structure
    private boolean isSelf;
    //The date of a message record, used for sort
    private Date date;

    public ChatItem(){}

    public ChatItem(Bitmap image, String text, boolean isSelf,Date date){
        this.image=image;
        this.text=text;
        this.isSelf=isSelf;
        this.date=date;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSelf(boolean self) {
        isSelf = self;
    }

    public void setDate(Date date) {
        this.date = date;
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
    public Date getDate(){return this.date;}
}
