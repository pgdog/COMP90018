package com.example.comp90018.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.comp90018.R;
import com.example.comp90018.dataBean.FriendItem;
import com.example.comp90018.utils.DataManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FriendRequestActivity extends AppCompatActivity {
    private Button backBtn;
    private ImageView imageView;
    private TextView nameText;
    private TextView contentText;
    private LinearLayout acceptView;
    private LinearLayout rejectView;

    private String id;
    private String pic;
    private String name;
    private String content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Cancel the title
        if (getSupportActionBar() != null)

        {
            getSupportActionBar().hide();

        }
        //Bind the layout
        setContentView(R.layout.activity_friend_request);

        //Initialize data
        initData();
        //Initialize view
        initView();
    }

    public void initData(){
        Intent intent=getIntent();
        id=intent.getStringExtra(MainViewActivity.VALUES_FRIEND_ID);
        pic=intent.getStringExtra("Picture");
        name=intent.getStringExtra("Name");
        content=intent.getStringExtra("Content");
    }
    public void initView(){
        backBtn=(Button)findViewById(R.id.friend_request_back_btn);
        imageView=(ImageView)findViewById(R.id.friend_request_image);
        nameText=(TextView)findViewById(R.id.friend_request_name_text);
        contentText=(TextView)findViewById(R.id.friend_request_content_text);
        acceptView=(LinearLayout)findViewById(R.id.friend_request_accept_view);
        rejectView=(LinearLayout)findViewById(R.id.friend_request_reject_view);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Picasso.get().load(pic).into(imageView);
        nameText.setText(name);
        contentText.setText(content);

        acceptView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptRequest();
            }
        });

        acceptView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        view.setBackgroundColor(getColor(R.color.colorLightGrey));
                        break;
                    case MotionEvent.ACTION_UP:
                        view.setBackground(getDrawable(R.drawable.shape_border));
                        break;
                }
                return false;
            }
        });

        rejectView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rejectRequest();
            }
        });

        rejectView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        view.setBackgroundColor(getColor(R.color.colorLightGrey));
                        break;
                    case MotionEvent.ACTION_UP:
                        view.setBackground(getDrawable(R.drawable.shape_border));
                        break;
                }
                return false;
            }
        });
    }

    public void rejectRequest(){
        //Reject the friend request
        //-------------update the data here---------------

        //Only delete the request
        DataManager dataManager=DataManager.getDataManager(getApplicationContext());
        DatabaseReference databaseReference= dataManager.getDatabaseReference();

        //Delete the data from local value
        for(int i=0;i<dataManager.getNewFriendItems().size();i++){
            if(dataManager.getNewFriendItems().get(i).getID().equals(id)){
                dataManager.getNewFriendItems().remove(i);
                break;
            }
        }
        dataManager.setLocalRequestChanged(true);

        //Delete the data from firebase
        databaseReference.child("request").child(dataManager.getUser().getID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot realSnapShot : snapshot.getChildren()) {
                    //Delete the friend request
                    if (id.equals((String) realSnapShot.getValue())) {
                        realSnapShot.getRef().setValue(null);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //------------------------------------------------

        //Go back to the last activity
        setResult(NewFriendsActivity.CODE_FROM_FRIEND_REQUEST_DATA_CHANGED);
        finish();
    }
    public void acceptRequest(){
        //Accept the friend request
        //-------------update the data here---------------
        // add current user's uid from target user's friend list
        //add target user's uid from current user's friend list
        DataManager dataManager=DataManager.getDataManager(getApplicationContext());
        DatabaseReference databaseReference= dataManager.getDatabaseReference();

        databaseReference.child("users").child(id)
                .child("friends").push().setValue(dataManager.getUser().getID());
        databaseReference.child("users").child(dataManager.getUser().getID())
                .child("friends").push().setValue(id);

        //Add the friend to local
        FriendItem newFriend=new FriendItem(id,pic,name);
        dataManager.getFriendItems().add(newFriend);

        //Delete the request data from local value
        for(int i=0;i<dataManager.getNewFriendItems().size();i++){
            if(dataManager.getNewFriendItems().get(i).getID().equals(id)){
                dataManager.getNewFriendItems().remove(i);
                break;
            }
        }
        dataManager.setLocalRequestChanged(true);

        //delete friend request of current user from target user's request
        databaseReference.child("request").child(dataManager.getUser().getID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot realSnapShot : snapshot.getChildren()) {
                    //Delete the friend request
                    if (id.equals((String) realSnapShot.getValue())) {
                        realSnapShot.getRef().setValue(null);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //------------------------------------------------

        //Go back to the last activity
        setResult(NewFriendsActivity.CODE_FROM_FRIEND_REQUEST_FRIEND_CHANGED);
        finish();
    }
}
