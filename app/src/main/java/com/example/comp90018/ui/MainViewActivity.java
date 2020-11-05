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
import com.example.comp90018.dataBean.NewFriendItem;
import com.example.comp90018.dataBean.User;
import com.example.comp90018.utils.DataManager;
import com.example.comp90018.utils.FirebaseManager;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

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
    public static final String VALUES_FRIEND_ID = "FriendID";

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DataManager dataManager;
    private DatabaseReference databaseReference;

    private int currentFragmentId;

    //Listener
    private ValueEventListener unreadMessageListener;


    public static final int REQUEST_CODE_FROM_MESSAGE_TO_CHAT = 1;
    public static final int REQUEST_CODE_FROM_FRIEND_TO_SEARCH=2;
    public static final int REQUEST_CODE_FROM_FRIEND_TO_PROFILE=3;
    public static final int REQUEST_CODE_FROM_FRIEND_TO_NEW_FRIEND=4;

    public static final int RESULT_CODE_FROM_CHAT_MESSAGE_CHANGED = 11;
    public static final int RESULT_CODE_FROM_SEARCH_FRIEND_FRIEND_CHANGED=21;
    public static final int RESULT_CODE_FROM_FRIEND_PROFILE_FRIEND_CHANGED=31;
    public static final int RESULT_CODE_FROM_NEW_FRIEND_REQUEST_CHANGED=41;
    public static final int RESULT_CODE_FROM_NEW_FRIEND_FRIEND_CHANGED=42;

    public static boolean isLocalFriendChanged=false;
    public static boolean isLocalRequestChanged=false;


    //Data
    List<String> requestIds; //The user's id of request

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get current user, if user not logged in, go to login activity
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "user email is " + user.getEmail(),
                    Toast.LENGTH_LONG).show();
            //Cancel the title
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }
            //Bind the layout
            setContentView(R.layout.activity_main_view);

            //Set the user's informaiton when the application started
            initUserData();
        }


    }

    public void initView() {
        //Create views here
        navView = findViewById(R.id.BottomNavigation_message);

        //Create all fragments
        if (messageFragment == null) {
            messageFragment = new MessageFragment();
            friendsFragment = new FriendsFragment();
            meFragment = new MeFragment();
            nearbyFragment = new NearbyFragment();
            //Default: go to the message fragment
            getSupportFragmentManager().beginTransaction().replace(R.id.myf, messageFragment).commitNow();
        }
        //Listen to the item selected events
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                hideAllFragment(fragmentTransaction);
                switch (item.getItemId()) {
                    case R.id.bottom_nav_message_item:
                        if (messageFragment.isAdded()) {
                            fragmentTransaction.show(messageFragment);
                        } else {
                            fragmentTransaction.add(R.id.myf, messageFragment);
                            fragmentTransaction.show(messageFragment);
                        }
                        fragmentTransaction.commitNow();
//                        getSupportFragmentManager().beginTransaction().replace(R.id.myf,messageFragment).commitNow();
                        return true;
                    case R.id.bottom_nav_friends_item:
                        if (friendsFragment.isAdded()) {
                            fragmentTransaction.show(friendsFragment);
                        } else {
                            fragmentTransaction.add(R.id.myf, friendsFragment);
                            fragmentTransaction.show(friendsFragment);
                        }
                        fragmentTransaction.commitNow();
//                        getSupportFragmentManager().beginTransaction().replace(R.id.myf,friendsFragment).commitNow();
                        return true;
                    case R.id.bottom_nav_nearby_item:
                        if (nearbyFragment.isAdded()) {
                            fragmentTransaction.show(nearbyFragment);
                        } else {
                            fragmentTransaction.add(R.id.myf, nearbyFragment);
                            fragmentTransaction.show(nearbyFragment);
                        }
                        fragmentTransaction.commitNow();
//                        getSupportFragmentManager().beginTransaction().replace(R.id.myf,nearbyFragment).commitNow();
                        return true;
                    case R.id.bottom_nav_me_item:
                        if (meFragment.isAdded()) {
                            fragmentTransaction.show(meFragment);
                        } else {
                            fragmentTransaction.add(R.id.myf, meFragment);
                            fragmentTransaction.show(meFragment);
                        }
                        fragmentTransaction.commitNow();
//                        getSupportFragmentManager().beginTransaction().replace(R.id.myf,meFragment).commitNow();
                        return true;
                }

                return false;
            }
        });

