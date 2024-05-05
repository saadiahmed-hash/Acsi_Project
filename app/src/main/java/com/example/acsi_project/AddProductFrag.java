package com.example.acsi_project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import classes.Article;


public class AddProductFrag extends Fragment {


    protected String currDate;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int RESULT_OK = -1;
    protected Uri selectedImageUri;

    protected StorageReference mStorage;
    protected Button saveProdBtn;
    protected FirebaseDatabase firebaseDatabase;
    protected DatabaseReference prodRef;
    protected ImageView prodImage;
    protected TextInputEditText dateEdit;

    protected TextInputEditText nameEdit, descEdit, quantityEdit, priceEdit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);
        init(view);
        currDate = "";

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

                if (verifyEdit(nameEdit) && verifyEdit(descEdit) && verifyEdit(quantityEdit)&&
                        verifyEdit(priceEdit) && (!currDate.equals(""))){
                    saveProdBtn.setClickable(false);
                    saveProdBtn.setBackground(getContext().getDrawable(R.drawable.progress_btn));
                    saveProdBtn.setText("Please wait...");
                    saveProduct();

                }

            }
        });


        return view;
    }

    private void init(View view) {
        dateEdit = view.findViewById(R.id.dateEdit);
        prodImage = view.findViewById(R.id.prodImage);
        saveProdBtn = view.findViewById(R.id.saveProdBtn);
        nameEdit = view.findViewById(R.id.nameEdit);
        descEdit = view.findViewById(R.id.descEdit);
        quantityEdit = view.findViewById(R.id.quantityEdit);
        priceEdit = view.findViewById(R.id.priceEdit);

        // DB declaration
        mStorage = FirebaseStorage.getInstance().getReference();
        firebaseDatabase = FirebaseDatabase.getInstance(getString(R.string.DB_URL));
        prodRef = firebaseDatabase.getReference().child("products");


    }

    public void openDialogPickDate() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog picker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
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
        String id = prodRef.push().getKey();
        StorageReference imageRef = mStorage.child("productsImages/").child(id);
        imageRef.putFile(selectedImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            saveProdInDB(uri , id);
                        }
                    });
                } else {
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    saveProdBtn.setBackground(getContext().getDrawable(R.drawable.button_back));
                    saveProdBtn.setText("Save product");
                    saveProdBtn.setClickable(true);
                }
            }
        });
    }

    private void saveProdInDB(Uri uri , String id) {
        String prodID = id ;
        assert prodID != null;

            prodRef.child(prodID).setValue(new Article(prodID, nameEdit.getText().toString(),
                            descEdit.getText().toString(),
                            String.valueOf(uri),
                            Integer.valueOf(quantityEdit.getText().toString()),
                            Double.valueOf(priceEdit.getText().toString())
                            , currDate))
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Succ", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            currDate ="";
                            clearEdit(descEdit);
                            clearEdit(nameEdit);
                            clearEdit(priceEdit);
                            clearEdit(quantityEdit);
                            clearEdit(dateEdit);
                            saveProdBtn.setBackground(getContext().getDrawable(R.drawable.button_back));
                            saveProdBtn.setText("Save product");
                            saveProdBtn.setClickable(true);
                        }
                    });
        }

        private void clearEdit(TextInputEditText edit){
        edit.setText("");
        edit.clearFocus();
        }
        private boolean verifyEdit(TextInputEditText edit){
        if (edit.getText().toString().isEmpty()){
            edit.setError("Please enter all informations ! ");
            return false ;
        } else return true ;
        }
    }
