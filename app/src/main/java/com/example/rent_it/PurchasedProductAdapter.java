package com.example.rent_it;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PurchasedProductAdapter extends RecyclerView.Adapter<PurchasedProductAdapter.ProductViewHolder> {

    private List<ProductInfo> productList;

    public PurchasedProductAdapter(List<ProductInfo> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductInfo product = productList.get(position);

        // Bind data to the ViewHolder
        holder.txtName.setText("Name : "+product.getName());
        holder.txtPrice.setText("Price purchased at : "+String.valueOf(product.getPrice()));
        holder.txtDuration.setText("Duration : "+String.valueOf(product.getDuration()));

        // Load image using Picasso or Glide (replace with your preferred library)
        Picasso.get().load(product.getImageUrl()).into(holder.imageView);

        //on click listener to the specific product
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PurchasedProductDetails.class);
                intent.putExtra("product", product);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtName, txtPrice, txtDuration,soldTo;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.productImage);
            txtName = itemView.findViewById(R.id.productName);
            txtPrice = itemView.findViewById(R.id.productPrice);
            txtDuration = itemView.findViewById(R.id.productDuration);
            soldTo=itemView.findViewById(R.id.soldTo);
        }
    }
}
