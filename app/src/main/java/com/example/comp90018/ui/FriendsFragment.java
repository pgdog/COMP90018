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
import android.widget.Toast;

import com.example.comp90018.R;
import com.example.comp90018.UsersActivity;
import com.example.comp90018.adapter.FriendListAdapter;
import com.example.comp90018.adapter.MessageListAdapter;
import com.example.comp90018.dataBean.FriendItem;
import com.example.comp90018.utils.DataManager;
import com.example.comp90018.utils.OnRecycleItemClickListener;
import com.example.comp90018.utils.RecycleItemTouchHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FriendsFragment extends Fragment implements View.OnClickListener {
    //The layout of this fragment
    View view;
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
        addFriendBtn=(Button)view.findViewById(R.id.friends_add_btn);
        addFriendBtn.setOnClickListener(this);
        //Initialize view
        initView();

        return view;
    }

    public void initData(){
        DataManager.getDataManager(getActivity()).createItemsForFriends();
    }

    public void initView(){
        //Create views here
        titleText=(TextView)view.findViewById(R.id.friends_title);

        recyclerView=(RecyclerView)view.findViewById(R.id.friends_recycler_view);
        sideIndexBar=(SideIndexBar)view.findViewById(R.id.friends_side_index_bar);
        indexCenterText=(TextView)view.findViewById(R.id.friends_center_index_text);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        friendListAdapter=new FriendListAdapter(DataManager.getDataManager(getActivity()).getFriendItems());
        recyclerView.setAdapter(friendListAdapter);
        friendListAdapter.setOnItemClickListener(new OnRecycleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(getActivity().getApplicationContext(),FriendProfileActivity.class);
                FriendItem item=friendListAdapter.getFriendListItem().get(position);
                intent.putExtra(MainViewActivity.VALUES_FRIEND_ID,item.getID());
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

    @Override
    public void onClick(View v){
        Intent intent=new Intent(getActivity().getApplicationContext(), UsersActivity.class);
        startActivity(intent);
    }
}