package com.example.comp90018.dataBean;

import android.graphics.Bitmap;

import java.util.Date;

public class NewFriendItem {
    //The ID of a friend
    private String ID;
    //The image of a friend
    private String image;
    //The name of a friend
    private String name;
    //The latest message of a friend
    private String content;
    //The date of the request
    private Date date;

    public NewFriendItem(){}
    public NewFriendItem(String ID, String image,String name, String content, Date date) {
        this.ID = ID;
        this.image=image;
        this.name = name;
        this.content = content;
        this.date = date;
    }

    public String getID() {
        return ID;
    }

    public String getImage() {
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

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setImage(String image) {
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
