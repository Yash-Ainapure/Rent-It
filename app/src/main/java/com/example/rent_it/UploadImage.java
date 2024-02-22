package com.example.rent_it;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadImage extends AppCompatActivity {

    Button b1,b2;
    ImageView iv;
    Uri imageuri;
    ProgressDialog pd;
    StorageReference storageReference;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    EditText name;
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
        setContentView(R.layout.activity_upload_image);
        b1=(Button) findViewById(R.id.select);
        b2=(Button) findViewById(R.id.upload);
        iv=(ImageView) findViewById(R.id.imageView3);
        name=findViewById(R.id.editTextText2);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Upload Product Image");
        }
        mAuth = FirebaseAuth.getInstance();
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });
    }
    private void uploadImage() {
        pd=new ProgressDialog(this);
        pd.setTitle("Uploading File....");
        pd.show();

        String productName=name.getText().toString();
        if (productName.isEmpty()){
            Toast.makeText(this, "Please enter product name ", Toast.LENGTH_SHORT).show();
            if(pd.isShowing()){
                pd.dismiss();
            }
            return;
        }
        if(imageuri==null){
            Toast.makeText(this, "please select a image", Toast.LENGTH_SHORT).show();
            if(pd.isShowing()){
                pd.dismiss();
            }
            return;
        }
        FirebaseUser user = mAuth.getCurrentUser();
        String UserId=user.getUid();

        storageReference= FirebaseStorage.getInstance().getReference("products/"+UserId+"/"+name.getText().toString());
        storageReference.putFile(imageuri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        iv.setImageURI(null);
                        Toast.makeText(UploadImage.this, "successfully uploaded", Toast.LENGTH_SHORT).show();
                        if(pd.isShowing()){
                            pd.dismiss();
                        }

                        // Get the download URL of the uploaded image
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadUri) {
                                // Pass the image URI to the next activity
                                Intent intent = new Intent(UploadImage.this, UploadNewProduct.class);
                                intent.putExtra("imageUri", downloadUri.toString());
                                intent.putExtra("productName",name.getText().toString());
                                startActivity(intent);
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UploadImage.this, "upload Failed", Toast.LENGTH_SHORT).show();
                        if(pd.isShowing()){
                            pd.dismiss();
                        }
                    }
                });
    }
    private void selectImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==100 && data!=null && data.getData() !=null){
            imageuri=data.getData();
            iv.setImageURI(imageuri);
        }
    }
}