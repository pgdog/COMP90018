package com.example.comp90018.dataBean;

import android.graphics.Bitmap;

import com.example.comp90018.adapter.FriendListAdapter;

import java.io.Serializable;

/**
 * The item of message record list
 */
public class FriendItem {
    //The ID of a friend
    private String ID;
    //The image of a friend
    private String image;
    //The name of a Friend
    private String name;

    //The type used to create views, don't set it by yourself
    private int itemType;

    public FriendItem(String ID,String image,String name){
        this.ID=ID;
        this.image=image;
        this.name=name;
        this.itemType= FriendListAdapter.VIEW_HOLEDER_TYPE_NORMAL;
    }

    public FriendItem(String ID,String image,String name,int itemType){
        this.ID=ID;
        this.image=image;
        this.name=name;
        this.itemType= itemType;
    }

    public String getID(){return this.ID;}
    public String getImage(){
        return this.image;
    }
    public String getName(){
        return this.name;
    }
    public int getItemType(){return this.itemType;}
}
