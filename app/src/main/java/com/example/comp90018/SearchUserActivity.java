package com.example.comp90018;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.comp90018.adapter.FriendRequestAdapter;
import com.example.comp90018.dataBean.FriendRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SearchUserActivity extends AppCompatActivity {

    private EditText searchText;
    private Button searchButton,addFriend,deleteFriend,pending;
    private TextView userName,loadingText,requestUserText;
    private ImageView userPhoto;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mAuth;
    private boolean isPending = false,isFriend = false, userFound = false;
    private String searchString,searchUID="";
    private ListView listView;
    private ArrayList<FriendRequest> friendRequests;
    private ArrayList<String> friendRequestUid;
    private FriendRequestAdapter friendRequestAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        mAuth = FirebaseAuth.getInstance();
        searchText = (EditText) findViewById(R.id.searchText);
        searchButton = (Button) findViewById(R.id.searchButton);
        addFriend = (Button) findViewById(R.id.addFriend);
        deleteFriend = (Button) findViewById(R.id.deleteFriend);
        pending = (Button) findViewById(R.id.pendingButton);
        userName = (TextView) findViewById(R.id.searchResultUserName);
        userPhoto = (ImageView) findViewById(R.id.searchResultPhoto);
        loadingText = (TextView) findViewById(R.id.loadingText);
        requestUserText = (TextView) findViewById(R.id.requestUserText);
        listView = (ListView) findViewById(R.id.requestUserList);
        friendRequests = new ArrayList<FriendRequest>();
        friendRequestUid = new ArrayList<String>();
        friendRequestAdapter = new FriendRequestAdapter(SearchUserActivity.this,friendRequests);
        listView.setAdapter(friendRequestAdapter);
        loadRequestUser();
/*        friendRequests.add(new FriendRequest("https://firebasestorage.googleapis.com/v0/b/comp90018-9cb71.appspot.com/o/users%2FdIYAWqyvBkR39W53M9GjtgJT9b62.png?alt=media&token=ff72f9fb-6e1f-461f-9126-4bf23a967a03",
               "dIYAWqyvBkR39W53M9GjtgJT9b62",mAuth.getUid(),"ddlk"));
        friendRequests.add(new FriendRequest("https://firebasestorage.googleapis.com/v0/b/comp90018-9cb71.appspot.com/o/users%2Ftest.png?alt=media&token=c7b7a6a8-70a1-4b14-9667-e00b1d555a9d",
                "5HD6Dw7wsFh3E3eEIwbXBZNMJ5f2",mAuth.getUid(),"oiu"));
        friendRequests.add(new FriendRequest("https://firebasestorage.googleapis.com/v0/b/comp90018-9cb71.appspot.com/o/users%2Ftest.png?alt=media&token=c7b7a6a8-70a1-4b14-9667-e00b1d555a9d",
              "5HD6Dw7wsFh3E3eEIwbXBZNMJ5f2",mAuth.getUid(),"oiu"));
        friendRequests.add(new FriendRequest("https://firebasestorage.googleapis.com/v0/b/comp90018-9cb71.appspot.com/o/users%2Ftest.png?alt=media&token=c7b7a6a8-70a1-4b14-9667-e00b1d555a9d",
                "5HD6Dw7wsFh3E3eEIwbXBZNMJ5f2",mAuth.getUid(),"oiu"));
        friendRequests.add(new FriendRequest("https://firebasestorage.googleapis.com/v0/b/comp90018-9cb71.appspot.com/o/users%2Ftest.png?alt=media&token=c7b7a6a8-70a1-4b14-9667-e00b1d555a9d",
                "5HD6Dw7wsFh3E3eEIwbXBZNMJ5f2",mAuth.getUid(),"oiu"));*/

    }

    public void startSearch(View view){
        searchString = searchText.getText().toString();
        //get search string from text field and only start searching if it is not empty string
        if(!TextUtils.isEmpty(searchString)){
            loadingText.setText("searching...please wait");
            mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users");
            mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                //first onDataChange is for getting uid of the searched user
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userFound = false;
                    for (DataSnapshot postSnapShot : snapshot.getChildren()) {
                        //get photo , username and uid of the searched user
                        if (searchString.equals((String) postSnapShot.child("email").getValue())) {
                            searchUID = (String) postSnapShot.child("uid").getValue();
                            userName.setText((String) postSnapShot.child("username").getValue());
                            Picasso.get().load((String) postSnapShot.child("photo").getValue()).into(userPhoto);
                            userPhoto.setVisibility(View.VISIBLE);
                            userFound = true;
                            break;
                        }
                        Log.i("search result", "data is " + (String) postSnapShot.child("email").getValue());
                    }
                    //after getting uid, check if the user is already friend of the current user
                    mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getUid()).child("friends");
                    mDatabaseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            isFriend = false;
                            for (DataSnapshot postSnapShot : snapshot.getChildren()) {
                                //check if the searched user is friend of current user
                                if (searchUID.equals((String) postSnapShot.getValue())) {
                                    isFriend = true;
                                    isPending = false;
                                }
                                Log.i("search result", "data is " + (String) postSnapShot.child("email").getValue());
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    //after getting uid, get the pending statues of the searched user
                    if (!TextUtils.isEmpty(searchUID)){
                        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("request").child(searchUID);
                        mDatabaseRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                isPending = false;
                                for (DataSnapshot realSnapShot : snapshot.getChildren()) {
                                    //check if user has already send friend request
                                    if (mAuth.getUid().equals((String) realSnapShot.getValue())) {
                                        isPending = true;
                                        Log.i("search request result", "isPending set true ");
                                        break;
                                    }
                                }
                                //display appropriate button based on ispending and isfriend
                                changeButtonDisplay();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }else{
                        loadingText.setText("sorry, user not found");
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }else{
            loadingText.setText("please enter something in the search field");
        }
    }

    //action when add button is pressed
    //add current user's uid under target user's friend request
    public void addRequest(View view){
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mDatabaseRef.child("request").child(searchUID).push().setValue(mAuth.getUid());
    }

    //delete current user's uid from target user's friend list
    //delete target user's uid from current user's friend list
    public void deleteRequest(View view){
        //delete current user from friend list of target user
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(searchUID).child("friends");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapShot : snapshot.getChildren()) {
                    //check if the searched user is friend of current user
                    if(mAuth.getUid().equals((String)postSnapShot.getValue())){
                        postSnapShot.getRef().setValue(null);
                        Log.i("delete result (1)", "delete from target user complete");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        //delete target user from friend list of current user
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getUid()).child("friends");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapShot : snapshot.getChildren()) {
                    //check if the searched user is friend of current user
                    if(searchUID.equals((String)postSnapShot.getValue())){
                        postSnapShot.getRef().setValue(null);
                        Log.i("delete result (2)", "delete from current user complete");
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void loadRequestUser(){
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("request").child(mAuth.getUid());
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                friendRequestUid.clear();
                //get uid of all users that send friend request to current user
                for(DataSnapshot postSnapShot : snapshot.getChildren()) {
                    friendRequestUid.add((String)postSnapShot.getValue());
                    Log.i("search result", "adding value " + (String)postSnapShot.getValue());
                }
                //find photo url of these users
                mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users");
                mDatabaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        friendRequests.clear();
                        for(DataSnapshot postSnapShot : snapshot.getChildren()) {
                            //if uid of this user appear in the friendRequestUid arraylist,
                            //then add all related info into friendRequests
                            if(friendRequestUid.contains((String)postSnapShot.child("uid").getValue())){
                                friendRequests.add(new FriendRequest(
                                        (String)postSnapShot.child("photo").getValue(),
                                        (String)postSnapShot.child("uid").getValue(),
                                        mAuth.getUid(),
                                        (String)postSnapShot.child("username").getValue()));
                            }
                        }
                        //update the list view, very important!!
                        friendRequestAdapter.notifyDataSetChanged();
                        Log.i("test friendRequest", friendRequests.toString());
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    //display the button based on the information collected when searching the user
    public void changeButtonDisplay(){
        if(!isFriend && !isPending){
            addFriend.setVisibility(View.VISIBLE);
            deleteFriend.setVisibility(View.INVISIBLE);
            pending.setVisibility(View.INVISIBLE);
        }else if(isPending){
            pending.setVisibility(View.VISIBLE);
            addFriend.setVisibility(View.INVISIBLE);
            deleteFriend.setVisibility(View.INVISIBLE);
        }else if(isFriend){
            deleteFriend.setVisibility(View.VISIBLE);
            pending.setVisibility(View.INVISIBLE);
            addFriend.setVisibility(View.INVISIBLE);
        }
        if(userFound){
            loadingText.setText("user found");
        }else{
            loadingText.setText("sorry, no user found");
            userName.setText("");
            userPhoto.setVisibility(View.INVISIBLE);
            pending.setVisibility(View.INVISIBLE);
            addFriend.setVisibility(View.INVISIBLE);
            deleteFriend.setVisibility(View.INVISIBLE);
        }

    }
}