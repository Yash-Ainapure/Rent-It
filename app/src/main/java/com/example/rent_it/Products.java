package com.example.rent_it;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Products extends AppCompatActivity {

    Button searchBtn;
    EditText searchEdt;
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private ProductAdapter productAdapter;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // Handle the back button
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<ProductInfo> productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList,this);
        recyclerView.setAdapter(productAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    // Check if the user node has "myProducts" node
                    if (userSnapshot.child("myProducts").exists()) {
                        // User has "myProducts", proceed to retrieve and display them
                        DataSnapshot myProductsSnapshot = userSnapshot.child("myProducts");

                        for (DataSnapshot productSnapshot : myProductsSnapshot.getChildren()) {
                            ProductInfo productInfo = productSnapshot.getValue(ProductInfo.class);
                            if(productInfo.getSoldTo().equals("none")){
                                productList.add(productInfo);
                            }
                        }
                    }
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Products.this, "Failed to load products: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}