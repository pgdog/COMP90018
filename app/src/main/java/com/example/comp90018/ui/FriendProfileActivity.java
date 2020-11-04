package com.example.comp90018.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.comp90018.R;
import com.example.comp90018.dataBean.FriendItem;
import com.example.comp90018.dataBean.FriendProfile;
import com.example.comp90018.utils.DataManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FriendProfileActivity extends AppCompatActivity {
    //Views
    private Button backBtn;
    private ImageView imageView;
    private TextView nameText;
    private LinearLayout sendMessageView;
    private LinearLayout deleteFriendView;

    private DataManager dataManager;
    private DatabaseReference databaseReference;
    //data
    private String friendId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Cancel the title
        if (getSupportActionBar() != null)

        {
            getSupportActionBar().hide();

        }

        //Bind the layout
        setContentView(R.layout.activity_friend_profile);

        initData();
        initView();
    }

    public void initData(){
        dataManager=DataManager.getDataManager(this);
        databaseReference=dataManager.getDatabaseReference();
        Intent intent=getIntent();
        friendId=intent.getStringExtra(MainViewActivity.VALUES_FRIEND_ID);
    }

    public void initView(){
        backBtn=(Button)findViewById(R.id.friend_profile_back_btn);
        imageView=(ImageView)findViewById(R.id.friend_profile_image);
        nameText=(TextView)findViewById(R.id.friend_profile_name_text);
        sendMessageView=(LinearLayout)findViewById(R.id.friend_profile_chat);
        deleteFriendView=(LinearLayout)findViewById(R.id.friend_profile_delete_view);

        final FriendItem friend=DataManager.getDataManager(this).getAFriend(friendId);
        Picasso.get().load(friend.getImage()).into(imageView);

        nameText.setText(friend.getName());

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sendMessageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ChatActivity.class);
                intent.putExtra(MainViewActivity.VALUES_FRIEND_ID,friend.getID());
                startActivity(intent);
            }
        });

        sendMessageView.setOnTouchListener(new View.OnTouchListener() {
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

        deleteFriendView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Delete the friend from local
                for(FriendItem item:dataManager.getFriendItems()){
                    if(item.getID().equals(friendId)){
                        dataManager.getFriendItems().remove(item);
                        break;
                    }
                }

                //Delte the friend from firebase
                //delete current user from friend list of target user
                databaseReference.child("users").child(friendId).child("friends").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot postSnapShot : snapshot.getChildren()) {
                            //check if the searched user is friend of current user
                            if(dataManager.getUser().getID().equals((String)postSnapShot.getValue())){
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
                databaseReference.child("users").child(dataManager.getUser().getID()).child("friends").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot postSnapShot : snapshot.getChildren()) {
                            //check if the searched user is friend of current user
                            if(friendId.equals((String)postSnapShot.getValue())){
                                postSnapShot.getRef().setValue(null);
                                Log.i("delete result (2)", "delete from current user complete");
                            }

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                setResult(FriendsFragment.CODE_FROM_FRIEND_PROFILE_FRIEND_CHANGED);
                finish();
            }
        });

        deleteFriendView.setOnTouchListener(new View.OnTouchListener() {
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
}