package com.example.comp90018.adapter;

import android.util.Log;
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
import com.example.comp90018.utils.OnRecycleItemClickListener;
import com.example.comp90018.utils.RecycleItemTouchHelper;

import java.util.ArrayList;
import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter implements RecycleItemTouchHelper.ItemTouchHelperCallback {
    // The data of the list
    private List<MessageItem> messageItems=new ArrayList<MessageItem>();
    // Click event listener
    private OnRecycleItemClickListener onItemClickListener;

    public static final int VIEW_HOLEDER_TYPE_SPACE=0;
    public static final int VIEW_HOLEDER_TYPE_NORMAL=1;

    @Override
    public void onItemDelete(int position) {
        if(position<messageItems.size()) {
            messageItems.remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public void onMove(int fromPosition, int toPosition) {

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

    public MessageListAdapter(List<MessageItem> messageItems){
        this.messageItems=messageItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==VIEW_HOLEDER_TYPE_SPACE){
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_space,parent,false);
            SpaceViewHolder holder=new SpaceViewHolder(view);
            return holder;
        }else if(viewType==VIEW_HOLEDER_TYPE_NORMAL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }else{
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        //Bind the data
        if(position<messageItems.size()) {
            MessageItem item = messageItems.get(position);
            ViewHolder myHolder = (ViewHolder) holder;
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
        if(position==messageItems.size()){
            return VIEW_HOLEDER_TYPE_SPACE;
        }else{
            return VIEW_HOLEDER_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return messageItems.size()+1;
    }
}
