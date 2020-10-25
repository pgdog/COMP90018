package com.example.comp90018.dataBean;

import android.graphics.Bitmap;

import com.example.comp90018.adapter.FriendListAdapter;

import java.io.Serializable;

/**
 * The item of message record list
 */
public class FriendItem {
    //The ID of a friend
    private int ID;
    //The image of a friend
    private Bitmap image;
    //The name of a Friend
    private String name;

    //The type used to create views, don't set it by yourself
    private int itemType;

    public FriendItem(int ID,Bitmap image,String name){
        this.ID=ID;
        this.image=image;
        this.name=name;
        this.itemType= FriendListAdapter.VIEW_HOLEDER_TYPE_NORMAL;
    }

    public FriendItem(int ID,Bitmap image,String name,int itemType){
        this.ID=ID;
        this.image=image;
        this.name=name;
        this.itemType= itemType;
    }

    public int getID(){return this.ID;}
    public Bitmap getImage(){
        return this.image;
    }
    public String getName(){
        return this.name;
    }
    public int getItemType(){return this.itemType;}
}
