package com.example.comp90018.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.comp90018.R;
import com.example.comp90018.dataBean.FriendRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FriendRequestAdapter extends ArrayAdapter<FriendRequest>{
    private Context context;
    private List<FriendRequest> friendRequests = new ArrayList<FriendRequest>();
    private DatabaseReference mDatabaseRef;

    static class ViewHolder{
        ImageView imageView;
        TextView username;
        Button acceptButton;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //the below code is just setting up the view for the listview
        //only interesting part is the onclick listener
        ViewHolder mViewHolder = new ViewHolder();
        if(convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.request_user, parent, false);
            mViewHolder.imageView = (ImageView) convertView.findViewById(R.id.requestUserPhoto);
            mViewHolder.username = (TextView) convertView.findViewById(R.id.requestUserNameField);
            mViewHolder.acceptButton = (Button) convertView.findViewById(R.id.acceptButton);
            convertView.setTag(mViewHolder);
        }else{
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        Picasso.get().load(friendRequests.get(position).getPhotoUrl()).into(mViewHolder.imageView);
        mViewHolder.username.setText(friendRequests.get(position).getRequestUserName());

        mViewHolder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("testing list view", "value is "+ friendRequests.get(position).getRequestUserId());
                //add current user's uid from target user's friend list
                //add target user's uid from current user's friend list
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                mDatabaseRef.child("users").child(friendRequests.get(position).getRequestUserId())
                        .child("friends").push().setValue(friendRequests.get(position).getCurrentUserId());
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                mDatabaseRef.child("users").child(friendRequests.get(position).getCurrentUserId())
                        .child("friends").push().setValue(friendRequests.get(position).getRequestUserId());
                //delete friend request of current user from target user's request
                mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("request").child(friendRequests.get(position).getRequestUserId());
                mDatabaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot realSnapShot : snapshot.getChildren()) {
                            //check if user has already send friend request
                            if(friendRequests.isEmpty()){
                                break;
                            }
                            if (friendRequests.get(position).getCurrentUserId().equals((String) realSnapShot.getValue())) {
                                realSnapShot.getRef().setValue(null);
                                break;
                            }
                        }
                        if(!friendRequests.isEmpty()) {
                            //after the previous process,delete friend request of target user from current user's request
                            mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("request").child(friendRequests.get(position).getCurrentUserId());
                            mDatabaseRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot realSnapShot : snapshot.getChildren()) {
                                        //check if user has already send friend request
                                        if (friendRequests.isEmpty()) {
                                            break;
                                        }
                                        if (friendRequests.get(position).getRequestUserId().equals((String) realSnapShot.getValue())) {
                                            realSnapShot.getRef().setValue(null);
                                            break;
                                        }
                                    }
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
            }
        });
        return convertView;
    }

    public FriendRequestAdapter(Context context, List<FriendRequest> friendRequests){
        super(context, R.layout.request_user,friendRequests);
        this.context = context;
        this.friendRequests = friendRequests;
    }
}
