package com.example.comp90018.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.example.comp90018.R;
import com.example.comp90018.dataBean.User;
import com.example.comp90018.utils.DataManager;
import com.example.comp90018.utils.TestData;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class MainViewActivity extends AppCompatActivity {
    //Views
    private BottomNavigationView navView;

    //Fragments
    private MessageFragment messageFragment;
    private FriendsFragment friendsFragment;
    private MeFragment meFragment;
    private NearbyFragment nearbyFragment;

    //Some static value for transfer values between activitys
    public static final String VALUES_FRIEND_ID="FriendID";

   @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);

       //Cancel the title
       if (getSupportActionBar() != null)
       {
           getSupportActionBar().hide();
       }
       //Bind the layout
       setContentView(R.layout.activity_main_view);

       //Set the user's informaiton when the application started
       initUserData();
       //Initialize view
       initView();

    }

    public void initView(){
        //Create views here
        navView = findViewById(R.id.BottomNavigation_message);

        //Create all fragments
        messageFragment=new MessageFragment();
        friendsFragment=new FriendsFragment();
        meFragment=new MeFragment();
        nearbyFragment=new NearbyFragment();

        //Default: go to the message fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.myf,messageFragment).commitNow();

        //Listen to the item selected events
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.bottom_nav_message_item:
                        getSupportFragmentManager().beginTransaction().replace(R.id.myf,messageFragment).commitNow();
                        return true;
                    case R.id.bottom_nav_friends_item:
                        getSupportFragmentManager().beginTransaction().replace(R.id.myf,friendsFragment).commitNow();
                        return true;
                    case R.id.bottom_nav_nearby_item:
                        getSupportFragmentManager().beginTransaction().replace(R.id.myf,nearbyFragment).commitNow();
                        return true;
                    case R.id.bottom_nav_me_item:
                        getSupportFragmentManager().beginTransaction().replace(R.id.myf,meFragment).commitNow();
                        return true;
                }
                startActivityForResult(new Intent(),1);
                return false;
            }
        });

        //Used for message notification
//        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navView.getChildAt(0);
//        View tab = menuView.getChildAt(3);
//        BottomNavigationItemView itemView = (BottomNavigationItemView) tab;
//        View badge = LayoutInflater.from(this).inflate(R.layout.my, menuView, false);
//        itemView.addView(badge);
//        TextView textView = badge.findViewById(R.id.texT);
//        textView.setText(String.valueOf(1));
//        textView.setVisibility(View.VISIBLE);
    }

    /**
     * Set the user's data while the application start
     */
    public void initUserData(){
        DataManager.getDataManager(this).setUser(TestData.getTestData(this).testUser);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}