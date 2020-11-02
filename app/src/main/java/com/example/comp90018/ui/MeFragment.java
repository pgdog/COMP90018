package com.example.comp90018.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.renderscript.Sampler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comp90018.MainActivity;
import com.example.comp90018.R;
import com.example.comp90018.adapter.MeListAdapter;
import com.example.comp90018.adapter.MessageListAdapter;
import com.example.comp90018.dataBean.MeItem;
import com.example.comp90018.utils.DataManager;
import com.example.comp90018.utils.OnRecycleItemClickListener;
import com.example.comp90018.utils.RecycleItemTouchHelper;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

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
    private Button editBtn;

    //List of the item
    private List<MeItem> meItems;

    FirebaseUser user;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;
    private String photoLink;
    public static final int PICK_IMAGE_REQUEST = 1;
    public static final int PERSONAL_INFO_REQUEST = 2;
    private Uri uploadUri;

    public MeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

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
        editBtn=(Button)view.findViewById(R.id.me_edit_btn);

        Picasso.get().load(DataManager.getDataManager(getActivity()).getUser().getImage()).into(imageView);
        nameText.setText(DataManager.getDataManager(getActivity()).getUser().getUserName());
        recyclerView=(RecyclerView)view.findViewById(R.id.me_recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        MeListAdapter meListAdapter=new MeListAdapter(meItems);
        recyclerView.setAdapter(meListAdapter);
        meListAdapter.setOnItemClickListener(new OnRecycleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(position == 1){
                    mAuth.signOut();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }else if(position == 0){

                }else if(position ==2){
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent,PICK_IMAGE_REQUEST);
                }
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity().getApplicationContext(),PersonalInfoActivity.class);
                startActivityForResult(intent,PERSONAL_INFO_REQUEST);
            }
        });
    }

    public void testData(){
        meItems.add(new MeItem(R.drawable.ic_setting,"Settings",MeItem.ITEM_TYPE_SETTING));
        meItems.add(new MeItem(R.drawable.ic_setting,"Logout",MeItem.ITEM_TYPE_LOGOUT));
        meItems.add(new MeItem(R.drawable.ic_setting,"Change Photo",MeItem.ITEM_TYPE_CHANGEPHOTO));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null
                        && data.getData() != null){
            uploadUri = data.getData();
            mStorageRef = FirebaseStorage.getInstance().getReference("users").child(mAuth.getCurrentUser().getUid()+".png");
            mStorageRef.putFile(uploadUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return mStorageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        mDatabaseRef.child("photo").setValue(downloadUri.toString());
                    }
                }
            });
        }
        if(requestCode==PERSONAL_INFO_REQUEST && resultCode==PersonalInfoActivity.PERSONAL_INFO_CHANGED_RESULT){
            initView();
        }
    }
}