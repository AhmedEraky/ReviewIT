package com.example.eraky.reviewit.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

import com.example.eraky.reviewit.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity implements ProductAdabter.SetOncLickListener {

    public static final String ID = "ID";
    public static final String MANGER = "Manger";
    public static final String PRODUCT = "Product";
    public static final String CATEGORY_ID = "Category_id";
    String categoryId;
    private RecyclerView productRecyclerView;
    private ProductAdabter adabter;
    private GridLayoutManager layoutManager;
    private ArrayList<Product> products;
    private ArrayList<Product> importantProduct;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_product);
        categoryId = getIntent().getStringExtra(ID);
        importantProduct = new ArrayList<>();
        products = new ArrayList<>();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int productNumber = ((int) dpWidth / 101);
        layoutManager = new GridLayoutManager(ProductActivity.this, productNumber);
        if (savedInstanceState != null) {
            products = savedInstanceState.getParcelableArrayList("prod");
            Parcelable state = savedInstanceState.getParcelable(MANGER);
            layoutManager.onRestoreInstanceState(state);
            ;

        } else {
            getData();
        }
        adabter = new ProductAdabter(products, ProductActivity.this, ProductActivity.this);
        productRecyclerView = (RecyclerView) findViewById(R.id.product_list);
        productRecyclerView.setLayoutManager(layoutManager);
        productRecyclerView.setHasFixedSize(true);
        productRecyclerView.setAdapter(adabter);

    }


    private void getData() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(PRODUCT);
        mDatabase.orderByChild(CATEGORY_ID).equalTo(categoryId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Product product = new Product();
                product = dataSnapshot.getValue(Product.class);
                products.add(product);
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
    public void SetOnclick(Product product) {
        Intent intent = new Intent(this, OneProductActivity.class);
        intent.putExtra(PRODUCT, product);
        startActivity(intent);
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
                finish();
                break;
            case R.id.Home:
                Intent intent1 = new Intent(this, AllCategoryActivity.class);
                this.startActivity(intent1);
                finish();
                break;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("prod", products);
        outState.putParcelable(MANGER, layoutManager.onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }
}
