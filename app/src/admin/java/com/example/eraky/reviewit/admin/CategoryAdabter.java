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
 * Created by Eraky on 2/10/2018.
 */

public class CategoryAdabter extends RecyclerView.Adapter<CategoryAdabter.CategoryViewHolder> {
    private final ArrayList<Category> categories;
    private final Context context;
    private final SetOncLickListener listener;
    private final DeleteCategory deleteListner;

    public CategoryAdabter(ArrayList<Category> categories, Context context, SetOncLickListener listener, DeleteCategory deleteCategory) {
        this.categories = categories;
        this.context = context;
        this.listener = listener;
        deleteListner = deleteCategory;

    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(root);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, final int position) {

        Picasso.with(context).load(categories.get(position).getImage()).resize(400, 400).placeholder(R.drawable.no_image).into(holder.categoryImage);
        holder.name.setText(categories.get(position).getName());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteListner.Delete(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public interface SetOncLickListener {
        void SetOnclick(Category category);
    }

    public interface DeleteCategory {
        void Delete(int position);
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView categoryImage;
        final TextView name;
        Button button;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.category_image);
            name = itemView.findViewById(R.id.category_name);
            button = itemView.findViewById(R.id.deleteCatButton);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            listener.SetOnclick(categories.get(getAdapterPosition()));
        }
    }

}
