package com.example.acsi_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VerifyEmailActivity extends AppCompatActivity {


    protected FirebaseAuth mAuth ;
    protected FirebaseUser currUser ;

    protected Button continueBtn;
    protected LottieAnimationView animEmail ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);
        init();
        animEmail.playAnimation();
        Toast.makeText(this, currUser.getEmail(), Toast.LENGTH_SHORT).show();
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currUser!=null){
                    continueBtn.setClickable(false);
                    continueBtn.setBackground(getDrawable(R.drawable.progress_btn));
                    continueBtn.setText("Please wait...");
                    currUser.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                if (currUser.isEmailVerified()){
                                    Toast.makeText(VerifyEmailActivity.this, "Welcome !  ", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(VerifyEmailActivity.this , HomeActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(VerifyEmailActivity.this, "Please check your email ! ", Toast.LENGTH_SHORT).show();
                                    continueBtn.setBackground(getDrawable(R.drawable.button_back));
                                    continueBtn.setText("Continue");
                                    continueBtn.setClickable(true);
                                }
                            } else {
                                Toast.makeText(VerifyEmailActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                continueBtn.setBackground(getDrawable(R.drawable.button_back));
                                continueBtn.setText("Continue");
                                continueBtn.setClickable(true);
                            }
                        }
                    });
                }
            }
        });

    }
    private void init(){
        mAuth = FirebaseAuth.getInstance() ;
        currUser = mAuth.getCurrentUser() ;
        continueBtn = findViewById(R.id.continueBtn);
        animEmail = findViewById(R.id.animEmail);
    }

}