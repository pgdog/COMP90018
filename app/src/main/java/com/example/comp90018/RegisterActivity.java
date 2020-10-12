package com.example.comp90018;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText email,password,confirmPassword,address;

    public void signIn(View view){
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i("sign Up result", "createUserWithEmail:success");
                            //FirebaseUser user = mAuth.getCurrentUser();
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.i("sign Up result", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //get google firebase object reference
        mAuth = FirebaseAuth.getInstance();
        //initializing reference to the layout
        email = (EditText)findViewById(R.id.signUpEmail);
        password = (EditText)findViewById(R.id.signUpPassword);
        confirmPassword = (EditText)findViewById(R.id.confirmPassword);
        address = (EditText)findViewById(R.id.signUpAddress);
    }
}