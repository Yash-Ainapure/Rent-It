package com.example.rent_it;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class PurchasedProductDetails extends AppCompatActivity {

    ImageView productImage;
    TextView productName,productDescription,productDuration,productPrice;
    Button purchaseProduct;
    int flg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchased_product_details);

        Intent intent = getIntent();
        ProductInfo productInfo = (ProductInfo) intent.getSerializableExtra("product");

        productImage = findViewById(R.id.product_image);
        productName = findViewById(R.id.product_name);
        productDescription = findViewById(R.id.product_description);
        productDuration = findViewById(R.id.product_duration);
        purchaseProduct = findViewById(R.id.purchase_button);
        productPrice = findViewById(R.id.product_price);

        productName.setText("Name : "+productInfo.getName());

        productDescription.setText("Description : "+productInfo.getDescription());
        productDuration.setText("Duration : "+productInfo.getDuration());
        productPrice.setText("Price : "+productInfo.getPrice());

        Uri imageUri = Uri.parse(productInfo.getImageUrl());
        Picasso.get().load(imageUri).into(productImage);

        purchaseProduct.setOnClickListener(v -> {

            flg=0;

            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
            Query emailQuery = usersRef.orderByChild("userInfo/email").equalTo(productInfo.getOwnerId());
            emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                        //String userId = userSnapshot.getKey();
                        String username = userSnapshot.child("userInfo/userId").getValue(String.class);

                        usersRef.child(username).child("myProducts")
                                .child(productInfo.getName()).child("soldTo")
                                .setValue("none");
                        flg=1;
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle error
                    System.out.println("Error: " + databaseError.getMessage());
                }
            });

            //remove product from purchased list
            DatabaseReference useref2 = usersRef.child(FirebaseAuth.getInstance().getUid());

            useref2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("myPurchasedProducts")) {
                        // 'myPurchasedProducts' child exists
                        DataSnapshot purchasedProductsSnapshot = dataSnapshot.child("myPurchasedProducts");

                        // Replace "productNameToSearch" with the actual product name you are searching for
                        String productNameToSearch = productInfo.getName();

                        // Search for the product by name
                        for (DataSnapshot productSnapshot : purchasedProductsSnapshot.getChildren()) {
                            String productName = productSnapshot.child("name").getValue(String.class);

                            if (productName != null && productName.equals(productNameToSearch)) {
                                productSnapshot.getRef().removeValue();
                                flg=2;
                            }
                        }
                    } else {
                        Toast.makeText(PurchasedProductDetails.this, "product not found", Toast.LENGTH_SHORT).show();
                   }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                }
            });

                    Toast.makeText(this, "product cancled successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PurchasedProductDetails.this, Home.class));

        });
    }
}