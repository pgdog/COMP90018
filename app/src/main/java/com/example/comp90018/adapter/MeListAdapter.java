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
import com.example.comp90018.dataBean.MeItem;
import com.example.comp90018.dataBean.MessageItem;
import com.example.comp90018.utils.OnRecycleItemClickListener;
import com.example.comp90018.utils.RecycleItemTouchHelper;

import java.util.ArrayList;
import java.util.List;

public class MeListAdapter extends RecyclerView.Adapter {
    // The data of the list
    private List<MeItem> meItems=new ArrayList<MeItem>();
    // Click event listener
    private OnRecycleItemClickListener onItemClickListener;

    public static final int VIEW_HOLEDER_TYPE_SPACE=0;
    public static final int VIEW_HOLEDER_TYPE_NORMAL=1;

    //The view for each item
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageAvatar;
        TextView nameText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageAvatar=(ImageView)itemView.findViewById(R.id.item_me_image);
            nameText=(TextView)itemView.findViewById(R.id.item_me_name_text);
        }
    }

//    private class SpaceViewHolder extends RecyclerView.ViewHolder{
//        Space space;
//        public SpaceViewHolder(View view){
//            super(view);
//            space=view.findViewById(R.id.item_space);
//        }
//    }
    //The method used to set a listener
    public void setOnItemClickListener(OnRecycleItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }

    public MeListAdapter(List<MeItem> meItems){
        this.meItems=meItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_me, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        //Bind the data
        if(position<meItems.size()) {
            MeItem item = meItems.get(position);
            ViewHolder myHolder = (ViewHolder) holder;
            myHolder.imageAvatar.setImageResource(item.getIcImage());
            myHolder.nameText.setText(item.getItemName());

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

//    @Override
//    public int getItemViewType(int position) {
//        if(position==meItems.size()){
//            return VIEW_HOLEDER_TYPE_SPACE;
//        }else{
//            return VIEW_HOLEDER_TYPE_NORMAL;
//        }
//    }

    @Override
    public int getItemCount() {
        return meItems.size();
    }
}
