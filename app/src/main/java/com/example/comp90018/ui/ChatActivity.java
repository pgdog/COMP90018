package com.example.comp90018.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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
    private Button sendBtn;

    ChatListAdapter chatListAdapter;

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
        titleText=(TextView)findViewById(R.id.chat_title);
        backBtn=(Button)findViewById(R.id.chat_back_btn);
        recyclerView=(RecyclerView) findViewById(R.id.chat_recycler);
        inputText=(EditText)findViewById(R.id.chat_edit_text);
        addBtn=(Button)findViewById(R.id.chat_add_btn);
        sendBtn=(Button)findViewById(R.id.chat_send_btn);

        chatListAdapter=new ChatListAdapter(DataManager.getDataManager(this).getChatItems());
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

        inputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text=editable.toString();
                if(text.length()==0){
                    sendBtn.setEnabled(false);
                }else{
                    sendBtn.setEnabled(true);
                }
            }
        });

        //hide the keyboard if touch the screen outside the keyboard
        inputText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    InputMethodManager manager = ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE));
                    if (manager != null)
                        manager.hideSoftInputFromWindow(view.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text=inputText.getText().toString();
                addDataToList(text,true);
                updateView();
            }
        });
    }

    /**
     * Add a new data to chat list
     * @param text
     * @param isSelf whether the text is from the user
     */
    public void addDataToList(String text,boolean isSelf){
        ChatItem item=new ChatItem();
        item.setText(text);
        item.setSelf(isSelf);
        item.setDate(new Date(System.currentTimeMillis()));
        if(isSelf){
            //The text is from the user, add his picture
            ;item.setImage(TestData.getTestData(this).testPic);
        }else{
            //The text is from the friend, add the friend's picture
            item.setImage(TestData.getTestData(this).testPic);
        }

        chatListAdapter.addItem(item);
    }

    /**
     * update the view
     */
    public void updateView(){
        //hide the keyboard and clear the EditText
        inputText.setText("");
        InputMethodManager manager = ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE));
        if (manager != null)
            manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        //update the RecyclerView
        chatListAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(chatListAdapter.getItemCount()-1);
    }
}