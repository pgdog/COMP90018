package com.example.comp90018.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comp90018.MainActivity;
import com.example.comp90018.R;
import com.example.comp90018.dataBean.User;
import com.example.comp90018.utils.DataManager;
import com.example.comp90018.utils.TestData;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class MainViewActivity extends AppCompatActivity {
    //Views
    private BottomNavigationView navView;
    private BottomNavigationItemView messageTab;
    private BottomNavigationItemView friendsTab;
    private View messageBadge;
    private View friendsBadge;

    //Fragments
    private MessageFragment messageFragment;
    private FriendsFragment friendsFragment;
    private MeFragment meFragment;
    private NearbyFragment nearbyFragment;

    //Some static value for transfer values between activitys
    public static final String VALUES_FRIEND_ID="FriendID";

    private FirebaseAuth mAuth;
    FirebaseUser user;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       //get current user, if user not logged in, go to login activity
       mAuth = FirebaseAuth.getInstance();
       user = mAuth.getCurrentUser();
       if(user == null){
           Intent intent = new Intent(this, MainActivity.class);
           startActivity(intent);
           finish();
       }else{
           Toast.makeText(getApplicationContext(), "user email is "+user.getEmail(),
                   Toast.LENGTH_LONG).show();
       }

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

//      Used for message notification
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navView.getChildAt(0);
        View tab = menuView.getChildAt(3);
        messageTab=(BottomNavigationItemView)menuView.getChildAt(0);
        friendsTab=(BottomNavigationItemView)menuView.getChildAt(1);
        BottomNavigationItemView itemView = (BottomNavigationItemView) tab;
        messageBadge = LayoutInflater.from(this).inflate(R.layout.menu_badge, menuView, false);
        friendsBadge = LayoutInflater.from(this).inflate(R.layout.menu_badge, menuView, false);
        messageTab.addView(messageBadge);
        friendsTab.addView(friendsBadge);

        showFriendsBadge(DataManager.getDataManager(this).getNewFriendItems().size());
        showMessageBadge(108);
//        hideFriendsBadge();
//        hideMessageBadge();
    }

    /**
     * Set the user's data while the application start
     */
    public void initUserData(){
        DataManager.getDataManager(this).setNewFriendItems(TestData.getTestData(this).testNewFriendsItem);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    /**
     * Display the badge of message
     * @param num the number of message unread
     */
    public void showMessageBadge(int num){
        TextView textView = messageTab.findViewById(R.id.badge_text);
        if(num>99){
            textView.setText("99+");
        }else{
            textView.setText(String.valueOf(num));
        }

        messageBadge.setVisibility(View.VISIBLE);
    }

    /**
     * Hide the badge of message
     */
    public void hideMessageBadge(){
        messageBadge.setVisibility(View.INVISIBLE);
    }

    /**
     * Display the badge of friends
     * @param num the number of friend requests
     */
    public void showFriendsBadge(int num){
        TextView textView = friendsBadge.findViewById(R.id.badge_text);
        if(num>99){
            textView.setText("99+");
        }else{
            textView.setText(String.valueOf(num));
        }
        friendsBadge.setVisibility(View.VISIBLE);
    }

    /**
     * Hide the badge of friends
     */
    public void hideFriendsBadge(){
        friendsBadge.setVisibility(View.INVISIBLE);
    }
}