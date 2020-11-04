package com.example.comp90018.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.comp90018.R;
import com.example.comp90018.adapter.FriendListAdapter;
import com.example.comp90018.dataBean.FriendItem;
import com.example.comp90018.utils.DataManager;
import com.example.comp90018.utils.OnRecycleItemClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment {
    //The layout of this fragment
    View view;
    //Views
    private TextView titleText;
    private Button addFriendBtn;
    private RecyclerView recyclerView;
    private SideIndexBar sideIndexBar;
    private TextView indexCenterText;

    private FriendListAdapter friendListAdapter;


    private DataManager dataManager;
    private DatabaseReference databaseReference;

    private boolean dataReady;
    private boolean viewReady;
    //data
    List<String> friendIds; //The user's id of friend

    public FriendsFragment() {
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

        view = inflater.inflate(R.layout.fragment_friends, container, false);

        if(dataReady){
            //Initialize view
            initView();
        }
        viewReady=true;

        return view;
    }

    public void initData(){
        dataReady=false;
        viewReady=false;
        dataManager=DataManager.getDataManager(getActivity());
        databaseReference=dataManager.getDatabaseReference();
        //Get friends
        databaseReference.child("users").child(dataManager.getUser().getID()).child("friends").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Get all friend's id
                friendIds=new ArrayList<String>();
                for(DataSnapshot postSnapShot: snapshot.getChildren()){
                    friendIds.add((String)postSnapShot.getValue());
                }
                //Get the information of user whose id is in the lsit
                databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<FriendItem> friendItems=new ArrayList<FriendItem>();
                        for(DataSnapshot postSnapShot : snapshot.getChildren()) {
                            //if uid of this user appear in the friendItems arraylist,
                            //then add all related info into friendItems
                            if(friendIds.contains((String)postSnapShot.child("uid").getValue())){
                                FriendItem friendItem=new FriendItem((String)postSnapShot.child("uid").getValue(),(String)postSnapShot.child("photo").getValue(),(String)postSnapShot.child("username").getValue());
                                friendItems.add(friendItem);
                            }
                        }
                        dataManager.setFriendItems(friendItems);
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

    public void initView(){
        //Create views here

        titleText=(TextView)view.findViewById(R.id.friends_title);

        recyclerView=(RecyclerView)view.findViewById(R.id.friends_recycler_view);
        sideIndexBar=(SideIndexBar)view.findViewById(R.id.friends_side_index_bar);
        indexCenterText=(TextView)view.findViewById(R.id.friends_center_index_text);
        addFriendBtn=(Button)view.findViewById(R.id.friends_add_btn);

        addFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Go to the search activity
                Intent intent=new Intent(getActivity().getApplicationContext(), SearchActivity.class);
                getActivity().startActivityForResult(intent,MainViewActivity.REQUEST_CODE_FROM_FRIEND_TO_SEARCH);
            }
        });
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        friendListAdapter=new FriendListAdapter(DataManager.getDataManager(getActivity()).getFriendItems());
        recyclerView.setAdapter(friendListAdapter);
        friendListAdapter.setOnItemClickListener(new OnRecycleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                FriendItem item=friendListAdapter.getFriendListItem().get(position);
                if(item.getItemType()==FriendListAdapter.VIEW_HOLEDER_TYPE_NORMAL){
                    //Go to the friend's profile activity
                    Intent intent=new Intent(getActivity().getApplicationContext(),FriendProfileActivity.class);
                    intent.putExtra(MainViewActivity.VALUES_FRIEND_ID,item.getID());
                    getActivity().startActivityForResult(intent,MainViewActivity.REQUEST_CODE_FROM_FRIEND_TO_PROFILE);
                }else if(item.getItemType()==FriendListAdapter.VIEW_HOLEDER_TYPE_REQUEST){
                    //Go to the new friend activity
                    Intent intent=new Intent(getActivity().getApplicationContext(),NewFriendsActivity.class);
                    getActivity().startActivityForResult(intent,MainViewActivity.REQUEST_CODE_FROM_FRIEND_TO_NEW_FRIEND);
                }

            }
        });

        sideIndexBar.setOnIndexTouchListener(new SideIndexBar.IndexTouchListener() {
            @Override
            public void touch(String index) {
                //Display the index in the center
                indexCenterText.setVisibility(View.VISIBLE);
                indexCenterText.setText(index);

                //Scroll the list to the correct position
                List<FriendItem> items=friendListAdapter.getFriendListItem();
                for(int i=1;i<items.size();i++){
                    if(items.get(i).getName().charAt(0)-index.charAt(0)>=0){
                        ((LinearLayoutManager)recyclerView.getLayoutManager()).scrollToPositionWithOffset(i,0);
                        break;
                    }
                }
            }

            @Override
            public void unTouch() {
                indexCenterText.setVisibility(View.GONE);
            }
        });

        indexCenterText.setVisibility(View.GONE);
        showRequestNumText(DataManager.getDataManager(getActivity()).getNewFriendItems().size());
    }


    /**
     * Display the TextView of request num with a number, if the num is zero, hide it.
     * @param num the number of requests
     */
    public void showRequestNumText(int num){
        friendListAdapter.setRequestNum(num);
        friendListAdapter.notifyDataSetChanged();
    }

    public void updateListView(){
        initView();
    }

}