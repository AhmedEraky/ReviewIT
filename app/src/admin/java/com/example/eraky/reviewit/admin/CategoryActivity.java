package com.example.eraky.reviewit.admin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

import com.example.eraky.reviewit.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity implements CategoryAdabter.SetOncLickListener, CategoryAdabter.DeleteCategory {

    public static final String CAT = "cat";
    public static final String MANGER = "Manger";
    public static final String CATEGORY = "Category";
    public static final String ID = "ID";
    public static final String RANGE = "Range";
    public static final String PRODUCT = "Product";
    public static final String CATEGORY_ID = "Category_id";
    GridLayoutManager layoutManager;
    ArrayList<Category> categories;
    private RecyclerView categoryRecyclerView;
    private CategoryAdabter adabter;
    private DatabaseReference mDatabase;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(CAT, categories);
        outState.putParcelable(MANGER, layoutManager.onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int categoryNumber = ((int) dpWidth / 101);
        layoutManager = new GridLayoutManager(this, categoryNumber);
        if (savedInstanceState != null) {
            categories = savedInstanceState.getParcelableArrayList(CAT);
            Parcelable state = savedInstanceState.getParcelable(MANGER);
            layoutManager.onRestoreInstanceState(state);

        } else {
            getData();

        }
        adabter = new CategoryAdabter(categories, this, this, this);
        categoryRecyclerView = (RecyclerView) findViewById(R.id.category_list);
        categoryRecyclerView.setLayoutManager(layoutManager);
        categoryRecyclerView.setHasFixedSize(true);
        categoryRecyclerView.setAdapter(adabter);

    }

    private void getData() {
        categories = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(CATEGORY);
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Category category = new Category();
                category = dataSnapshot.getValue(Category.class);
                categories.add(category);
                adabter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void SetOnclick(Category category) {
        Intent intent = new Intent(this, ProductActivity.class);
        intent.putExtra(ID, category.getCategory_id());
        intent.putExtra(RANGE, 1);
        this.startActivity(intent);
    }


    @Override
    public void Delete(int position) {
        final ArrayList<String> str = new ArrayList<>();

        String cat_id = categories.get(position).getCategory_id();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(PRODUCT);
        mDatabase.orderByChild(CATEGORY_ID).equalTo(cat_id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mDatabase.child(dataSnapshot.getKey()).removeValue();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        DatabaseReference mDatabase1 = FirebaseDatabase.getInstance().getReference().child(CATEGORY).child(categories.get(position).getCategory_id());
        mDatabase1.removeValue();
        categories.remove(position);
        adabter.notifyDataSetChanged();
    }


}
