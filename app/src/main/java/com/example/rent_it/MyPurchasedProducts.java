package com.example.rent_it;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyPurchasedProducts extends AppCompatActivity {

    private PurchasedProductAdapter productAdapter;
    DatabaseReference ref;
    private RecyclerView recyclerView;
    private TextView totalSpendings;
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
        setContentView(R.layout.activity_my_purchased_products);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("My Purchased Products");
        }
        totalSpendings=findViewById(R.id.spendings);
        List<ProductInfo> productList = new ArrayList<>();
        String UID=FirebaseAuth.getInstance().getCurrentUser().getUid();
        ref = FirebaseDatabase.getInstance().getReference("users").child(UID);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new PurchasedProductAdapter(productList);
        recyclerView.setAdapter(productAdapter);
        final int[] total = {0};
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                total[0] = 0;

                if (dataSnapshot.child("myPurchasedProducts").exists()) {
                    DataSnapshot myProductsSnapshot = dataSnapshot.child("myPurchasedProducts");

                    for (DataSnapshot productSnapshot : myProductsSnapshot.getChildren()) {
                        ProductInfo productInfo = productSnapshot.getValue(ProductInfo.class);
                        total[0] +=Integer.parseInt(productInfo.getPrice());
                        productList.add(productInfo);
                    }

                    // Notify the adapter about the data change
                    productAdapter.notifyDataSetChanged();
                    totalSpendings.setText("Total Spendings: ₹" + total[0]);
                } else {
                    // "myProducts" node doesn't exist, handle accordingly (e.g., show a message)
                    Toast.makeText(MyPurchasedProducts.this, "No products found for this user", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MyPurchasedProducts.this, "Failed to load products: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}