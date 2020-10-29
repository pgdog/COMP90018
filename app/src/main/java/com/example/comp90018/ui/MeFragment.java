package com.example.comp90018.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.comp90018.R;
import com.example.comp90018.adapter.MeListAdapter;
import com.example.comp90018.adapter.MessageListAdapter;
import com.example.comp90018.dataBean.MeItem;
import com.example.comp90018.utils.OnRecycleItemClickListener;
import com.example.comp90018.utils.RecycleItemTouchHelper;

import java.util.ArrayList;
import java.util.List;

public class MeFragment extends Fragment {
    //The view of this fragment
    private View view;

    //Views
    private TextView titleText;
    private ImageView imageView;
    private TextView nameText;
    private RecyclerView recyclerView;

    //List of the item
    private List<MeItem> meItems;
    public MeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initialize data
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_me, container, false);

        //Initialize view
        initView();
        return view;
    }

    public void initData(){
        //Get all data here
        meItems=new ArrayList<MeItem>();
        testData();
    }

    public void initView(){
        //Create views here
        titleText=(TextView)view.findViewById(R.id.me_title_text);
        imageView=(ImageView)view.findViewById(R.id.me_image);
        nameText=(TextView)view.findViewById(R.id.me_name);

        Bitmap testPic= BitmapFactory.decodeResource(this.getContext().getResources(),R.drawable.test_image);
        imageView.setImageBitmap(testPic);
        recyclerView=(RecyclerView)view.findViewById(R.id.me_recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        MeListAdapter meListAdapter=new MeListAdapter(meItems);
        recyclerView.setAdapter(meListAdapter);
        meListAdapter.setOnItemClickListener(new OnRecycleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
    }

    public void testData(){
        meItems.add(new MeItem(R.drawable.ic_setting,"Settings",MeItem.ITEM_TYPE_SETTING));
    }
}