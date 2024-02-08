package com.example.rent_it;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<ProductInfo> productList;

    public ProductAdapter(List<ProductInfo> productList) {
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
        holder.txtPrice.setText("Price : "+String.valueOf(product.getPrice()));
        holder.txtDuration.setText("Duration : "+String.valueOf(product.getDuration()));

        // Load image using Picasso or Glide (replace with your preferred library)
        Picasso.get().load(product.getImageUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtName, txtPrice, txtDuration;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.productImage);
            txtName = itemView.findViewById(R.id.productName);
            txtPrice = itemView.findViewById(R.id.productPrice);
            txtDuration = itemView.findViewById(R.id.productDuration);
        }
    }
}
