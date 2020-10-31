package com.example.comp90018;

import androidx.appcompat.app.AppCompatActivity;
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
import com.example.comp90018.ui.FriendProfileActivity;
import com.example.comp90018.ui.MainViewActivity;
import com.example.comp90018.ui.SideIndexBar;
import com.example.comp90018.utils.DataManager;
import com.example.comp90018.utils.OnRecycleItemClickListener;
import com.example.comp90018.utils.RecycleItemTouchHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UsersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FriendListAdapter friendListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        Log.i("Test", "executed");
        initData();
        recyclerView=(RecyclerView)findViewById(R.id.userList);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        friendListAdapter=new FriendListAdapter(DataManager.getDataManager(this).getFriendItems());
        recyclerView.setAdapter(friendListAdapter);
        friendListAdapter.setOnItemClickListener(new OnRecycleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(getApplicationContext(), FriendProfileActivity.class);
                FriendItem item=friendListAdapter.getFriendListItem().get(position);
                intent.putExtra(MainViewActivity.VALUES_FRIEND_ID,item.getID());
                startActivity(intent);
            }
        });
    }

    public void initData(){
        DataManager.getDataManager(this).createItemsForFriends();
    }
}