package com.example.rent_it;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ProductDetails extends AppCompatActivity {

    ImageView productImage;
    TextView productName,productDescription,productDuration,productPrice;
    Button purchaseProduct;
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
        setContentView(R.layout.activity_product_details);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Product Details");
        }
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
            Intent intent1 = new Intent(ProductDetails.this,PurchaseActivity.class);
            intent1.putExtra("product",productInfo);
            startActivity(intent1);
        });
    }
}