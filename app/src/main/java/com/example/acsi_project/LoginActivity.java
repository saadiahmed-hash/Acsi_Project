package com.example.acsi_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {


   protected TextInputEditText emailEdit , passEdit ;
    protected LottieAnimationView animLogin ;

    protected FirebaseAuth mAuth ;

    protected Button loginBtn ;

    protected TextView signUpTxt ;

    protected TextView forgetTxt ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        animLogin.playAnimation();
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verifyEmail() && verifyPass()){
                    loginBtn.setClickable(false);
                    loginBtn.setBackground(getDrawable(R.drawable.progress_btn));
                    loginBtn.setText("Please wait...");
                    mAuth.signInWithEmailAndPassword(emailEdit.getText().toString().trim() , passEdit.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                if (mAuth.getCurrentUser().isEmailVerified()){
                                    Toast.makeText(LoginActivity.this, "welcome back ! ", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this , HomeActivity.class));
                                    finish();
                                } else {
                                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(LoginActivity.this, "check your email ! ", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(LoginActivity.this , VerifyEmailActivity.class));
                                                finish();
                                            } else {
                                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                loginBtn.setBackground(getDrawable(R.drawable.button_back));
                                                loginBtn.setText("Login");
                                                loginBtn.setClickable(true);
                                            }
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                loginBtn.setBackground(getDrawable(R.drawable.button_back));
                                loginBtn.setText("Create account ");
                                loginBtn.setClickable(true);
                            }
                        }
                    });
                }
            }
        });

        signUpTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this , signUp.class));
                finish();
            }
        });

        forgetTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this , ForgetActivity.class));
            }
        });
    }

    private void  init(){
        animLogin = findViewById(R.id.animLogin);
        emailEdit = findViewById(R.id.emailEdit);
        passEdit = findViewById(R.id.passEdit);
        loginBtn = findViewById(R.id.loginBtn);
        signUpTxt = findViewById(R.id.signUpTxt);
        forgetTxt = findViewById(R.id.forgetTxt);
        mAuth = FirebaseAuth.getInstance() ;
    }
    protected boolean verifyEmail() {
        if (emailEdit.getText().toString().equals("")) {
            emailEdit.setError("Please entre your email ! ");
            return false;
        } else return true;
    }

    private boolean verifyPass() {
        if (passEdit.getText().toString().length() < 5) {
            passEdit.setError("Please enter your password ! ");
            return false;
        } else return true;
    }

}