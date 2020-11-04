package com.example.comp90018.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comp90018.R;
import com.example.comp90018.adapter.ChatListAdapter;
import com.example.comp90018.dataBean.ChatItem;
import com.example.comp90018.dataBean.MessageItem;
import com.example.comp90018.utils.DataManager;
import com.example.comp90018.utils.TestData;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {
    //Views
    private TextView titleText;
    private Button backBtn;
    private RecyclerView recyclerView;
    private EditText inputText;
    private Button audioBtn;
    private Button addBtn;
    private Button sendBtn;
    private String friendId, userId;
    DatabaseReference mDatabaseRef;

    ChatListAdapter chatListAdapter;
    private SpeechRecognizer mSpeechRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Cancel the title
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();

        }
        //Bind the layout
        setContentView(R.layout.activity_chat);

        //Initialize data
        initData();
        //Initialize view
        initView();
    }

    public void initData() {
        //Get all data here
        friendId = getIntent().getStringExtra(MainViewActivity.VALUES_FRIEND_ID);
        userId = DataManager.getDataManager(getApplicationContext()).getUser().getID();
        //DataManager.getDataManager(this).createItemsForChat(friendID);
        //DataManager.getDataManager(this).setMessageRead(friendID);
        mDatabaseRef = DataManager.getDataManager(getApplicationContext()).getDatabaseReference();
        /*Log.i("test profile", "friend uid is "+friendId);
        Log.i("test profile", "user uid is "+ userId);
        Log.i("test profile", "friend photo is "+friendPhoto);
        Log.i("test profile", "user photo is "+ userPhoto);*/
    }

    public void initView() {
        //Create view here
        titleText = (TextView) findViewById(R.id.chat_title);
        backBtn = (Button) findViewById(R.id.chat_back_btn);
        recyclerView = (RecyclerView) findViewById(R.id.chat_recycler);
        inputText = (EditText) findViewById(R.id.chat_edit_text);
        audioBtn = (Button) findViewById(R.id.chat_audio_btn);
        addBtn = (Button) findViewById(R.id.chat_add_btn);
        sendBtn = (Button) findViewById(R.id.chat_send_btn);

        chatListAdapter = new ChatListAdapter(DataManager.getDataManager(this).getChatItems());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(chatListAdapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(chatListAdapter.getItemCount() - 1);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        inputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                if (text.length() == 0) {
                    sendBtn.setEnabled(false);
                } else {
                    sendBtn.setEnabled(true);
                }
            }
        });

        //hide the keyboard if touch the screen outside the keyboard
        inputText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    InputMethodManager manager = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
                    if (manager != null)
                        manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });

        audioBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Toast.makeText(getApplicationContext(), "Hold to speak", Toast.LENGTH_SHORT).show();
                        doSpeechRecognition();
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        mSpeechRecognizer.destroy();
                        break;
                }
                return true;
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = inputText.getText().toString();
                addToSender(text);
                addToReceiver(text);
                addDataToList(text, true);
                Log.i("test date", "date is " + new Date(System.currentTimeMillis()));
                updateView();
            }
        });
    }

    /**
     * Add a new data to chat list
     *
     * @param text
     * @param isSelf whether the text is from the user
     */
    public void addDataToList(String text, boolean isSelf) {
        ChatItem item = new ChatItem();
        item.setText(text);
        item.setSelf(isSelf);
        item.setDate(new Date(System.currentTimeMillis()));
        if (isSelf) {
            //The text is from the user, add his picture
            item.setImage(TestData.getTestData(this).testPic);
        } else {
            //The text is from the friend, add the friend's picture
            item.setImage(TestData.getTestData(this).testPic);
        }

        chatListAdapter.addItem(item);
    }

    /**
     * update the view
     */
    public void updateView() {
        //hide the keyboard and clear the EditText
        inputText.setText("");
        InputMethodManager manager = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
        if (manager != null)
            manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        //update the RecyclerView
        chatListAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(chatListAdapter.getItemCount() - 1);
    }

    public void addToSender(String text) {
        String key = mDatabaseRef.child("message").child(userId).child(friendId).push().getKey();
        mDatabaseRef.child("message").child(userId).child(friendId).child(key).child("text").setValue(text);
        mDatabaseRef.child("message").child(userId).child(friendId).child(key).child("uid").setValue(userId);
        mDatabaseRef.child("message").child(userId).child(friendId).child(key).child("date").setValue(new Date(System.currentTimeMillis()).toString());
        mDatabaseRef.child("message").child(userId).child(friendId).child(key).child("hasRead").setValue("0");
    }

    public void addToReceiver(String text) {
        String key = mDatabaseRef.child("message").child(friendId).child(userId).push().getKey();
        mDatabaseRef.child("message").child(friendId).child(userId).child(key).child("text").setValue(text);
        mDatabaseRef.child("message").child(friendId).child(userId).child(key).child("uid").setValue(userId);
        mDatabaseRef.child("message").child(friendId).child(userId).child(key).child("date").setValue(new Date(System.currentTimeMillis()).toString());
        mDatabaseRef.child("message").child(friendId).child(userId).child(key).child("hasRead").setValue("0");
    }

    public void doSpeechRecognition() {
        this.mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        this.mSpeechRecognizer.setRecognitionListener(new mRecognitionListener());
        Intent recognitionIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognitionIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        recognitionIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH.toString());
        this.mSpeechRecognizer.startListening(recognitionIntent);
    }

    private class mRecognitionListener implements RecognitionListener {
        @Override
        public void onReadyForSpeech(Bundle params) {

        }

        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onRmsChanged(float var) {

        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onError(int error) {
            switch (error) {
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    Toast.makeText(getApplicationContext(), "Network Timeout", Toast.LENGTH_SHORT).show();
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
                    break;
                case SpeechRecognizer.ERROR_AUDIO:
                    Toast.makeText(getApplicationContext(), "Audio Error", Toast.LENGTH_SHORT).show();
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_SHORT).show();
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    Toast.makeText(getApplicationContext(), "Didn't hear anything", Toast.LENGTH_SHORT).show();
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    Toast.makeText(getApplicationContext(), "No Match", Toast.LENGTH_SHORT).show();
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    Toast.makeText(getApplicationContext(), "Recognition Service is on, please wait a sec.", Toast.LENGTH_SHORT).show();
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    Toast.makeText(getApplicationContext(), "No Permission", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onResults(Bundle results) {
            Log.i("result", "onResults");
            ArrayList<String> partialResults = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            if (partialResults != null && partialResults.size() > 0) {
                String bestResult = partialResults.get(0);
                String origin = inputText.getText().toString();
                inputText.setText(String.format("%s%s.", origin, bestResult));
            }
        }

        @Override
        public void onPartialResults(Bundle bundle) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    }

}