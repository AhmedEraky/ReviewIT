package com.example.eraky.reviewit.admin;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eraky.reviewit.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Eraky on 2/11/2018.
 */

public class ProductAdabter extends RecyclerView.Adapter<ProductAdabter.ProductViewHolder> {

    private final ArrayList<Product> products;
    private final Context context;
    private final ProductAdabter.SetOncLickListener listener;
    DeleteProduct deleteProduct;

    public ProductAdabter(ArrayList<Product> products, Context context, ProductAdabter.SetOncLickListener listener, DeleteProduct deleteProduct) {
        this.products = products;
        this.context = context;
        this.listener = listener;
        this.deleteProduct = deleteProduct;
    }

    @Override
    public ProductAdabter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(root);
    }

    @Override
    public void onBindViewHolder(ProductAdabter.ProductViewHolder holder, final int position) {

        Picasso.with(context).load(products.get(position).getImage()).resize(400, 400).placeholder(R.drawable.no_image).into(holder.productImage);
        holder.name.setText(products.get(position).getName());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteProduct.delet(position);
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteProduct.editProduct(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return products.size();
    }


    public interface SetOncLickListener {
        void SetOnclick(Product product);
    }

    public interface DeleteProduct {
        void delet(int position);

        void editProduct(int position);
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView productImage;
        final TextView name;
        Button button;
        Button edit;

        public ProductViewHolder(View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            name = itemView.findViewById(R.id.product_name);
            itemView.setOnClickListener(this);
            button = itemView.findViewById(R.id.deleteProduct);
            edit = itemView.findViewById(R.id.editProduct);
        }

        @Override
        public void onClick(View view) {
            listener.SetOnclick(products.get(getAdapterPosition()));
        }
    }
}
