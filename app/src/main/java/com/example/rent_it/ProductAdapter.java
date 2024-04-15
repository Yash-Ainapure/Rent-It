package com.example.rent_it;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<ProductInfo> productList;
    private Context context; // Add a Context field


    public ProductAdapter(List<ProductInfo> productList,Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_purchased_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductInfo product = productList.get(position);

        // Bind data to the ViewHolder
        holder.txtName.setText("Name : "+product.getName());
        holder.txtPrice.setText("Price : "+String.valueOf(product.getPrice()));
        holder.txtDuration.setText("Duration : "+String.valueOf(product.getDuration()));
//        if(product.getOwnerId().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
//            if(!product.getSoldTo().equals("none")) {
//                holder.soldTo.setText("purchased by: "+String.valueOf(product.getSoldTo()));
//            }
//        }else{
//            holder.btnDelete.setVisibility(View.GONE);
//        }
        if (product.getOwnerId() != null && FirebaseAuth.getInstance().getCurrentUser() != null && product.getOwnerId().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
            if (product.getSoldTo() != null && !product.getSoldTo().equals("none")) {
                holder.soldTo.setText("purchased by: " + String.valueOf(product.getSoldTo()));
            }
        } else {
            holder.btnDelete.setVisibility(View.GONE);
        }
        // Load image using Picasso or Glide (replace with your preferred library)
        Picasso.get().load(product.getImageUrl()).into(holder.imageView);

        //on click listener to the specific product
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    ProductInfo clickedProduct = productList.get(adapterPosition);
                    Intent intent = new Intent(v.getContext(), ProductDetails.class);
                    intent.putExtra("product", clickedProduct);
                    v.getContext().startActivity(intent);
                }
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    showDeleteConfirmationDialog(adapterPosition);
                }            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    private void showDeleteConfirmationDialog(final int position) {
        if (context == null) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete this product?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteProduct(position);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked No, do nothing or handle accordingly
            }
        });

        builder.show();
    }

    private void deleteProduct(int position) {
        // Perform the delete operation here

        if (position >= 0 && position < productList.size()) {
            ProductInfo deletedProduct = productList.get(position);

            if(deletedProduct.getSoldTo().equals("none")) {
                productList.remove(position);
                notifyItemRemoved(position);
                String UID = FirebaseAuth.getInstance().getUid();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(UID).child("myProducts");
                databaseReference.child(deletedProduct.getName()).removeValue();
            }else{
                Toast.makeText(context, "cannot delete,product is purchased by : "+deletedProduct.getSoldTo(), Toast.LENGTH_SHORT).show();
            }

        }

    }
    public void updateList(List<ProductInfo> newList) {
        productList = new ArrayList<>();
        productList.addAll(newList);
        notifyDataSetChanged();
    }



    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtName, txtPrice, txtDuration,soldTo;
        Button btnDelete;


        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.productImage);
            txtName = itemView.findViewById(R.id.productName);
            txtPrice = itemView.findViewById(R.id.productPrice);
            txtDuration = itemView.findViewById(R.id.productDuration);
            soldTo=itemView.findViewById(R.id.soldTo);
            btnDelete=itemView.findViewById(R.id.deleteProduct);
        }
    }
}
