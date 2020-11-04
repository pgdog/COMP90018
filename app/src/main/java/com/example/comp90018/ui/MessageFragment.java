package com.example.comp90018.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.comp90018.R;
import com.example.comp90018.adapter.MessageListAdapter;
import com.example.comp90018.dataBean.MessageItem;
import com.example.comp90018.utils.DataManager;
import com.example.comp90018.utils.OnRecycleItemClickListener;
import com.example.comp90018.utils.RecycleItemTouchHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageFragment extends Fragment {

    //Views
    private TextView titleText;
    private RecyclerView recyclerView;

    //A view bind the layout
    private View view;

    //The adapter of recycler view
    private MessageListAdapter messageListAdapter;

    //data
    private DataManager dataManager;
    private DatabaseReference databaseReference;

    private boolean dataReady;
    private boolean viewReady;

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

        if(dataReady){
            initView();
        }
        viewReady=true;
        return view;
    }

    /**
     * Set data needed for displaying recent message
     */
    public void initData(){
        dataReady=false;
        viewReady=false;
        dataManager=DataManager.getDataManager(getActivity());
        databaseReference=dataManager.getDatabaseReference();
        //get recent chat from firebase
        databaseReference.child("chat").child(dataManager.getUser().getID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Get all recent chat
                List<MessageItem> messageItems=new ArrayList<MessageItem>();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    MessageItem messageItem=new MessageItem();
                    String friendID=dataSnapshot.getKey();
                    long date=new Long(dataSnapshot.child("date").getValue().toString());
                    String lastMessage=dataSnapshot.child("text").getValue().toString();
                    messageItem.setID(friendID);
                    messageItem.setLastMessageDate(date);
                    messageItem.setLastMessage(lastMessage);
                    messageItems.add(messageItem);
                }
                dataManager.setMessageItems(messageItems);
                //According to the friend's id, get the friend's information
                databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(MessageItem messageItem:dataManager.getMessageItems()){
                            messageItem.setImage(snapshot.child(messageItem.getID()).child("photo").getValue().toString());
                            messageItem.setName(snapshot.child(messageItem.getID()).child("username").getValue().toString());
                        }
                        //According to the friend id get the unread message number
                        databaseReference.child("message").child(dataManager.getUser().getID()).child("unread").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(MessageItem messageItem:dataManager.getMessageItems()){
                                    int num=0;
                                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                                        if(messageItem.getID().equals(dataSnapshot.child("from").getValue().toString())){
                                            num++;
                                        }
                                    }
                                    messageItem.setNumOfUnread(num);
                                }
                                dataReady=true;
                                if(viewReady){
                                    initView();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
                MessageItem messageItem=DataManager.getDataManager(getActivity()).getMessageItems().get(position);
                intent.putExtra(MainViewActivity.VALUES_FRIEND_ID,messageItem.getID());
                intent.putExtra("Picture",messageItem.getImage());
                intent.putExtra("Name",messageItem.getName());
                getActivity().startActivityForResult(intent, MainViewActivity.REQUEST_CODE_FROM_MESSAGE_TO_CHAT);
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(messageListAdapter);

        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(new RecycleItemTouchHelper(messageListAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void updateListView(){
        messageListAdapter.notifyDataSetChanged();
    }
}