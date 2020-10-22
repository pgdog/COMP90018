package com.example.comp90018.dataBean;

import android.graphics.Bitmap;

import com.example.comp90018.adapter.FriendListAdapter;

import java.io.Serializable;

public class FriendItem {
    private Bitmap image;
    private String name;

    private int itemType;

    public FriendItem(Bitmap image,String name){
        this.image=image;
        this.name=name;
        this.itemType= FriendListAdapter.VIEW_HOLEDER_TYPE_NORMAL;
    }

    public FriendItem(Bitmap image,String name,int itemType){
        this.image=image;
        this.name=name;
        this.itemType= itemType;
    }

    public Bitmap getImage(){
        return this.image;
    }
    public String getName(){
        return this.name;
    }
    public int getItemType(){return this.itemType;}
}
