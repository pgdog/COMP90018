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
    public List<MessageItem> testMessageItem;
    public Bitmap testPic;
    public List<ChatItem> testChatItem;
    public List<FriendItem> testFriendItems;
    public FriendProfile testFriendProfile;
    public List<NewFriendItem> testNewFriendsItem;

    private TestData(Context context){
        testPic= BitmapFactory.decodeResource(context.getResources(),R.drawable.test_image);

        testMessageItem=new ArrayList<MessageItem>();
        testMessageItem.add(new MessageItem(1,testPic,"test1","content1",0,new Date()));
        testMessageItem.add(new MessageItem(2,testPic,"test2","content2",0,new Date()));
        testMessageItem.add(new MessageItem(3,testPic,"test3","content3",0,new Date()));
        testMessageItem.add(new MessageItem(4,testPic,"test4","content1",0,new Date()));
        testMessageItem.add(new MessageItem(5,testPic,"test5","content2",0,new Date()));
        testMessageItem.add(new MessageItem(6,testPic,"test6","content3",0,new Date()));
        testMessageItem.add(new MessageItem(7,testPic,"test7","content1",0,new Date()));
        testMessageItem.add(new MessageItem(8,testPic,"test8","content2",0,new Date()));
        testMessageItem.add(new MessageItem(9,testPic,"test9","content3",0,new Date()));
        testMessageItem.add(new MessageItem(10,testPic,"test10","content1",0,new Date()));
        testMessageItem.add(new MessageItem(11,testPic,"test11","content2",0,new Date()));
        testMessageItem.add(new MessageItem(12,testPic,"test12","content12",0,new Date()));
        testMessageItem.add(new MessageItem(13,testPic,"test13","content1",0,new Date()));
        testMessageItem.add(new MessageItem(14,testPic,"test14","content2",0,new Date()));
        testMessageItem.add(new MessageItem(15,testPic,"test15","content12",0,new Date()));

        testChatItem=new ArrayList<ChatItem>();
        testChatItem.add(new ChatItem(testPic,"text from me 546s5d46f5sd4f564655464564564sdfsdafsfs",true, new Date()));
        testChatItem.add(new ChatItem(testPic,"text from me",true, new Date()));
        testChatItem.add(new ChatItem(testPic,"text from others sadfaadz1321321gfafadsfa12313123sdfadf23",false, new Date()));
        testChatItem.add(new ChatItem(testPic,"text from me",true, new Date()));
        testChatItem.add(new ChatItem(testPic,"text from others",false, new Date()));
        testChatItem.add(new ChatItem(testPic,"text from others",false, new Date()));
        testChatItem.add(new ChatItem(testPic,"text from me",true, new Date()));
        testChatItem.add(new ChatItem(testPic,"text from others",false, new Date()));
        testChatItem.add(new ChatItem(testPic,"text from me",true, new Date()));

        testFriendItems=new ArrayList<FriendItem>();
        testFriendItems.add(new FriendItem(1,testPic,"Alice"));
        testFriendItems.add(new FriendItem(2,testPic,"Bob"));
        testFriendItems.add(new FriendItem(3,testPic,"Tom"));
        testFriendItems.add(new FriendItem(4,testPic,"Jack"));
        testFriendItems.add(new FriendItem(5,testPic,"Cathy"));
        testFriendItems.add(new FriendItem(6,testPic,"Mark"));
        testFriendItems.add(new FriendItem(7,testPic,"Henry"));
        testFriendItems.add(new FriendItem(8,testPic,"Gary"));
        testFriendItems.add(new FriendItem(9,testPic,"Martin"));
        testFriendItems.add(new FriendItem(10,testPic,"James"));
        testFriendItems.add(new FriendItem(11,testPic,"152675"));
        testFriendItems.add(new FriendItem(12,testPic,"+0152675"));
        testFriendItems.add(new FriendItem(13,testPic,"#152675"));

        testFriendProfile=new FriendProfile(1,"Alice",testPic);

        testNewFriendsItem=new ArrayList<NewFriendItem>();
        testNewFriendsItem.add(new NewFriendItem(1,testPic,"New Friend 1","Hi, I'm new friend 1",new Date()));
        testNewFriendsItem.add(new NewFriendItem(2,testPic,"New Friend 2","Hi, I'm new friend 2",new Date()));
        testNewFriendsItem.add(new NewFriendItem(3,testPic,"New Friend 3","Hi, I'm new friend 3",new Date()));
        testNewFriendsItem.add(new NewFriendItem(4,testPic,"New Friend 4","Hi, I'm new friend 4",new Date()));
        testNewFriendsItem.add(new NewFriendItem(5,testPic,"New Friend 5","Hi, I'm new friend 5",new Date()));
    }

    public static TestData getTestData(Context context){
        if(testData==null){
            testData=new TestData(context);
        }
        return testData;
    }
}
