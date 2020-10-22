package com.example.comp90018.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.example.comp90018.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class MainViewActivity extends AppCompatActivity {
    //Views
    private BottomNavigationView navView;

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
        initView();

    }

    public void initView(){
        //Create views here
        navView = findViewById(R.id.BottomNavigation_message);
//        final MyFragment f1=new MyFragment();
//        final MyFragment f2=new MyFragment();
//        final MyFragment f3=new MyFragment();
//        final MyFragment f4=new MyFragment();
//        f1.setMessage("Message");
//        f2.setMessage("Friends");
//        f3.setMessage("Nearby");
//        f4.setMessage("Me");
        //Default to the message fragment
        final MessageFragment mf=new MessageFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.myf,mf).commitNow();

        //Create all fragments
        final FriendsFragment friendsFragment=new FriendsFragment();
        final MeFragment meFragment=new MeFragment();
        final NearbyFragment nearbyFragment=new NearbyFragment();

        //Listen to the item selected events
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.bottom_nav_message_item:
                        getSupportFragmentManager().beginTransaction().replace(R.id.myf,mf).commitNow();
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

}