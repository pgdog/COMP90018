package com.example.comp90018.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.comp90018.R;
import com.example.comp90018.adapter.FriendListAdapter;
import com.example.comp90018.adapter.MessageListAdapter;
import com.example.comp90018.dataBean.FriendItem;
import com.example.comp90018.utils.OnRecycleItemClickListener;
import com.example.comp90018.utils.RecycleItemTouchHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FriendsFragment extends Fragment {
    //The layout of this fragment
    View view;

    //The list of friends data
    private List<FriendItem> friendItems;

    //Views
    private TextView titleText;
    private Button addFriendBtn;
    private RecyclerView recyclerView;
    private SideIndexBar sideIndexBar;
    private TextView indexCenterText;

    private FriendListAdapter friendListAdapter;

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

        //Initialize view
        initView();

        return view;
    }

    public void initData(){
        friendItems = new ArrayList<FriendItem>();
        testData();
    }

    public void initView(){
        //Create views here
        titleText=(TextView)view.findViewById(R.id.friends_title);
        addFriendBtn=(Button)view.findViewById(R.id.friends_add_btn);
        recyclerView=(RecyclerView)view.findViewById(R.id.friends_recycler_view);
        sideIndexBar=(SideIndexBar)view.findViewById(R.id.friends_side_index_bar);
        indexCenterText=(TextView)view.findViewById(R.id.friends_center_index_text);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        friendListAdapter=new FriendListAdapter(friendItems);
        recyclerView.setAdapter(friendListAdapter);
        friendListAdapter.setOnItemClickListener(new OnRecycleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(getActivity().getApplicationContext(),FriendProfileActivity.class);
                FriendItem item=friendListAdapter.getFriendListItem().get(position);
                startActivity(intent);
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
                for(int i=0;i<items.size();i++){
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
    }

    public void testData(){
        Bitmap testPic= BitmapFactory.decodeResource(this.getContext().getResources(),R.drawable.test_image);
        friendItems.add(new FriendItem(testPic,"Alice"));
        friendItems.add(new FriendItem(testPic,"Bob"));
        friendItems.add(new FriendItem(testPic,"Tom"));
        friendItems.add(new FriendItem(testPic,"Jack"));
        friendItems.add(new FriendItem(testPic,"Cathy"));
        friendItems.add(new FriendItem(testPic,"Mark"));
        friendItems.add(new FriendItem(testPic,"Henry"));
        friendItems.add(new FriendItem(testPic,"Gary"));
        friendItems.add(new FriendItem(testPic,"Martin"));
        friendItems.add(new FriendItem(testPic,"James"));
        friendItems.add(new FriendItem(testPic,"152675"));
        friendItems.add(new FriendItem(testPic,"+0152675"));
        friendItems.add(new FriendItem(testPic,"#152675"));
    }
}