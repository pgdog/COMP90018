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
import com.example.comp90018.utils.DataManager;
import com.example.comp90018.utils.OnRecycleItemClickListener;
import com.example.comp90018.utils.RecycleItemTouchHelper;
import com.example.comp90018.utils.TestData;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment {

    //Views
    private TextView titleText;
    private RecyclerView recyclerView;

    //A view bind the layout
    private View view;

    //The adapter of recycler view
    private MessageListAdapter messageListAdapter;

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

    /**
     * Set data needed for displaying recent message
     */
    public void initData(){
        DataManager.getDataManager(getActivity()).setMessageItems(TestData.getTestData(getActivity()).testMessageItem);
    }

    public void initView(){
        //Create views here
        recyclerView=(RecyclerView)view.findViewById(R.id.message_recycle);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        messageListAdapter=new MessageListAdapter(DataManager.getDataManager(getActivity()).getMessageItems());
        messageListAdapter.setOnItemClickListener(new OnRecycleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Go to chat activity
                Intent intent=new Intent(getActivity(),ChatActivity.class);
                intent.putExtra(MainViewActivity.VALUES_FRIEND_ID,DataManager.getDataManager(getActivity()).getMessageItems().get(position).getID());
                startActivity(intent);
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(messageListAdapter);

        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(new RecycleItemTouchHelper(messageListAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

}