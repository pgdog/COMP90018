package com.example.comp90018;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.comp90018.ui.MainViewActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    EditText email,password,confirmPassword,username;
    private Uri defaultphoto;
    FirebaseDatabase database;
    DatabaseReference myRef;

    public void signUp(View view){
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i("sign Up result", "createUserWithEmail:success");
                            Toast.makeText(getApplicationContext(), "Sign up Success",
                                    Toast.LENGTH_SHORT).show();
                            //storing user information into firebase database
                            myRef.child("users").child(mAuth.getCurrentUser().getUid()).
                                    child("username").setValue(username.getText().toString());
                            myRef.child("users").child(mAuth.getCurrentUser().getUid()).
                                    child("email").setValue(mAuth.getCurrentUser().getEmail());
                            myRef.child("users").child(mAuth.getCurrentUser().getUid()).
                                    child("uid").setValue(mAuth.getCurrentUser().getUid());
                            mStorageRef.putFile(defaultphoto).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                                        myRef.child("users").child(mAuth.getCurrentUser().getUid()).
                                                child("photo").setValue(downloadUri.toString());
                                        //launching home activity

                                    }
                                }
                            });
                            Intent intent = new Intent(getApplicationContext(), MainViewActivity.class);
                            startActivity(intent);
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
        mStorageRef = FirebaseStorage.getInstance().getReference("users").child("test.png");
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        //initializing reference to the layout
        email = (EditText)findViewById(R.id.signUpEmail);
        password = (EditText)findViewById(R.id.signUpPassword);
        confirmPassword = (EditText)findViewById(R.id.confirmPassword);
        username = (EditText)findViewById(R.id.signUpUserName);
        defaultphoto = Uri.parse("android.resource://"+getPackageName()+"/"+ R.drawable.pusheen);

        ///drawable/test_image.png
    }
}