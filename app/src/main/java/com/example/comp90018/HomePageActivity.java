package com.example.comp90018;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class HomePageActivity extends AppCompatActivity {
    TextView username;
    private FirebaseAuth mAuth;
    FirebaseUser user;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //initiate action bar
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        //set onclick logic of items in action bar
        switch(item.getItemId()){
            case R.id.logout:
                Log.i("item selected","logout");
                mAuth.signOut();
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        username = (TextView)findViewById(R.id.userName);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if(user == null){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            username.setText(user.getEmail());
        }
    }
}