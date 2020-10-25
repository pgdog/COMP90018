package com.example.comp90018.dataBean;

import android.graphics.Bitmap;

/**
 * The information of a friend's profile
 */
public class FriendProfile {
    private int ID;
    private String name;
    private Bitmap image;

    //Could add more here

    public FriendProfile(int ID, String name, Bitmap image) {
        this.ID = ID;
        this.name = name;
        this.image = image;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public Bitmap getImage() {
        return image;
    }
}
