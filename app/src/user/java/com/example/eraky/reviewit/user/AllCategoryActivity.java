package com.example.eraky.reviewit.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

import com.example.eraky.reviewit.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AllCategoryActivity extends AppCompatActivity implements CategoryAdabter.SetOncLickListener {
    public static final String CAT = "cat";
    public static final String MANGER = "Manger";
    public static final String CATEGORY = "Category";
    public static final String ID = "ID";
    public static final String USER = "user";
    public static final String FAVORITE = "favorite";
    GridLayoutManager layoutManager;
    ArrayList<Category> categories;
    List<String> Mylist;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ArrayList<Product> products;
    private RecyclerView categoryRecyclerView;
    private CategoryAdabter adabter;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_category);
        categories = new ArrayList<>();
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

        mAuth = FirebaseAuth.getInstance();
        products = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        Mylist = new ArrayList<>();
        categoryRecyclerView = (RecyclerView) findViewById(R.id.category_list);
        adabter = new CategoryAdabter(categories, this, this);
        categoryRecyclerView.setLayoutManager(layoutManager);
        categoryRecyclerView.setHasFixedSize(true);
        categoryRecyclerView.setAdapter(adabter);


    }


    private void getData() {
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
        this.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                AuthUI.getInstance().signOut(this).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                });
                break;
            case R.id.favorite:
                Intent intent = new Intent(this, MyFavouActivity.class);
                this.startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(CAT, categories);
        outState.putParcelable(MANGER, layoutManager.onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }
}
