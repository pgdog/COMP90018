package com.example.comp90018.utils;

import android.content.Context;

import com.example.comp90018.dataBean.ChatItem;
import com.example.comp90018.dataBean.FriendItem;
import com.example.comp90018.dataBean.FriendProfile;
import com.example.comp90018.dataBean.MessageItem;
import com.example.comp90018.dataBean.NewFriendItem;
import com.example.comp90018.dataBean.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A data manager for all data operations
 * can't be create from other class
 * can be get from the get function
 */
public class DataManager {
    //default instance for the data manager
    private static DataManager dataManager;

    //The context call the manager
    private Context context;

    //The user login to the application
    private User user;
    //The list of MessageItem, used to display recent messages
    private List<MessageItem> messageItems;
    //The list of ChatItem, used to display message records
    private List<ChatItem> chatItems;
    //The list of FriendItem, used to display friends
    private List<FriendItem> friendItems;

    //The list of NewFriendItem, used to display friends request
    private List<NewFriendItem> newFriendItems;

    //The flag used to prevent data conflict
    private boolean isLocalRequestChanged;
    private boolean isLocalFriendChanged;



    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private DataManager(){
        messageItems=new ArrayList<>();
        chatItems=new ArrayList<>();
        friendItems=new ArrayList<>();
        newFriendItems=new ArrayList<>();
        isLocalRequestChanged =false;
        isLocalFriendChanged = false;
    }

    public boolean isLocalFriendChanged() {
        return isLocalFriendChanged;
    }

    public void setLocalFriendChanged(boolean localFriendChanged) {
        isLocalFriendChanged = localFriendChanged;
    }

    public boolean isLocalRequestChanged() {
        return isLocalRequestChanged;
    }

    public void setLocalRequestChanged(boolean localRequestChanged) {
        isLocalRequestChanged = localRequestChanged;
    }

    /**
     * Set the user's information, need a "User"
     * See details in the "User" class
     * @param user a instance of User
     */
    public void setUser(User user){
        this.user=user;
    }

    /**
     * Get the instance of User who login
     * @return the instance of User
     */
    public User getUser(){
        return this.user;
    }

    /**
     * Set the list of MessageItem
     * See detail in the "MessageItem"
     * @param messageItems A list of MessageItem
     */
    public void setMessageItems(List<MessageItem> messageItems){
        this.messageItems=messageItems;
    }

    public List<MessageItem> getMessageItems(){
        return this.messageItems;
    }
    /**
     * Create items for message fragment, seed details in the "MessageItem" class
     */
    public void createItemsForMessage(){
        //According to the user's ID, get all recent messages from database
        //Create a List of MessageItem and use setMessageItems function to set it
        //See MessageItem details in the "MessageItem" class
        String userID=user.getID();

        setMessageItems(TestData.getTestData(context).testMessageItem);//The data only used for test, don't use it for the final version
    }

    public List<ChatItem> getChatItems(){
        return this.chatItems;
    }

    public void setChatItems(List<ChatItem> chatItems){
        this.chatItems=chatItems;
    }
    /**
     * Create items for message fragment, seed details in the "ChatItem" class
     * @param friendID the ID of a friend
     */
    public void createItemsForChat(int friendID){
        //According to the user's ID and the friend's ID, get all message records from database
        //Create a List of ChatItem and use setChatItems function to set it
        //See ChatItem details in the "ChatItem" class
        String userID=user.getID();

        setChatItems(TestData.getTestData(context).testChatItem);//The data only used for test, don't use it for the final version
    }

    /**
     * According the user's ID and a friend's ID, tell server to set all the messages' status not unread
     * @param friendID
     */
    public void setMessageRead(int friendID){
        String userID=user.getID();

        //Need to be implemented here
    }

    public void setFriendItems(List<FriendItem> friendItems){
        this.friendItems=friendItems;
    }

    public List<FriendItem> getFriendItems(){
        return this.friendItems;
    }

    /**
     * Create items for Friends fragment, seed details in the "FriendItem" class
     */
    public void createItemsForFriends(){
        //According to the user's ID, get all friends data from database
        //Create a List of FriendItem and use setFriendItems function to set it
        //See FriendItem details in the "FriendItem" class
        String userID=user.getID();


    }

    /**
     * According the friend's ID, get the information from database
     * @param friendID The ID of a friend
     * @return A instance of FriendItem
     */
    public FriendItem getAFriend(String friendID){
        //According the friend's ID, get the information from database

        for(FriendItem item:friendItems){
            if(item.getID().equals(friendID)){
                return item;
            }
        }
        return null;
    }

    public void setNewFriendItems(List<NewFriendItem> newFriendItems){
        this.newFriendItems=newFriendItems;
    }

    public List<NewFriendItem> getNewFriendItems(){
        return this.newFriendItems;
    }

    private void setContext(Context context){
        this.context=context;
    }

    public void setDatabase(FirebaseDatabase database) {
        this.database = database;
    }

    public void setDatabaseReference(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public static DataManager getDataManager(Context context){
        if(dataManager == null){
            dataManager=new DataManager();
        }
        dataManager.setContext(context);
        return dataManager;
    }
}
