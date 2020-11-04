package com.example.comp90018.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.comp90018.R;
import com.example.comp90018.dataBean.ChatItem;
import com.example.comp90018.dataBean.FriendItem;
import com.example.comp90018.dataBean.FriendProfile;
import com.example.comp90018.dataBean.MessageItem;
import com.example.comp90018.dataBean.NewFriendItem;
import com.example.comp90018.dataBean.User;
import com.example.comp90018.ui.MainViewActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A class used to create test data,
 * don't use it for the final version
 */
public class TestData {
    private static TestData testData;

    public Bitmap testPic;

    public FriendProfile testFriendProfile;

    private TestData(Context context){
        testPic= BitmapFactory.decodeResource(context.getResources(),R.drawable.test_image);

        testFriendProfile=new FriendProfile(1,"Alice",testPic);
    }

    public static TestData getTestData(Context context){
        if(testData==null){
            testData=new TestData(context);
        }
        return testData;
    }
}