//      Used for message notification
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navView.getChildAt(0);
        View tab = menuView.getChildAt(3);
        messageTab = (BottomNavigationItemView) menuView.getChildAt(0);
        friendsTab = (BottomNavigationItemView) menuView.getChildAt(1);
        BottomNavigationItemView itemView = (BottomNavigationItemView) tab;
        messageBadge = LayoutInflater.from(this).inflate(R.layout.menu_badge, menuView, false);
        friendsBadge = LayoutInflater.from(this).inflate(R.layout.menu_badge, menuView, false);
        messageTab.addView(messageBadge);
        friendsTab.addView(friendsBadge);
    }

    public void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if (messageFragment != null) {
            fragmentTransaction.hide(messageFragment);
        }
        if (friendsFragment != null) {
            fragmentTransaction.hide(friendsFragment);
        }
        if (nearbyFragment != null) {
            fragmentTransaction.hide(nearbyFragment);
        }
        if (meFragment != null) {
            fragmentTransaction.hide(meFragment);
        }
    }

    /**
     * Set the user's data while the application start
     */
    public void initUserData() {
        dataManager = DataManager.getDataManager(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        dataManager.setDatabaseReference(databaseReference);
        if (dataManager.getUser() == null) {
            databaseReference = FirebaseDatabase.getInstance().getReference();
            //Get user's information
            databaseReference.child("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User newUser = new User();
                    String id = user.getUid();
                    String username = (String) snapshot.child("username").getValue();
                    String pic = (String) snapshot.child("photo").getValue();
                    newUser.setID(id);
                    newUser.setUserName(username);
                    newUser.setImage(pic);
                    DataManager.getDataManager(getApplicationContext()).setUser(newUser);
                    DataManager.getDataManager(getApplicationContext()).setDatabase(FirebaseDatabase.getInstance());
                    DataManager.getDataManager(getApplicationContext()).setDatabaseReference(databaseReference);
                    initView();
                    listenRequestDataChanged();
                    listenMessageDataChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            databaseReference = dataManager.getDatabaseReference();
            //Initialize view
            initView();
            listenRequestDataChanged();
            listenMessageDataChanged();
        }

    }

    public void listenMessageDataChanged() {
        //Listen to the unread message changed
        FirebaseManager.getFirebaseManager().setUnreadMessageNumListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //get all unread message
                int num = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    num++;
                }
                showMessageBadge(num);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child("message").child(dataManager.getUser().getID()).child("unread").addValueEventListener(FirebaseManager.getFirebaseManager().getUnreadMessageNumListener());
    }

    public void listenRequestDataChanged() {
        //Listen to the request changed
        FirebaseManager.getFirebaseManager().setFriendRequestNumListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (dataManager.isLocalRequestChanged()) {
                    dataManager.setLocalRequestChanged(false);
                    showFriendsBadge(dataManager.getNewFriendItems().size());
                    return;
                }
                //Get all request
                requestIds = new ArrayList<String>();
                for (DataSnapshot postSnapShot : snapshot.getChildren()) {
                    requestIds.add((String) postSnapShot.getValue());
                }
                showFriendsBadge(requestIds.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child("request").child(dataManager.getUser().getID()).addValueEventListener(FirebaseManager.getFirebaseManager().getFriendRequestNumListener());
    }

    /**
     * Display the badge of message, if the number is zero, hide the badge
     *
     * @param num the number of message unread
     */
    public void showMessageBadge(int num) {
        TextView textView = messageBadge.findViewById(R.id.badge_text);
        if (num > 99) {
            textView.setText("99+");
            messageBadge.setVisibility(View.VISIBLE);
        } else if (num == 0) {
            messageBadge.setVisibility(View.INVISIBLE);
        } else {
            textView.setText(String.valueOf(num));
            messageBadge.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Display the badge of friends, if the number is zero, hide the badge
     *
     * @param num the number of friend requests
     */
    public void showFriendsBadge(int num) {
        TextView textView = friendsBadge.findViewById(R.id.badge_text);
        if (num > 99) {
            textView.setText("99+");
            friendsBadge.setVisibility(View.VISIBLE);
        } else if (num == 0) {
            friendsBadge.setVisibility(View.INVISIBLE);
        } else {
            textView.setText(String.valueOf(num));
            friendsBadge.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FROM_MESSAGE_TO_CHAT && resultCode == RESULT_CODE_FROM_CHAT_MESSAGE_CHANGED) {
            messageFragment.updateListView();
        }
        if(requestCode==REQUEST_CODE_FROM_FRIEND_TO_PROFILE && resultCode==RESULT_CODE_FROM_CHAT_MESSAGE_CHANGED){
            messageFragment.updateListView();
        }
        if(requestCode==REQUEST_CODE_FROM_FRIEND_TO_SEARCH && resultCode==RESULT_CODE_FROM_SEARCH_FRIEND_FRIEND_CHANGED){
            friendsFragment.updateListView();
        }
        if(requestCode==REQUEST_CODE_FROM_FRIEND_TO_SEARCH && resultCode==RESULT_CODE_FROM_NEW_FRIEND_REQUEST_CHANGED){
            friendsFragment.showRequestNumText(dataManager.getNewFriendItems().size());
        }
        if(requestCode==REQUEST_CODE_FROM_FRIEND_TO_PROFILE && resultCode==RESULT_CODE_FROM_FRIEND_PROFILE_FRIEND_CHANGED){
            messageFragment.updateListView();
            friendsFragment.updateListView();
        }
        if(requestCode==REQUEST_CODE_FROM_FRIEND_TO_NEW_FRIEND && resultCode==RESULT_CODE_FROM_NEW_FRIEND_REQUEST_CHANGED){
            friendsFragment.showRequestNumText(dataManager.getNewFriendItems().size());
        }
        if(requestCode==REQUEST_CODE_FROM_FRIEND_TO_NEW_FRIEND && resultCode==RESULT_CODE_FROM_NEW_FRIEND_FRIEND_CHANGED){
            friendsFragment.updateListView();
            isLocalFriendChanged=true;
        }
        if(requestCode==REQUEST_CODE_FROM_MESSAGE_TO_CHAT || requestCode==REQUEST_CODE_FROM_FRIEND_TO_PROFILE){
            if(MessageFragment.unreadChanged){
                messageFragment.updateListView();
                MessageFragment.unreadChanged=false;
            }
        }
    }
}