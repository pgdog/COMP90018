package com.example.comp90018.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.comp90018.R;
import com.example.comp90018.utils.DataManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditNameActivity extends AppCompatActivity {
    private EditText nameEdit;
    private Button backBtn;
    private Button doneBtn;

    public static int NAME_CHANGED_RESULT=1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Cancel the title
        if (getSupportActionBar() != null)

        {
            getSupportActionBar().hide();

        }
        //Bind the layout
        setContentView(R.layout.activity_edit_name);

        //Initialize view
        initView();
    }

    public void initView(){
        nameEdit=(EditText)findViewById(R.id.edit_name_edit_text);
        backBtn=(Button)findViewById(R.id.edit_name_back_btn);
        doneBtn=(Button)findViewById(R.id.edit_name_done_btn);

        doneBtn.setEnabled(false);
        nameEdit.setText(DataManager.getDataManager(this).getUser().getUserName());

        nameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().equals("") || editable.toString().equals(DataManager.getDataManager(getApplicationContext()).getUser().getUserName())){
                    doneBtn.setEnabled(false);
                }else{
                    doneBtn.setEnabled(true);
                }
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //update the name of user
                String newName=nameEdit.getText().toString();
                DataManager dataManager=DataManager.getDataManager(getApplicationContext());
                dataManager.getDatabaseReference().child("users").child(dataManager.getUser().getID()).child("username").setValue(newName);
                dataManager.getUser().setUserName(newName);
                setResult(NAME_CHANGED_RESULT);
                finish();
            }
        });


    }
}
