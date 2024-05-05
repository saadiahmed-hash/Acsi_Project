package com.example.acsi_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetActivity extends AppCompatActivity {


    protected TextInputEditText emailEdit ;
    protected FirebaseAuth mAuth ;
    protected Button restBtn ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        init();
        restBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkEmail()){
                    restBtn.setClickable(false);
                    restBtn.setBackground(getDrawable(R.drawable.progress_btn));
                    restBtn.setText("Please wait...");
                    mAuth.sendPasswordResetEmail(emailEdit.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ForgetActivity.this, "We have sent you an email , please check ! ", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(ForgetActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                restBtn.setClickable(true);
                                restBtn.setBackground(getDrawable(R.drawable.button_back));
                                restBtn.setText("Rest password");
                            }
                        }
                    });
                }
            }
        });
    }
    private boolean checkEmail() {
        return ! emailEdit.getText().toString().equals("");
    }
    private void  init(){
        restBtn = findViewById(R.id.restBtn);
        emailEdit = findViewById(R.id.emailEdit);
        mAuth = FirebaseAuth.getInstance() ;
    }
}