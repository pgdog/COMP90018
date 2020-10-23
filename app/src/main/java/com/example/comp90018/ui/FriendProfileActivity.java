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

public class FriendProfileActivity extends AppCompatActivity {
    //Views
    private Button backBtn;
    private ImageView imageView;
    private TextView nameText;
    private LinearLayout sendMessageView;

    //data
    private FriendItem friendItem;
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
        testData();
    }

    public void initView(){
        backBtn=(Button)findViewById(R.id.friend_profile_back_btn);
        imageView=(ImageView)findViewById(R.id.friend_profile_image);
        nameText=(TextView)findViewById(R.id.friend_profile_name_text);
        sendMessageView=(LinearLayout)findViewById(R.id.friend_profile_chat);

        imageView.setImageBitmap(this.friendItem.getImage());
        nameText.setText(this.friendItem.getName());

        sendMessageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ChatActivity.class);
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

    public void testData(){
        Bitmap testPic= BitmapFactory.decodeResource(getResources(),R.drawable.test_image);
        this.friendItem=new FriendItem(testPic,"Friend's Nicname");
    }
}