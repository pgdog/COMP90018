package com.example.comp90018.dataBean;

import android.graphics.Bitmap;

import java.util.Date;

public class NewFriendItem {
    //The ID of a friend
    private int ID;
    //The image of a friend
    private Bitmap image;
    //The name of a friend
    private String name;
    //The latest message of a friend
    private String content;
    //The date of the request
    private Date date;

    public NewFriendItem(int ID, Bitmap image,String name, String content, Date date) {
        this.ID = ID;
        this.image=image;
        this.name = name;
        this.content = content;
        this.date = date;
    }

    public int getID() {
        return ID;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public Date getDate() {
        return date;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
