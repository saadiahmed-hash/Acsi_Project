package com.example.acsi_project;

import static com.example.acsi_project.R.layout.drop_down_item;
import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import classes.User;

public class signUp extends AppCompatActivity {
   protected LottieAnimationView animSignup ;
    protected TextInputEditText nameEdit, emailEdit, phoneEdit, passEdit;
    protected Button createBtn;
        protected FirebaseAuth mAuth = FirebaseAuth.getInstance();
    protected FirebaseDatabase firebaseDatabase ;
    protected DatabaseReference usersRef ;

    protected TextView loginText ;
    private TextInputLayout textInputLayout;
    private AutoCompleteTextView roleList;
    String[] roles  = { "Inventory Manager", "Store Manager" , "Supplier"};
    protected ArrayAdapter roleAdapter;

    String selectedRole = null ;    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
        roleAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.drop_down_item , roles);
        roleList.setAdapter(roleAdapter);
        roleList.setText("Select your role ", false);
        roleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               selectedRole = (String) adapterView.getItemAtPosition(i);

            }
        });


        animSignup.playAnimation();
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verifyEdit(nameEdit) && verifyEdit(emailEdit) && verifyPhone() && verifyPass() && testRole()) {
                    createBtn.setClickable(false);
                    createBtn.setBackground(getDrawable(R.drawable.progress_btn));
                    createBtn.setText("Please wait...");
                    createUser();
                }
            }
        });
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(signUp.this , LoginActivity.class));
                finish();
            }
        });
    }
    private void init() {
        nameEdit = findViewById(R.id.nameEdit);
        emailEdit = findViewById(R.id.emailEdit);
        phoneEdit = findViewById(R.id.phoneEdit);
        passEdit = findViewById(R.id.passEdit);
        createBtn = findViewById(R.id.createBtn);
            firebaseDatabase = FirebaseDatabase.getInstance(getString(R.string.DB_URL));
        usersRef = firebaseDatabase.getReference().child("users");
       animSignup = findViewById(R.id.animSignup);
        loginText = findViewById(R.id.loginText);
        textInputLayout = findViewById(R.id.textInputLayout);
        roleList = findViewById(R.id.roleList);
    }

    protected boolean verifyPhone() {
        if (phoneEdit.getText().toString().length() != 10) {
            phoneEdit.setError("Please entre your phone number ! ");
            return false;
        } else return true;
    }
    protected boolean testRole(){
        if (selectedRole == null){
            Toast.makeText(this, "Please select your role ! ", Toast.LENGTH_SHORT).show();
        }
        return selectedRole!=null;
    }

    protected boolean verifyEdit(TextInputEditText editText) {
        if (editText.getText().toString().equals("")) {
            editText.setError("Please entre your information ! ");
            return false;
        } else return true;
    }

    private boolean verifyPass() {
        if (passEdit.getText().toString().length() < 5) {
            passEdit.setError("password must contains at least 6 characters ! ");
            return false;
        } else return true;
    }


    private void createUser(){

        mAuth.createUserWithEmailAndPassword(emailEdit.getText().toString() , passEdit.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    sendVerificationEmail();
                }  else {
                    Toast.makeText(signUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    createBtn.setBackground(getDrawable(R.drawable.button_back));
                    createBtn.setText("Create account ");
                    createBtn.setClickable(true);
                }
            }
        });

    }

    private void sendVerificationEmail() {
        FirebaseUser currUser = mAuth.getCurrentUser();
        if (currUser != null ){
            currUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        saveUserData();
                        startActivity(new Intent(signUp.this , VerifyEmailActivity.class));
                        finish();
                    } else {
                        Toast.makeText(signUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        createBtn.setBackground(getDrawable(R.drawable.button_back));
                        createBtn.setText("Create account ");
                        createBtn.setClickable(true);
                    }
                }
            });
        }
    }

    private void saveUserData() {
        User user= new User(mAuth.getCurrentUser().getUid(), nameEdit.getText().toString().trim() , phoneEdit.getText().toString().trim(), emailEdit.getText().toString().trim() , selectedRole);
        usersRef.child(user.getUserID()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(signUp.this, "Account has been created successfully ", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(signUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    createBtn.setBackground(getDrawable(R.drawable.button_back));
                    createBtn.setText("Create account ");
                    createBtn.setClickable(true);
                }
            }
        });
    }
}