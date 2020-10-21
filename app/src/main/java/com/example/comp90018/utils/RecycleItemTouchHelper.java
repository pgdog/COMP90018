package com.example.comp90018.utils;

import android.graphics.Canvas;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comp90018.R;
import com.example.comp90018.adapter.MessageListAdapter;

public class RecycleItemTouchHelper extends ItemTouchHelper.Callback {
    private final ItemTouchHelperCallback helperCallback;

    public RecycleItemTouchHelper(ItemTouchHelperCallback helperCallback){
        this.helperCallback=helperCallback;
    }
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0,ItemTouchHelper.LEFT);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if(viewHolder.getItemViewType()== MessageListAdapter.VIEW_HOLEDER_TYPE_NORMAL) {
            helperCallback.onItemDelete(viewHolder.getAdapterPosition());
        }
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if(actionState==ItemTouchHelper.ACTION_STATE_SWIPE && viewHolder.getItemViewType()== MessageListAdapter.VIEW_HOLEDER_TYPE_NORMAL){
            viewHolder.itemView.setBackgroundColor(viewHolder.itemView.getResources().getColor(R.color.colorLightGrey));
        }
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if(viewHolder.getItemViewType()== MessageListAdapter.VIEW_HOLEDER_TYPE_NORMAL) {
            viewHolder.itemView.setBackgroundColor(viewHolder.itemView.getResources().getColor(R.color.colorWhite));
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if(actionState==ItemTouchHelper.ACTION_STATE_SWIPE){

        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    public interface ItemTouchHelperCallback{
        void onItemDelete(int position);
        void onMove(int fromPosition,int toPosition);;
    }
}
