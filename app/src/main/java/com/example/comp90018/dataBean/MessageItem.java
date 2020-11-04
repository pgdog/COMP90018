package com.example.comp90018.dataBean;

import java.util.Date;

/**
 * The item of recent message list
 */
public class MessageItem {
    //The ID of a friend
    private String ID;
    //The image of a friend
    private String image;
    //The name of a friend
    private String name;
    //The latest message of a friend
    private String lastMessage;
    //The number of unread messages
    private int numOfUnread;
    //The  time millis of last message between this friend and the user, used for sorting
    private long lastMessageDate;

    public MessageItem(){}
    public MessageItem(String ID, String image, String name, String lastMessage, int numOfUnread, long lastMessageDate){
        this.ID=ID;
        this.image=image;
        this.name=name;
        this.lastMessage = lastMessage;
        this.numOfUnread=numOfUnread;
        this.lastMessageDate=lastMessageDate;
    }

    public String getID(){
        return this.ID;
    }
    public String getImage(){
        return this.image;
    }
    public String getName(){
        return this.name;
    }
    public String getLastMessage(){
        return this.lastMessage;
    }
    public int getNumOfUnread(){
        return this.numOfUnread;
    }
    public long getLastMessageDate(){
        return this.lastMessageDate;
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

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setNumOfUnread(int numOfUnread) {
        this.numOfUnread = numOfUnread;
    }

    public void setLastMessageDate(long lastMessageDate) {
        this.lastMessageDate = lastMessageDate;
    }
}
