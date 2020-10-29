package com.example.comp90018.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.comp90018.R;
import com.example.comp90018.adapter.ChatListAdapter;
import com.example.comp90018.dataBean.ChatItem;
import com.example.comp90018.dataBean.MessageItem;
import com.example.comp90018.utils.DataManager;
import com.example.comp90018.utils.TestData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    //Views
    private TextView titleText;
    private Button backBtn;
    private RecyclerView recyclerView;
    private EditText inputText;
    private Button addBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Cancel the title
        if (getSupportActionBar() != null)

        {
            getSupportActionBar().hide();

        }
        //Bind the layout
        setContentView(R.layout.activity_chat);

        //Initialize data
        initData();
        //Initialize view
        initView();
    }

    public void initData(){
        //Get all data here
        int friendID=getIntent().getIntExtra(MainViewActivity.VALUES_FRIEND_ID,0);
        DataManager.getDataManager(this).createItemsForChat(friendID);
        DataManager.getDataManager(this).setMessageRead(friendID);
    }

    public void initView(){
        //Create view here
        titleText=findViewById(R.id.chat_title);
        backBtn=findViewById(R.id.chat_back_btn);
        recyclerView=findViewById(R.id.chat_recycler);
        inputText=findViewById(R.id.chat_edit_text);
        addBtn=findViewById(R.id.chat_add_btn);

        ChatListAdapter chatListAdapter=new ChatListAdapter(DataManager.getDataManager(this).getChatItems());
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setAdapter(chatListAdapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(chatListAdapter.getItemCount()-1);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}