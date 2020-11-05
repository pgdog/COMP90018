package com.example.comp90018.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comp90018.dataBean.MessageItem;
import com.example.comp90018.R;
import com.example.comp90018.utils.DataManager;
import com.example.comp90018.utils.OnRecycleItemClickListener;
import com.example.comp90018.utils.RecycleItemTouchHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter implements RecycleItemTouchHelper.ItemTouchHelperCallback {
    // The data of the list
    private List<MessageItem> messageItems = new ArrayList<MessageItem>();
    // Click event listener
    private OnRecycleItemClickListener onItemClickListener;

    public static final int VIEW_HOLEDER_TYPE_SPACE = 0;
    public static final int VIEW_HOLEDER_TYPE_NORMAL = 1;


    @Override
    public void onItemDelete(int position) {
        if (position < messageItems.size()) {
            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            MessageItem item = messageItems.get(position);
            String friendID = item.getID();
            messageItems.remove(position);
            notifyItemRemoved(position);
            FirebaseDatabase.getInstance().getReference().child("chat").child(userID).child(friendID).setValue(null);
        }
    }

    @Override
    public void onMove(int fromPosition, int toPosition) {

    }

    //The view for each item
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageAvatar;
        TextView nameText;
        TextView contentText;
        TextView timeText;
        TextView badgeText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageAvatar = (ImageView) itemView.findViewById(R.id.item_message_image);
            nameText = (TextView) itemView.findViewById(R.id.item_message_name);
            contentText = (TextView) itemView.findViewById(R.id.item_message_content);
            timeText = (TextView) itemView.findViewById(R.id.item_message_time_text);
            badgeText=(TextView)itemView.findViewById(R.id.item_message_badge_text);
        }
    }

    private class SpaceViewHolder extends RecyclerView.ViewHolder {
        Space space;

        public SpaceViewHolder(View view) {
            super(view);
            space = view.findViewById(R.id.item_space);
        }
    }

    //The method used to set a listener
    public void setOnItemClickListener(OnRecycleItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public MessageListAdapter(List<MessageItem> messageItems) {
        this.messageItems = messageItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_HOLEDER_TYPE_NORMAL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        //Bind the data
        if (position < messageItems.size()) {
            MessageItem item = messageItems.get(position);
            ViewHolder myHolder = (ViewHolder) holder;
            Picasso.get().load(item.getImage()).into(myHolder.imageAvatar);
            myHolder.nameText.setText(item.getName());
            myHolder.contentText.setText(item.getLastMessage());
            myHolder.timeText.setText(transformDate(item.getLastMessageDate()));
            myHolder.timeText.setVisibility(View.VISIBLE);
            if(item.getNumOfUnread()==0){
                myHolder.badgeText.setVisibility(View.GONE);
            }else{
                myHolder.badgeText.setText(String.valueOf(item.getNumOfUnread()));
                myHolder.badgeText.setVisibility(View.VISIBLE);
            }

            //Set click listener to each item
            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        int pos = holder.getLayoutPosition();
                        onItemClickListener.onItemClick(holder.itemView, pos);
                    }
                }
            });
        }
    }

    public String transformDate(long date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
        String dateStr = dateFormat.format(date);
        String currentDate = dateFormat.format(new Date(System.currentTimeMillis()));
        String[] dateStrs = dateStr.split("-");
        String[] currentStrs = currentDate.split("-");
        if (dateStrs[0].equals(currentStrs[0]) && dateStrs[1].equals(currentStrs[1]) && dateStrs[2].equals(currentStrs[2])) {
            return dateStrs[3] + ":" + dateStrs[4];
        } else {
            return dateStrs[2] + "/" + dateStrs[1] + "/" + dateStrs[0];
        }
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_HOLEDER_TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        return messageItems.size();
    }
}
