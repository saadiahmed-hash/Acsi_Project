package com.example.acsi_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    protected FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2800);
                    if (mAuth.getCurrentUser()!=null){
                        if(mAuth.getCurrentUser().isEmailVerified()){
                            Intent i = new Intent(MainActivity.this , HomeActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            mAuth.getCurrentUser().sendEmailVerification();
                            Intent i = new Intent(MainActivity.this , VerifyEmailActivity.class);
                            startActivity(i);
                            finish();
                        }

                    } else {
                        Intent i = new Intent(MainActivity.this , signUp.class);
                        startActivity(i);
                        finish();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
     thread.start();
    }
}