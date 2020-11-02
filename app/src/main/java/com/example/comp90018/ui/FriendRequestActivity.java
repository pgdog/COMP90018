package com.example.comp90018.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.comp90018.R;

public class FriendRequestActivity extends AppCompatActivity {
    private Button backBtn;
    private ImageView imageView;
    private TextView nameText;
    private TextView contentText;
    private LinearLayout acceptView;
    private LinearLayout rejectView;

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

        nameText.setText(name);
        contentText.setText(content);

        acceptView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Accept the friend request
                //-------------update the data here---------------

                //------------------------------------------------

                //Go back to the last activity
                setResult(1);
                finish();
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
                //Reject the friend request
                //-------------update the data here---------------

                //------------------------------------------------

                //Go back to the last activity
                setResult(1);
                finish();
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
}
