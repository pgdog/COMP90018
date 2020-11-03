package com.example.comp90018.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.comp90018.R;
import com.example.comp90018.utils.DataManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SearchActivity extends AppCompatActivity {
    private Button backBtn;
    private EditText inputEditText;
    private TextView resultText;

    private DataManager dataManager;
    private DatabaseReference databaseReference;

    private boolean userFound;
    private boolean isFriend;
    private boolean isPending;

    private String searchUID;
    private String userName;
    private String userPic;

    private boolean friendChanged;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Cancel the title
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();

        }
        //Bind the layout
        setContentView(R.layout.activity_search_friend);

        dataManager = DataManager.getDataManager(this);
        databaseReference = dataManager.getDatabaseReference();
        friendChanged=false;
        //Initialize view
        initView();
    }

    public void initView() {
        backBtn = (Button) findViewById(R.id.search_friend_back_btn);
        inputEditText = (EditText) findViewById(R.id.search_friend_edit_text);
        resultText = (TextView) findViewById(R.id.search_friend_result_text);

        resultText.setVisibility(View.INVISIBLE);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(friendChanged){
                    setResult(FriendsFragment.CODE_FROM_SEARCH_FRIEND_FRIEND_CHANGED);
                }
                finish();
            }
        });
        inputEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                //get search string from text field and only start searching if it is not empty string
                if (i == EditorInfo.IME_ACTION_SEARCH && !inputEditText.getText().toString().equals("")) {
                    //Search the user
                    String email = inputEditText.getText().toString();
                    startSearch(email);
                    return true;
                }
                return false;
            }
        });
    }

    public void startSearch(final String searchString) {
        resultText.setVisibility(View.INVISIBLE);
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            //first onDataChange is for getting uid of the searched user
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userFound = false;
                for (DataSnapshot postSnapShot : snapshot.getChildren()) {
                    //get photo , username and uid of the searched user
                    if (searchString.equals((String) postSnapShot.child("email").getValue())) {
                        searchUID = (String) postSnapShot.child("uid").getValue();
                        userName = (String) postSnapShot.child("username").getValue();
                        userPic = (String) postSnapShot.child("photo").getValue();
                        userFound = true;
                        break;
                    }
                    Log.i("search result", "data is " + (String) postSnapShot.child("email").getValue());
                }
                if (userFound) {
                    //after getting uid, check if the user is already friend of the current user
                    databaseReference.child("users").child(dataManager.getUser().getID()).child("friends").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            isFriend = false;
                            for (DataSnapshot postSnapShot : snapshot.getChildren()) {
                                //check if the searched user is friend of current user
                                if (searchUID.equals((String) postSnapShot.getValue())) {
                                    isFriend = true;
                                    isPending = false;
                                }
                                Log.i("search result", "data is " + (String) postSnapShot.child("email").getValue());
                            }
                            if (isFriend) {
                                //The friend already exist
                                Intent intent = new Intent(getApplicationContext(), FriendProfileActivity.class);
                                intent.putExtra(MainViewActivity.VALUES_FRIEND_ID, searchUID);
                                startActivityForResult(intent,FriendsFragment.CODE_TO_FRIEND_PROFILE);
                            } else {
                                //after getting uid, get the pending statues of the searched user
                                databaseReference.child("request").child(searchUID).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        isPending = false;
                                        for (DataSnapshot realSnapShot : snapshot.getChildren()) {
                                            //check if user has already send friend request
                                            if (dataManager.getUser().getID().equals((String) realSnapShot.getValue())) {
                                                isPending = true;
                                                Log.i("search request result", "isPending set true ");
                                                break;
                                            }
                                        }
                                        Intent intent = new Intent(getApplicationContext(), AddFriendActivity.class);
                                        intent.putExtra(MainViewActivity.VALUES_FRIEND_ID, searchUID);
                                        intent.putExtra("Picture", userPic);
                                        intent.putExtra("Name", userName);
                                        intent.putExtra("IsPending", isPending);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                } else {
                    resultText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==FriendsFragment.CODE_TO_FRIEND_PROFILE &&resultCode==FriendsFragment.CODE_FROM_FRIEND_PROFILE_FRIEND_CHANGED){
            friendChanged=true;
        }
    }
}
