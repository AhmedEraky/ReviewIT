package com.example.eraky.reviewit.admin;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eraky.reviewit.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity implements ProductAdabter.SetOncLickListener, AdapterView.OnItemSelectedListener, ProductAdabter.DeleteProduct {

    public static final String MANGER = "Manger";
    public static final String PRODU = "prod";
    public static final String PRODUCT = "Product";
    public static final String CATEGORY_ID = "Category_id";
    public static final String ID = "ID";

    String categoryId;
    private RecyclerView productRecyclerView;
    private ProductAdabter adabter;
    private GridLayoutManager layoutManager;
    private ArrayList<Product> products;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_product);
        categoryId = getIntent().getStringExtra(ID);
        products = new ArrayList<>();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int productNumber = ((int) dpWidth / 101);
        productRecyclerView = (RecyclerView) findViewById(R.id.product_list);
        layoutManager = new GridLayoutManager(ProductActivity.this, productNumber);

        if (savedInstanceState != null) {
            products = savedInstanceState.getParcelableArrayList(PRODU);
            Parcelable state = savedInstanceState.getParcelable(MANGER);
            layoutManager.onRestoreInstanceState(state);
            ;

        } else {
            getData();
        }
        adabter = new ProductAdabter(products, this, this, this);

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

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void delet(int position) {
        String image = products.get(position).getImage().substring(products.get(position).getImage().lastIndexOf("/") + 1);
        mDatabase = FirebaseDatabase.getInstance().getReference().child(PRODUCT).child(products.get(position).getId());
        mDatabase.removeValue();
        products.remove(position);
        adabter.notifyDataSetChanged();
    }

    @Override
    public void editProduct(int position) {
        startActivity(new Intent(this
                , AddNewProductActivity.class).putExtra(PRODUCT, products.get(position)));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("prod", products);
        outState.putParcelable(MANGER, layoutManager.onSaveInstanceState());
        super.onSaveInstanceState(outState);
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
            case R.id.Home:
                Intent intent1 = new Intent(this, UserHome.class);
                this.startActivity(intent1);
                finish();
                break;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

}
