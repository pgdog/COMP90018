package com.example.comp90018.dataBean;


public class FriendRequest {
    private String photoUrl;
    private String requestUserId;
    private String currentUserId;
    private String requestUserName;

    //to do all actions related to friend request, we need
    //url of this user, uid of this user, uid of the current user and user name of this user
    public FriendRequest(String photoUrl,String requestUserId,String currentUserId, String requestUserName){
        this.photoUrl = photoUrl;
        this.requestUserId = requestUserId;
        this.currentUserId = currentUserId;
        this.requestUserName = requestUserName;
    }

    public String getPhotoUrl(){return photoUrl;}
    public String getRequestUserId(){return requestUserId;}
    public String getCurrentUserId(){return currentUserId;}
    public String getRequestUserName(){return requestUserName;}
}
