package com.example.comp90018.dataBean;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * The item of recent message list
 */
public class MessageItem {
    //The ID of a friend
    private int ID;
    //The image of a friend
    private Bitmap image;
    //The name of a friend
    private String name;
    //The latest message of a friend
    private String content;
    //The number of unread messages
    private int numOfUnread;
    //The date of last message between this friend and the user, used for sorting
    private Date lastMessageDate;

    public MessageItem(int ID, Bitmap image,String name, String content,int numOfUnread,Date lastMessageDate){
        this.ID=ID;
        this.image=image;
        this.name=name;
        this.content=content;
        this.numOfUnread=numOfUnread;
        this.lastMessageDate=lastMessageDate;
    }

    public int getID(){
        return this.ID;
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
    public int getNumOfUnread(){
        return this.numOfUnread;
    }
    public Date getLastMessageDate(){
        return this.lastMessageDate;
    }
}
