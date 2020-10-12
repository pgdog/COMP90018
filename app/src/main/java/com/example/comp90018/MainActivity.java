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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText email,password;
    Button login;

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
        login.setText("logging in, please wait..");
        mAuth.signInWithEmailAndPassword(loginEmail, loginPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i("Login result", "signInWithEmail:success");
                            Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.i("Login result", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "login fail! Email or password is incorrect",
                                    Toast.LENGTH_SHORT).show();
                            login.setText("login");
                        }
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        email = (EditText)findViewById(R.id.loginEmail);
        password = (EditText)findViewById(R.id.loginPassword);
        login = (Button)findViewById(R.id.loginButton);
    }
}