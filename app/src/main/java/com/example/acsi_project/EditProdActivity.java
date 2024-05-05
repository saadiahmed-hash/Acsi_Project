package com.example.acsi_project;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.saadahmedev.popupdialog.PopupDialog;
import com.saadahmedev.popupdialog.listener.StandardDialogActionListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import classes.Article;

public class EditProdActivity extends AppCompatActivity {
    protected String prodID ;
    protected DatabaseReference prodRef;
    protected Article article ;
    protected LottieAnimationView animLoading ;
    protected ConstraintLayout blackContainer ;

    protected String currDate;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int RESULT_OK = -1;
    protected Uri selectedImageUri;

    protected StorageReference mStorage;
    protected Button saveProdBtn;

        protected ImageView prodImage;
    protected TextInputEditText dateEdit;

    protected TextInputEditText nameEdit, descEdit, quantityEdit, priceEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_prod);
        prodID = getIntent().getStringExtra("prodID");
        init();
        playLoading();
        prodRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                article = snapshot.getValue(Article.class);
                // filling edits
                Picasso.get().load(article.getImg())
                        .placeholder(getDrawable(R.drawable.loading))
                        .into(prodImage);
                nameEdit.setText(article.getName());
                descEdit.setText(article.getDesc());
                quantityEdit.setText(String.valueOf(article.getQuantity()));
                priceEdit.setText(String.valueOf(article.getPrice()));
                dateEdit.setText(article.getExpirationDate());
                currDate = article.getExpirationDate();
                dismissLoading();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        dateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogPickDate();

            }
        });
        prodImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
            }
        });

        saveProdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupDialog.getInstance(EditProdActivity.this)
                        .standardDialogBuilder()
                        .createIOSDialog()
                        .setHeading("Update")
                        .setDescription("Are you sure you want to save changes of  "+article.getName() + " ?")
                        .setPositiveButtonTextColor(com.saadahmedev.popupdialog.R.color.colorGreen)
                        .build(new StandardDialogActionListener() {
                            @Override
                            public void onPositiveButtonClicked(Dialog dialog) {
                                //saveChanges();
                                saveProdBtn.setClickable(false);
                                saveProdBtn.setBackground(getDrawable(R.drawable.progress_btn));
                                saveProdBtn.setText("Please wait...");
                                dialog.dismiss();
                                if (selectedImageUri == null){
                                    saveProdInDB(null);
                                } else {
                                    saveProduct();
                                }

                            }

                            @Override
                            public void onNegativeButtonClicked(Dialog dialog) {
                                finish();
                            }
                        }).show();
            }
        });

    }

    public void openDialogPickDate() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog picker = new DatePickerDialog(EditProdActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String dd , mm  ;
                if (i2<10){
                    dd = "0"+i2 ;
                } else {
                    dd = String.valueOf(i2);
                }
                if ((i1+1)<10){
                    mm = "0"+(i1+1) ;
                } else {
                    mm = String.valueOf(i2+1);
                }
                currDate = dd + "/" + mm + "/" + i;
                dateEdit.setText(currDate);
            }
        }, year, month, day);
        picker.show();
    }
    private void init(){
        animLoading = findViewById(R.id.animLoading);
        blackContainer = findViewById(R.id.blackContainer);
        prodRef = FirebaseDatabase.getInstance(getString(R.string.DB_URL)).getReference().child("products").child(prodID);
        dateEdit = findViewById(R.id.dateEdit);
        prodImage =findViewById(R.id.prodImage);
        saveProdBtn = findViewById(R.id.saveProdBtn);
        nameEdit = findViewById(R.id.nameEdit);
        descEdit = findViewById(R.id.descEdit);
        quantityEdit = findViewById(R.id.quantityEdit);
        priceEdit = findViewById(R.id.priceEdit);
        selectedImageUri = null ;

        // DB declaration
        mStorage = FirebaseStorage.getInstance().getReference();

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
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == PICK_IMAGE_REQUEST) && (resultCode == RESULT_OK) && (data != null)) {
            // Handle the selected image here
            // For example, you can get the URI of the selected image using:
            selectedImageUri = data.getData();
            prodImage.setImageURI(selectedImageUri);
            // Then you can use this URI to load the image into an ImageView or do further processing.
        }
    }



protected void saveProduct() {
        String id = article.getId();
        StorageReference imageRef = mStorage.child("productsImages/").child(id);
        imageRef.putFile(selectedImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            saveProdInDB(uri);
                        }
                    });
                } else {
                    Toast.makeText(EditProdActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    saveProdBtn.setBackground(getDrawable(R.drawable.button_back));
                    saveProdBtn.setText("Save product");
                    saveProdBtn.setClickable(true);
                }
            }
        });
    }




    private void saveProdInDB(Uri uri) {
        String prodID = article.getId() ;
        assert prodID != null;
        String url;
        if (uri==null) {
          url = article.getImg() ;
        } else {
             url = String.valueOf(uri);
        }
        prodRef.setValue(new Article(prodID, nameEdit.getText().toString(),
                        descEdit.getText().toString(),
                        url,
                        Integer.valueOf(quantityEdit.getText().toString()),
                        Double.valueOf(priceEdit.getText().toString())
                        , currDate))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditProdActivity.this, "Succ", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(EditProdActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    dismissLoading();
                    }

                });
    }







}