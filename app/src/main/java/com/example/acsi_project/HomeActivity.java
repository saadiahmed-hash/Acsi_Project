package com.example.acsi_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class HomeActivity extends AppCompatActivity {


    protected FrameLayout frameContainer ;
    protected LinearLayout addProductBtn , prodListBtn;

    protected ImageView addProdImg, prodListImg ;
    protected TextView addProdTxt , prodListTxt ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        desActiveBottom(addProdTxt , addProdImg);
        activeBottom(prodListTxt , prodListImg);
        setFragment(new ProductListFrag());
        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new AddProductFrag());
                activeBottom(addProdTxt , addProdImg);
                desActiveBottom(prodListTxt , prodListImg);
            }
        });
        prodListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new ProductListFrag());
                desActiveBottom(addProdTxt , addProdImg);
                activeBottom(prodListTxt , prodListImg);
            }
        });
    }

    private void init(){
        frameContainer = findViewById(R.id.frameContainer);
        addProductBtn = findViewById(R.id.addProductBtn);
        prodListBtn = findViewById(R.id.prodListBtn);
        addProdImg = findViewById(R.id.addProdImg);
        prodListImg = findViewById(R.id.prodListImg);
        addProdTxt = findViewById(R.id.addProdTxt);
        prodListTxt = findViewById(R.id.prodListTxt);
    }

    private void setFragment(Fragment fragment){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameContainer ,  fragment); // Replace `R.id.your_frame_layout_id` with the actual ID of your FrameLayout
        transaction.commit();
    }

    private void activeBottom(TextView txt , ImageView img){

        txt.setTypeface(null, Typeface.BOLD);
        img.setColorFilter(getColor(R.color.mainColor));
        txt.setTextColor(getColor(R.color.black));
    }

    private void desActiveBottom(TextView txt , ImageView img){
        txt.setTypeface( null, Typeface.NORMAL);
        txt.setTextColor(getColor(R.color.gray));
        img.setColorFilter(getColor(R.color.gray));

    }
}