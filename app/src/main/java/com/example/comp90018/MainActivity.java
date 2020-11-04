package com.example.comp90018;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.RestrictionEntry;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comp90018.dataBean.User;
import com.example.comp90018.ui.MainViewActivity;
import com.example.comp90018.utils.DataManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    EditText email,password;
    Button login;

    private ProgressBar progressBar;

    public void goToRegister(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void login(View view){
        String loginEmail = email.getText().toString();
        String loginPassword = password.getText().toString();
        if(loginEmail.isEmpty() || loginPassword.isEmpty()){
            Toast.makeText(getApplicationContext(), "please enter email and password",
                    Toast.LENGTH_SHORT).show();
            return;
        }else if(loginPassword.length() < 6){
            Toast.makeText(getApplicationContext(), "password should contains at least 6 characters",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        login.setText("logging in, please wait...");
        login.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(loginEmail, loginPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i("Login result", "signInWithEmail:success");
                            final FirebaseUser firebaseUser=mAuth.getCurrentUser();
                            database=FirebaseDatabase.getInstance();
                            myRef=database.getReference().child("users").child(firebaseUser.getUid());
                            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    User newUser=new User();
                                    String id=firebaseUser.getUid();
                                    String username=(String)snapshot.child("username").getValue();
                                    String pic=(String)snapshot.child("photo").getValue();
                                    newUser.setID(id);
                                    newUser.setUserName(username);
                                    newUser.setImage(pic);
                                    DataManager.getDataManager(getApplicationContext()).setUser(newUser);
                                    DataManager.getDataManager(getApplicationContext()).setDatabase(database);
                                    DataManager.getDataManager(getApplicationContext()).setDatabaseReference(database.getReference());
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Intent intent = new Intent(getApplicationContext(), MainViewActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.i("Login result", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "login fail! Email or password is incorrect",
                                    Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            login.setText("login");
                            login.setEnabled(true);
                        }
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Cancel the title
        if (getSupportActionBar() != null)

        {
            getSupportActionBar().hide();

        }
        mAuth = FirebaseAuth.getInstance();
        email = (EditText)findViewById(R.id.loginEmail);
        password = (EditText)findViewById(R.id.loginPassword);
        login = (Button)findViewById(R.id.loginButton);
        progressBar=(ProgressBar)findViewById(R.id.login_progressBar);
        progressBar.setVisibility(View.INVISIBLE);
    }
}