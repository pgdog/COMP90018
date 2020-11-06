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
    //The type of the message
    private int messageType;

    public static final int TYPE_TEXT=1;
    public static final int TYPE_PICTURE=2;

    public ChatItem(){}

    public ChatItem(String id, String image, String text, boolean isSelf,long date,int messageType){
        this.id=id;
        this.image=image;
        this.text=text;
        this.isSelf=isSelf;
        this.date=date;
        this.messageType=messageType;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
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
