package com.example.comp90018.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseManager {
    private static FirebaseManager firebaseManager;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    //Listeners
    private ValueEventListener unreadMessageNumListener;
    private ValueEventListener friendRequestNumListener;
    private ValueEventListener chatChangeListener;
    private ValueEventListener friendRequestListener;
    private ValueEventListener friendListener;

    private FirebaseManager(){
        databaseReference= FirebaseDatabase.getInstance().getReference();
        firebaseAuth=FirebaseAuth.getInstance();
    }

    public void removeAllListener(){
        String userID=firebaseAuth.getCurrentUser().getUid();
        if(unreadMessageNumListener!=null){
            databaseReference.child("message").child(userID).child("unread").removeEventListener(unreadMessageNumListener);
            unreadMessageNumListener=null;
        }
        if(friendRequestNumListener!=null){
            databaseReference.child("request").child(userID).removeEventListener(friendRequestNumListener);
            friendRequestNumListener=null;
        }
        if(chatChangeListener!=null){
            databaseReference.child("chat").child(userID).removeEventListener(chatChangeListener);
            chatChangeListener=null;
        }
        if(friendRequestListener!=null){
            databaseReference.child("users").child(userID).child("friends").removeEventListener(friendRequestListener);
            friendRequestListener=null;
        }
        if(friendListener!=null){
            databaseReference.child("users").child(userID).child("friends").removeEventListener(friendListener);
            friendListener=null;
        }
    }

    public ValueEventListener getFriendListener() {
        return friendListener;
    }

    public void setFriendListener(ValueEventListener friendListener) {
        if(this.friendListener!=null){
            String userID=firebaseAuth.getCurrentUser().getUid();
            databaseReference.child("users").child(userID).child("friends").removeEventListener(this.friendListener);
            this.friendListener=null;
        }
        this.friendListener = friendListener;
    }

    public ValueEventListener getFriendRequestListener() {
        return friendRequestListener;
    }

    public void setFriendRequestListener(ValueEventListener friendRequestListener) {
        if(this.friendRequestListener!=null){
            String userID=firebaseAuth.getCurrentUser().getUid();
            databaseReference.child("request").child(userID).removeEventListener(this.friendRequestListener);
            this.friendRequestListener=null;
        }
        this.friendRequestListener = friendRequestListener;
    }

    public ValueEventListener getChatChangeListener() {
        return chatChangeListener;
    }

    public void setChatChangeListener(ValueEventListener chatChangeListener) {
        if(this.chatChangeListener!=null){
            String userID=firebaseAuth.getCurrentUser().getUid();
            databaseReference.child("chat").child(userID).removeEventListener(this.chatChangeListener);
            this.chatChangeListener=null;
        }
        this.chatChangeListener = chatChangeListener;
    }

    public ValueEventListener getFriendRequestNumListener() {
        return friendRequestNumListener;
    }

    public void setFriendRequestNumListener(ValueEventListener friendRequestNumListener) {
        if(this.friendRequestNumListener!=null){
            String userID=firebaseAuth.getCurrentUser().getUid();
            databaseReference.child("request").child(userID).removeEventListener(this.friendRequestNumListener);
            this.friendRequestNumListener=null;
        }
        this.friendRequestNumListener = friendRequestNumListener;
    }

    public ValueEventListener getUnreadMessageNumListener() {
        return unreadMessageNumListener;
    }

    public void setUnreadMessageNumListener(ValueEventListener unreadMessageNumListener) {
        if(this.unreadMessageNumListener!=null){
            String userID=firebaseAuth.getCurrentUser().getUid();
            databaseReference.child("message").child(userID).child("unread").removeEventListener(this.unreadMessageNumListener);
            this.unreadMessageNumListener=null;
        }
        this.unreadMessageNumListener = unreadMessageNumListener;
    }

    public static FirebaseManager getFirebaseManager(){
        if(firebaseManager==null){
            firebaseManager=new FirebaseManager();
        }
        return firebaseManager;
    }
}
