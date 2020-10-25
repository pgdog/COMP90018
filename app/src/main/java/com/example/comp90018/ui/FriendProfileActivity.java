package com.example.comp90018.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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

public class FriendProfileActivity extends AppCompatActivity {
    //Views
    private Button backBtn;
    private ImageView imageView;
    private TextView nameText;
    private LinearLayout sendMessageView;

    //data
    private FriendProfile friendProfile;
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
        Intent intent=getIntent();
        int friendID=intent.getIntExtra(MainViewActivity.VALUES_FRIEND_ID,0);
        if(friendID!=0){
            friendProfile=DataManager.getDataManager(this).getFriendProfile(friendID);
        }
    }

    public void initView(){
        backBtn=(Button)findViewById(R.id.friend_profile_back_btn);
        imageView=(ImageView)findViewById(R.id.friend_profile_image);
        nameText=(TextView)findViewById(R.id.friend_profile_name_text);
        sendMessageView=(LinearLayout)findViewById(R.id.friend_profile_chat);

        imageView.setImageBitmap(this.friendProfile.getImage());
        nameText.setText(this.friendProfile.getName());

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
                intent.putExtra(MainViewActivity.VALUES_FRIEND_ID,friendProfile.getID());
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
    }
}