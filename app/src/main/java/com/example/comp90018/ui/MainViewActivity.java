package com.example.comp90018.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comp90018.MainActivity;
import com.example.comp90018.R;
import com.example.comp90018.dataBean.FriendItem;
import com.example.comp90018.dataBean.NewFriendItem;
import com.example.comp90018.utils.DataManager;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


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
    private FirebaseUser user;
    private DataManager dataManager;
    private DatabaseReference databaseReference;

    //Data
    List<String> requestIds; //The user's id of request

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

       //Listen to the request changed
       databaseReference.child("request").child(dataManager.getUser().getID()).addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               if(dataManager.isLocalRequestChanged()){
                   dataManager.setLocalRequestChanged(false);
                   showFriendsBadge(dataManager.getNewFriendItems().size());
                   return;
               }
               //Get all request
               requestIds=new ArrayList<String>();
               for(DataSnapshot postSnapShot: snapshot.getChildren()){
                   requestIds.add((String)postSnapShot.getValue());
               }

               //Get the information of user whose id is in requests
               databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       List<NewFriendItem> friendRequests=new ArrayList<NewFriendItem>();
                       for(DataSnapshot postSnapShot : snapshot.getChildren()) {
                           //if uid of this user appear in the friendRequestUid arraylist,
                           //then add all related info into friendRequests
                           if(requestIds.contains((String)postSnapShot.child("uid").getValue())){
                               NewFriendItem friendItem=new NewFriendItem();
                               friendItem.setID((String)postSnapShot.child("uid").getValue());
                               friendItem.setImage((String)postSnapShot.child("photo").getValue());
                               friendItem.setName((String)postSnapShot.child("username").getValue());
                               friendRequests.add(friendItem);
                           }
                       }
                       dataManager.setNewFriendItems(friendRequests);
                       initView();
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
    }

    /**
     * Set the user's data while the application start
     */
    public void initUserData(){
        dataManager=DataManager.getDataManager(this);
        //Listen the data in the firebase
        databaseReference=dataManager.getDatabaseReference();
    }


    /**
     * Display the badge of message, if the number is zero, hide the badge
     * @param num the number of message unread
     */
    public void showMessageBadge(int num){
        TextView textView = messageTab.findViewById(R.id.badge_text);
        if(num>99){
            textView.setText("99+");
        }else if(num==0){
            textView.setVisibility(View.INVISIBLE);
        }else{
            textView.setText(String.valueOf(num));
        }

        messageBadge.setVisibility(View.VISIBLE);
    }

    /**
     * Display the badge of friends, if the number is zero, hide the badge
     * @param num the number of friend requests
     */
    public void showFriendsBadge(int num){
        TextView textView = friendsBadge.findViewById(R.id.badge_text);
        if(num>99){
            textView.setText("99+");
        }else if(num==0){
            textView.setVisibility(View.INVISIBLE);
        }else{
            textView.setText(String.valueOf(num));
        }
        friendsBadge.setVisibility(View.VISIBLE);
    }

}