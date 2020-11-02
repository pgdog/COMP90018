package com.example.comp90018.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comp90018.R;
import com.example.comp90018.adapter.MessageListAdapter;
import com.example.comp90018.adapter.NewFriendListAdapter;
import com.example.comp90018.dataBean.FriendItem;
import com.example.comp90018.dataBean.NewFriendItem;
import com.example.comp90018.utils.DataManager;
import com.example.comp90018.utils.OnRecycleItemClickListener;
import com.example.comp90018.utils.RecycleItemTouchHelper;

public class NewFriendsActivity extends AppCompatActivity {
    private Button backBtn;
    private RecyclerView recyclerView;
    private NewFriendListAdapter newFriendListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Cancel the title
        if (getSupportActionBar() != null)

        {
            getSupportActionBar().hide();

        }
        //Bind the layout
        setContentView(R.layout.activity_new_friends);

        //Initialize view
        initView();
    }

    public void initView(){
        backBtn=(Button)findViewById(R.id.new_friend_back_btn);
        recyclerView=(RecyclerView)findViewById(R.id.new_friend_recycle_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        newFriendListAdapter=new NewFriendListAdapter(DataManager.getDataManager(this).getNewFriendItems());
        newFriendListAdapter.setOnItemClickListener(new OnRecycleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Go to FriendRequestActivity
                Log.d("mwg","here");
                NewFriendItem item=newFriendListAdapter.getNewFriendItems().get(position);
                Intent intent=new Intent(getApplicationContext(),FriendRequestActivity.class);
                intent.putExtra(MainViewActivity.VALUES_FRIEND_ID,item.getID());
                intent.putExtra("Name",item.getName());
                intent.putExtra("Content",item.getContent());
                startActivityForResult(intent,1);
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(newFriendListAdapter);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==1){
            initView();
        }
    }
}
