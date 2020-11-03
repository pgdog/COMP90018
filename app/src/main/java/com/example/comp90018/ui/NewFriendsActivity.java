package com.example.comp90018.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comp90018.R;
import com.example.comp90018.adapter.NewFriendListAdapter;
import com.example.comp90018.dataBean.NewFriendItem;
import com.example.comp90018.utils.DataManager;
import com.example.comp90018.utils.OnRecycleItemClickListener;

public class NewFriendsActivity extends AppCompatActivity {
    private Button backBtn;
    private RecyclerView recyclerView;
    private NewFriendListAdapter newFriendListAdapter;

    private boolean isRequestChanged;
    private boolean isFriendChanged;

    public static int CODE_TO_FRIEND_REQUEST=1;
    public static int CODE_FROM_FRIEND_REQUEST_DATA_CHANGED=11;
    public static int CODE_FROM_FRIEND_REQUEST_FRIEND_CHANGED=12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isRequestChanged=false;
        isFriendChanged=false;
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
                NewFriendItem item=newFriendListAdapter.getNewFriendItems().get(position);
                Intent intent=new Intent(getApplicationContext(),FriendRequestActivity.class);
                intent.putExtra(MainViewActivity.VALUES_FRIEND_ID,item.getID());
                intent.putExtra("Picture",item.getImage());
                intent.putExtra("Name",item.getName());
                intent.putExtra("Content",item.getContent());
                startActivityForResult(intent,CODE_TO_FRIEND_REQUEST);
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(newFriendListAdapter);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isRequestChanged){
                    setResult(FriendsFragment.CODE_FROM_NEW_FRIEND_REQUEST_CHANGED);
                }
                if(isFriendChanged){
                    setResult(FriendsFragment.CODE_FROM_NEW_FRIEND_FRIEND_CHANGED);
                }
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CODE_TO_FRIEND_REQUEST && resultCode==CODE_FROM_FRIEND_REQUEST_DATA_CHANGED){
            isRequestChanged=true;
            initView();
        }
        if(requestCode==CODE_TO_FRIEND_REQUEST && resultCode==CODE_FROM_FRIEND_REQUEST_FRIEND_CHANGED){
            isFriendChanged=true;
            initView();
        }
    }
}
