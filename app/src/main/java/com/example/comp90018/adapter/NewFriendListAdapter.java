package com.example.comp90018.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comp90018.R;
import com.example.comp90018.dataBean.MessageItem;
import com.example.comp90018.dataBean.NewFriendItem;
import com.example.comp90018.utils.OnRecycleItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class NewFriendListAdapter extends RecyclerView.Adapter{
    // The data of the list
    private List<NewFriendItem> newFriendItems=new ArrayList<NewFriendItem>();
    // Click event listener
    private OnRecycleItemClickListener onItemClickListener;

    public static final int VIEW_HOLEDER_TYPE_SPACE=0;
    public static final int VIEW_HOLEDER_TYPE_NORMAL=1;

    public NewFriendListAdapter(List<NewFriendItem> newFriendItems){
        this.newFriendItems=newFriendItems;
    }

    //The view for each item
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageAvatar;
        TextView nameText;
        TextView contentText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageAvatar=(ImageView)itemView.findViewById(R.id.item_message_image);
            nameText=(TextView)itemView.findViewById(R.id.item_message_name);
            contentText=(TextView)itemView.findViewById(R.id.item_message_content);
        }
    }

    private class SpaceViewHolder extends RecyclerView.ViewHolder{
        Space space;
        public SpaceViewHolder(View view){
            super(view);
            space=view.findViewById(R.id.item_space);
        }
    }

    //The method used to set a listener
    public void setOnItemClickListener(OnRecycleItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==VIEW_HOLEDER_TYPE_SPACE){
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_space,parent,false);
            NewFriendListAdapter.SpaceViewHolder holder=new NewFriendListAdapter.SpaceViewHolder(view);
            return holder;
        }else if(viewType==VIEW_HOLEDER_TYPE_NORMAL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
            NewFriendListAdapter.ViewHolder holder = new NewFriendListAdapter.ViewHolder(view);
            return holder;
        }else{
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        //Bind the data
        if(position<newFriendItems.size()) {
            NewFriendItem item = newFriendItems.get(position);
            NewFriendListAdapter.ViewHolder myHolder = (NewFriendListAdapter.ViewHolder) holder;
            myHolder.imageAvatar.setImageBitmap(item.getImage());
            myHolder.nameText.setText(item.getName());
            myHolder.contentText.setText(item.getContent());

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

    @Override
    public int getItemViewType(int position) {
        if(position==newFriendItems.size()){
            return VIEW_HOLEDER_TYPE_SPACE;
        }else{
            return VIEW_HOLEDER_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return newFriendItems.size()+1;
    }

    public List<NewFriendItem> getNewFriendItems(){
        return newFriendItems;
    }
}
