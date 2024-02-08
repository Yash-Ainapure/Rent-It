package com.example.rent_it;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class PurchaseActivity extends AppCompatActivity {

    Button purchaseProduct;
    TextView productName,productPrice,ownerName;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        Intent intent = getIntent();
        ProductInfo productInfo = (ProductInfo) intent.getSerializableExtra("product");

        productName = findViewById(R.id.textViewName);
        productPrice = findViewById(R.id.textViewPrice);
        ownerName = findViewById(R.id.textViewOwner);
        purchaseProduct = findViewById(R.id.buttonPurchase);

        productName.setText("Name : "+productInfo.getName());
        productPrice.setText("Price : "+productInfo.getPrice());
        ownerName.setText("Owner : "+productInfo.getOwnerId());

        String ownerCheck= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if(ownerCheck.equals(productInfo.getOwnerId())){
            purchaseProduct.setEnabled(false);
            purchaseProduct.setText("You are the owner");
        }
        else{
            purchaseProduct.setEnabled(true);
            purchaseProduct.setText("Purchase");
        }
        //product purchased?
        //tya user chya soldTo change karene
        //my purchased madhe add karne

        purchaseProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                                    .setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail().toString());

                            usersRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("myPurchasedProducts").push().setValue(productInfo);

                            Toast.makeText(PurchaseActivity.this, "Product purchased successfully", Toast.LENGTH_SHORT).show();
                            //redirecting to home page after successful product purchased
                            startActivity(new Intent(PurchaseActivity.this,Home.class));
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle error
                        System.out.println("Error: " + databaseError.getMessage());
                    }
                });

            }
        });

    }
}