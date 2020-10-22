package com.example.comp90018.adapter;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comp90018.dataBean.ChatItem;
import com.example.comp90018.dataBean.MessageItem;
import com.example.comp90018.R;
import com.example.comp90018.utils.RecycleItemTouchHelper;

import java.util.ArrayList;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter {
    // The data of the list
    private List<ChatItem> chatItems=new ArrayList<ChatItem>();

    public static final int MARGIN_TEXT_TO_PARENT=180;
    public static final int MARGIN_TEXT_TO_IMAGE=25;

    public static final int VIEW_HOLEDER_TYPE_SPACE=0;
    public static final int VIEW_HOLEDER_TYPE_NORMAL=1;

    //The view for each item
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageAvatar;
        TextView chatText;
        RelativeLayout chatLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageAvatar=(ImageView)itemView.findViewById(R.id.item_chat_image);
            chatText=(TextView)itemView.findViewById(R.id.item_chat_text);
            chatLayout=(RelativeLayout)itemView.findViewById(R.id.item_chat_text_layout);
        }
    }

    private class SpaceViewHolder extends RecyclerView.ViewHolder{
        Space space;
        public SpaceViewHolder(View view){
            super(view);
            space=view.findViewById(R.id.item_space);
        }
    }

    public ChatListAdapter(List<ChatItem> chatItems){
        this.chatItems=chatItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==VIEW_HOLEDER_TYPE_NORMAL){
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat,parent,false);
            ViewHolder holder=new ViewHolder(view);
            return holder;
        }else if(viewType==VIEW_HOLEDER_TYPE_SPACE){
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_space,parent,false);
            SpaceViewHolder holder=new SpaceViewHolder(view);
            return holder;
        }else{
            return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        //Bind the data
        if(position<chatItems.size()){
            ChatItem item=chatItems.get(position);
            ViewHolder myHolder=(ViewHolder)holder;
            myHolder.imageAvatar.setImageBitmap(item.getImage());
            myHolder.chatText.setText(item.getText());
            if(item.isSelf()){
                //Change the picture of the TextView
                myHolder.chatText.setBackground(myHolder.chatText.getContext().getDrawable(R.drawable.chat_text_background_blue));
                ConstraintLayout parentLayout=(ConstraintLayout) myHolder.imageAvatar.getParent();
                ConstraintSet constraintSet=new ConstraintSet();
                constraintSet.clone(parentLayout);
                constraintSet.clear(R.id.item_chat_image,ConstraintSet.START);
                constraintSet.clear(R.id.item_chat_text_layout,ConstraintSet.START);
                constraintSet.clear(R.id.item_chat_text_layout,ConstraintSet.END);

                constraintSet.connect(R.id.item_chat_image,ConstraintSet.END,ConstraintSet.PARENT_ID,ConstraintSet.END,MARGIN_TEXT_TO_IMAGE);

                constraintSet.connect(R.id.item_chat_text_layout,ConstraintSet.START,ConstraintSet.PARENT_ID,constraintSet.START,MARGIN_TEXT_TO_PARENT);
                constraintSet.connect(R.id.item_chat_text_layout,ConstraintSet.END,R.id.item_chat_image,constraintSet.START,MARGIN_TEXT_TO_IMAGE);
                myHolder.chatLayout.setGravity(Gravity.END);
                constraintSet.applyTo(parentLayout);
            }else{
                ConstraintLayout parentLayout=(ConstraintLayout) myHolder.imageAvatar.getParent();
                ConstraintSet constraintSet=new ConstraintSet();
                constraintSet.clone(parentLayout);

                constraintSet.setMargin(R.id.item_chat_image,ConstraintSet.START,MARGIN_TEXT_TO_IMAGE);
                constraintSet.setMargin(R.id.item_chat_text_layout,ConstraintSet.START,MARGIN_TEXT_TO_IMAGE);
                constraintSet.setMargin(R.id.item_chat_text_layout,ConstraintSet.END,MARGIN_TEXT_TO_PARENT);
                constraintSet.applyTo(parentLayout);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position==chatItems.size()){
            return VIEW_HOLEDER_TYPE_SPACE;
        }else{
            return VIEW_HOLEDER_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return chatItems.size()+1;
    }
}
