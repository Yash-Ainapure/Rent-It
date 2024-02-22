package com.example.rent_it;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;

public class UploadNewProduct extends AppCompatActivity {

    NumberPicker numberPickerRooms;
    Button uploadImageBtn,uploadProductBtn;
    ImageView productImage;
    Uri imageUri;
    String imageUriString;
    EditText productName,productPrice,productDescription;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    String Name;
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // Handle the back button
            case android.R.id.home:
                onBackPressed(); // This will call the default back button behavior
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_new_product);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Upload New Product");
        }
        productImage=findViewById(R.id.imageView);
        uploadImageBtn = findViewById(R.id.btnUploadImage);
        uploadProductBtn=findViewById(R.id.btnUploadProduct);
        productName=findViewById(R.id.etProductName);
        productPrice=findViewById(R.id.etProductPrice);
        productDescription=findViewById(R.id.etProductDescription);
        //set min and max values to rooms picker
        numberPickerRooms = findViewById(R.id.numberPickerRooms);



        productName.setEnabled(false);
        productName.setFocusable(false);
        productName.setFocusableInTouchMode(false);

        numberPickerRooms.setMinValue(1);
        numberPickerRooms.setMaxValue(10);
        numberPickerRooms.setValue(1);

        numberPickerRooms.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.setBackgroundResource(R.drawable.np_number_picker_pressed);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                v.setBackgroundResource(R.drawable.np_number_picker_default);
            }
            return false;
        });

        numberPickerRooms.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                v.setBackgroundResource(R.drawable.np_number_picker_focused);
            } else {
                v.setBackgroundResource(R.drawable.np_number_picker_default);
            }
        });


        Intent intent = getIntent();
        if (intent != null) {
            imageUriString = intent.getStringExtra("imageUri");
            Name=intent.getStringExtra("productName");
            if (imageUriString != null) {
                imageUri = Uri.parse(imageUriString);
                // Load the image into the ImageView using Picasso
                Picasso.get().load(imageUri).into(productImage);
            }
            if (Name != null) {
                productName.setText(Name);
            }

        }

        uploadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UploadNewProduct.this, UploadImage.class));
            }
        });

        uploadProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=productName.getText().toString();
                String price=productPrice.getText().toString();
                String description=productDescription.getText().toString();
                String duration=numberPickerRooms.getValue()+"";
                String imageUrl=imageUriString;

                Log.d("tag","name : "+name+" \n price : "+price+" \n description : "+description+" \n duration : "+duration+" \n imageUrl : "+imageUrl);

                mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                if(user!=null){
                    String LoggedUserEmail = user.getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(LoggedUserEmail).child("myProducts");
                    String UserId=name;
                    Log.d("tag","UserEmail : "+user.getEmail());
                    ProductInfo info=new ProductInfo(name,price,description,duration,imageUrl,"unapproved","none",user.getEmail().toString());
                    databaseReference.child(UserId).setValue(info).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Handle the success case, for example, show a success message
                                    Toast.makeText(UploadNewProduct.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(UploadNewProduct.this,Home.class));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle the failure case, for example, show an error message
                                    Toast.makeText(UploadNewProduct.this, "Failed to save data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                }else{
                    Toast.makeText(UploadNewProduct.this, "Error: No user logged in", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

}