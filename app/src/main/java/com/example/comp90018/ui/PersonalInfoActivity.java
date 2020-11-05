package com.example.comp90018.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.comp90018.R;
import com.example.comp90018.utils.DataManager;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class PersonalInfoActivity extends AppCompatActivity {
    private Button backBtn;
    private View imageEditView;
    private View nameEditView;
    private ImageView imageView;
    private TextView nameText;

    private boolean isDataChanged;

    public static int PERSONAL_INFO_CHANGED_RESULT=1;

    public static int NAME_EDIT_REQUEST=1;

    private Uri uploadUri;
    private StorageReference mStorageRef;
    private DataManager dataManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Cancel the title
        if (getSupportActionBar() != null)

        {
            getSupportActionBar().hide();

        }

        dataManager=DataManager.getDataManager(getApplicationContext());
        //Bind the layout
        setContentView(R.layout.activity_edit_info);

        isDataChanged=false;

        initView();
    }


    public void initView(){
        backBtn=(Button)findViewById(R.id.personal_info_back_btn);
        imageEditView=(View)findViewById(R.id.personal_infor_pic_editView);
        nameEditView=(View)findViewById(R.id.personal_infor_name_editView);
        imageView=(ImageView) findViewById(R.id.personal_info_image);
        nameText=(TextView)findViewById(R.id.personal_info_name_text);

        Picasso.get().load(DataManager.getDataManager(this).getUser().getImage()).into(imageView);
        nameText.setText(DataManager.getDataManager(this).getUser().getUserName());
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isDataChanged){
                    setResult(PERSONAL_INFO_CHANGED_RESULT);
                }
                finish();
            }
        });

        imageEditView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,MeFragment.PICK_IMAGE_REQUEST);
            }
        });
        imageEditView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        view.setBackgroundColor(getColor(R.color.colorLightGrey));
                        break;
                    case MotionEvent.ACTION_UP:
                        view.setBackgroundColor(getColor(R.color.colorWhite));
                        break;
                }
                return false;
            }
        });

        nameEditView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),EditNameActivity.class);
                startActivityForResult(intent,NAME_EDIT_REQUEST);
            }
        });
        nameEditView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        view.setBackgroundColor(getColor(R.color.colorLightGrey));
                        break;
                    case MotionEvent.ACTION_UP:
                        view.setBackgroundColor(getColor(R.color.colorWhite));
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==NAME_EDIT_REQUEST && requestCode== EditNameActivity.NAME_CHANGED_RESULT){
            isDataChanged=true;
            initView();
        }
        if(requestCode == MeFragment.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null
                && data.getData() != null){

            uploadUri = data.getData();
            mStorageRef = FirebaseStorage.getInstance().getReference("users").child(dataManager.getUser().getID()+".png");
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
                        dataManager.getDatabaseReference().child("users").child(dataManager.getUser().getID()).child("photo").setValue(downloadUri.toString());
                        dataManager.getUser().setImage(downloadUri.toString());
                        isDataChanged=true;
                        initView();
                    }
                }
            });
        }
    }
}
