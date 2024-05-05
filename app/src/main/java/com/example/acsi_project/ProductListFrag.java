package com.example.acsi_project;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Adapters.ProductAdapter;
import classes.Article;


public class ProductListFrag extends Fragment {

    protected RecyclerView prodRecycler ;
    protected  ArrayList<Article> articles ;
    protected FirebaseDatabase firebaseDatabase ;
    protected DatabaseReference prodRef ;
    protected EditText searchEdit ;
    protected  ProductAdapter adapter ;
    protected LottieAnimationView animLoading ;
    protected ConstraintLayout blackContainer ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        init(view);
        playLoading();
        fetchData();
        // rest animation
        prodRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // Reset animation when scrolling down
                if (dy > 0) {
                    adapter.resetAnimation();
                }
            }
        });

        // add EditText watcher to get what user is searching for
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filter(charSequence.toString());
            }


            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return view ;
    }

    private void fetchData() {

        prodRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                articles.clear();
                for (DataSnapshot oneSnap:
                     snapshot.getChildren()) {
                    articles.add(oneSnap.getValue(Article.class));
                }
                // associate recycler view stuff
                adapter = new ProductAdapter(articles , getContext());
                prodRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
                prodRecycler.setAdapter(adapter);
                dismissLoading();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void init(View v){
        animLoading = v.findViewById(R.id.animLoading);
        blackContainer = v.findViewById(R.id.blackContainer);
        prodRecycler = v.findViewById(R.id.prodRecycler);
        searchEdit = v.findViewById(R.id.searchEdit);
        articles = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance(getString(R.string.DB_URL));
        prodRef = firebaseDatabase.getReference().child("products");
    }


    private void filter(String query) {
        ArrayList<Article> filtredArray = new ArrayList<>();

        for (Article item : articles) {
            // Filter condition based on your requirement
            if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                filtredArray.add(item);
            }
        }
            adapter.setData(filtredArray);
            adapter.notifyDataSetChanged();
    }

    private void playLoading(){
        animLoading.setVisibility(View.VISIBLE);
        blackContainer.setVisibility(View.VISIBLE);
        animLoading.playAnimation();
    }

    private void dismissLoading(){
        animLoading.setVisibility(View.GONE);
        blackContainer.setVisibility(View.GONE);
        animLoading.cancelAnimation();
    }

}