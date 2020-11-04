package com.example.comp90018.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.comp90018.R;
import com.example.comp90018.utils.DataManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AddFriendActivity extends AppCompatActivity {
    private Button backBtn;
    private ImageView imageView;
    private TextView nameText;
    private TextView sendText;
    private LinearLayout sendView;

    private String id;
    private String picture;
    private String name;
    private boolean isPending;

    private DataManager dataManager;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Cancel the title
        if (getSupportActionBar() != null)

        {
            getSupportActionBar().hide();

        }
        //Bind the layout
        setContentView(R.layout.activity_add_friend);

        initData();
        //Initialize view
        initView();
    }

    public void initData(){
        dataManager=DataManager.getDataManager(this);
        databaseReference=dataManager.getDatabaseReference();
        Intent intent=getIntent();
        id=intent.getStringExtra(MainViewActivity.VALUES_FRIEND_ID);
        picture=intent.getStringExtra("Picture");
        name=intent.getStringExtra("Name");
        isPending=intent.getBooleanExtra("IsPending",false);
    }

    public void initView(){
        backBtn=(Button)findViewById(R.id.add_friend_back_btn);
        imageView=(ImageView)findViewById(R.id.add_friend_image);
        nameText=(TextView)findViewById(R.id.add_friend_name_text);
        sendText=(TextView)findViewById(R.id.add_friend_send_text);
        sendView=(LinearLayout) findViewById(R.id.add_friend_send_view);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Picasso.get().load(picture).into(imageView);
        nameText.setText(name);
        if(isPending){
            sendText.setText("Pending");
            sendView.setEnabled(false);
        }else{
            sendText.setText("Send Request");
            sendView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendRequest();
                }
            });

            sendView.setOnTouchListener(new View.OnTouchListener() {
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

    //action when send request button is pressed
    //add current user's uid under target user's friend request
    public void sendRequest(){
        databaseReference.child("request").child(id).push().setValue(dataManager.getUser().getID());
        isPending=true;
        sendText.setText("Pending");
        sendView.setEnabled(false);
    }
}
