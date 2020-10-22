package com.example.comp90018.dataBean;

import android.graphics.Bitmap;

public class MeItem {
    private int icImage;
    private String itemName;
    private int itemType;

    public static final int ITEM_TYPE_SETTING=1;

    public MeItem(int icImage,String itemName,int itemType){
        this.icImage=icImage;
        this.itemName=itemName;
        this.itemType=itemType;
    }

    public int getIcImage(){
        return icImage;
    }
    public String getItemName(){
        return itemName;
    }
    public int getItemType(){
        return itemType;
    }
}
