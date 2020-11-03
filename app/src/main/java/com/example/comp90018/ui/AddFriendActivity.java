//package com.example.comp90018.ui;
//
//import android.os.Bundle;
//import android.os.PersistableBundle;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.inputmethod.EditorInfo;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.comp90018.R;
//
//public class AddFriendActivity extends AppCompatActivity {
//    private Button backBtn;
//    private EditText inputText;
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
//        super.onCreate(savedInstanceState, persistentState);
//        //Cancel the title
//        if (getSupportActionBar() != null)
//
//        {
//            getSupportActionBar().hide();
//
//        }
//        //Bind the layout
//        setContentView(R.layout.activity_search_friend);
//
//        //Initialize view
//        initView();
//    }
//
//    public void initView(){
//        backBtn=(Button)findViewById(R.id.add_friend_back_btn);
//        inputText=(EditText)findViewById(R.id.add_friend_edit_text);
//
//        backBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                if(i== EditorInfo.IME_ACTION_SEARCH && !inputText.getText().toString().equals("")){
//                    //Search the user
//                    String email=inputText.getText().toString();
//
//                    return true;
//                }
//                return false;
//            }
//        });
//    }
//}
