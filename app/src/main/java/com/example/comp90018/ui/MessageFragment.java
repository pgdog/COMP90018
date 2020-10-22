package com.example.comp90018.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.comp90018.R;
import com.example.comp90018.adapter.MessageListAdapter;
import com.example.comp90018.dataBean.MessageItem;
import com.example.comp90018.utils.OnRecycleItemClickListener;
import com.example.comp90018.utils.RecycleItemTouchHelper;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment {

    //The list of message data
    private List<MessageItem> messageItems=new ArrayList<MessageItem>();

    //Views
    private TextView titleText;
    private RecyclerView recyclerView;

    //A view bind the layout
    private View view;

    public MessageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initialize data
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_message, container, false);

        //Initialize view
        initView();

        return view;
    }

    public void initData(){
        //Get all data here
        testData();
    }

    public void initView(){
        //Create views here
        recyclerView=(RecyclerView)view.findViewById(R.id.message_recycle);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        MessageListAdapter messageListAdapter=new MessageListAdapter(messageItems);
        recyclerView.setAdapter(messageListAdapter);
        messageListAdapter.setOnItemClickListener(new OnRecycleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Go to chat activity
                Intent intent=new Intent(getActivity().getApplicationContext(),ChatActivity.class);
                startActivity(intent);
            }
        });
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(new RecycleItemTouchHelper(messageListAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
    public void testData(){
        Bitmap testPic= BitmapFactory.decodeResource(this.getContext().getResources(),R.drawable.test_image);
        messageItems.add(new MessageItem(testPic,"test1","content1"));
        messageItems.add(new MessageItem(testPic,"test2","content2"));
        messageItems.add(new MessageItem(testPic,"test3","content3"));
        messageItems.add(new MessageItem(testPic,"test4","content1"));
        messageItems.add(new MessageItem(testPic,"test5","content2"));
        messageItems.add(new MessageItem(testPic,"test6","content3"));
        messageItems.add(new MessageItem(testPic,"test7","content1"));
        messageItems.add(new MessageItem(testPic,"test8","content2"));
        messageItems.add(new MessageItem(testPic,"test9","content3"));
        messageItems.add(new MessageItem(testPic,"test10","content1"));
        messageItems.add(new MessageItem(testPic,"test11","content2"));
        messageItems.add(new MessageItem(testPic,"test12","content12"));
        messageItems.add(new MessageItem(testPic,"test13","content1"));
        messageItems.add(new MessageItem(testPic,"test14","content2"));
        messageItems.add(new MessageItem(testPic,"test15","content12"));
    }
}