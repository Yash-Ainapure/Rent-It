package com.example.rent_it;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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

public class MyProducts extends AppCompatActivity {

    private ProductAdapter productAdapter;
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
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
        setContentView(R.layout.activity_my_products);

        totalSpendings=findViewById(R.id.spendings);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<ProductInfo> productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList,this);
        recyclerView.setAdapter(productAdapter);

        String UID= FirebaseAuth.getInstance().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(UID);
        final int[] total = {0};
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("My Products");
        }
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                total[0] = 0;

                // Check if the "myProducts" node exists for the specified UID
                if (dataSnapshot.child("myProducts").exists()) {
                    DataSnapshot myProductsSnapshot = dataSnapshot.child("myProducts");

                    // Iterate over products and add them to the list
                    for (DataSnapshot productSnapshot : myProductsSnapshot.getChildren()) {
                        ProductInfo productInfo = productSnapshot.getValue(ProductInfo.class);
                        total[0] +=Integer.parseInt(productInfo.getPrice());
                        productList.add(productInfo);
                    }

                    // Notify the adapter about the data change
                    productAdapter.notifyDataSetChanged();
                    totalSpendings.setText("Total Earnings: ₹" + total[0]);
                } else {
                    // "myProducts" node doesn't exist, handle accordingly (e.g., show a message)
                    Toast.makeText(MyProducts.this, "No products found for this user", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MyProducts.this, "Failed to load products: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}