package com.example.comp90018.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.comp90018.R;
import com.example.comp90018.adapter.ChatListAdapter;
import com.example.comp90018.dataBean.ChatItem;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    //The list of chat data
    private List<ChatItem> chatItems=new ArrayList<ChatItem>();

    //Views
    private TextView titleText;
    private Button backBtn;
    private RecyclerView recyclerView;
    private EditText inputText;
    private Button addBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Bind the layout
        setContentView(R.layout.activity_chat);

        //Initialize data
        initData();
        //Initialize view
        initView();
    }

    public void initData(){
        //Get all data here
        testData();
    }

    public void initView(){
        //Create view here
        titleText=findViewById(R.id.chat_title);
        backBtn=findViewById(R.id.chat_back_btn);
        recyclerView=findViewById(R.id.chat_recycler);
        inputText=findViewById(R.id.chat_edit_text);
        addBtn=findViewById(R.id.chat_add_btn);

        ChatListAdapter chatListAdapter=new ChatListAdapter(chatItems);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setAdapter(chatListAdapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(chatListAdapter.getItemCount()-1);
    }

    public void testData(){
        Bitmap testPic= BitmapFactory.decodeResource(getResources(),R.drawable.test_image);
        chatItems.add(new ChatItem(testPic,"text from me 546s5d46f5sd4f564655464564564sdfsdafsfs",true));
        chatItems.add(new ChatItem(testPic,"text from me",true));
        chatItems.add(new ChatItem(testPic,"text from others sadfaadz1321321gfafadsfa12313123sdfadf23",false));
        chatItems.add(new ChatItem(testPic,"text from me",true));
        chatItems.add(new ChatItem(testPic,"text from others",false));
        chatItems.add(new ChatItem(testPic,"text from others",false));
        chatItems.add(new ChatItem(testPic,"text from me",true));
        chatItems.add(new ChatItem(testPic,"text from others",false));
        chatItems.add(new ChatItem(testPic,"text from me",true));
    }
}