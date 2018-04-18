package com.example.eraky.reviewit.user;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.eraky.reviewit.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyFavouActivity extends AppCompatActivity implements ProductAdabter.SetOncLickListener {
    public static final String USER = "user";
    public static final String FAVORITE = "favorite";
    public static final String PRODUCT = "Product";
    FirebaseAuth mAuth;
    EditText editText;
    List<String> Mylist;
    private RecyclerView productRecyclerView;
    private ProductAdabter adabter;
    private GridLayoutManager layoutManager;
    private ArrayList<Product> products, AllProduct;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mylist = new ArrayList<>();
        setContentView(R.layout.activity_my_favou);
        AllProduct = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        products = new ArrayList<>();
        editText = (EditText) findViewById(R.id.search_edit_text);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int productNumber = ((int) dpWidth / 101);
        adabter = new ProductAdabter(products, MyFavouActivity.this, MyFavouActivity.this);

        productRecyclerView = (RecyclerView) findViewById(R.id.Favourit);
        layoutManager = new GridLayoutManager(MyFavouActivity.this, productNumber);
        productRecyclerView.setLayoutManager(layoutManager);
        productRecyclerView.setAdapter(adabter);

        mDatabase = FirebaseDatabase.getInstance().getReference().child(USER).child(mAuth.getUid()).child(FAVORITE);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getData(Mylist);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot x : dataSnapshot.getChildren()) {
                    Mylist.add(x.getKey());
                }
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


    private void getData(final List<String> Mylist) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(PRODUCT);
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Product product = new Product();
                product = dataSnapshot.getValue(Product.class);
                if (Mylist.contains(product.getId())) {
                    products.add(product);
                    AllProduct.add(product);
                    adabter.notifyDataSetChanged();
                }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favor_menu, menu);
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
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void getFav(View view) {
        new SearchTask(editText.getText().toString()).execute(AllProduct);

    }

    public class SearchTask extends AsyncTask<ArrayList<Product>, Void, ArrayList<Product>> {
        String searchFor;

        public SearchTask(String searchFor) {
            this.searchFor = searchFor;
        }


        @Override
        protected ArrayList<Product> doInBackground(ArrayList<Product>[] arrayLists) {
            ArrayList<Product> products1 = new ArrayList<>();
            if (searchFor.equals("")) {
                products1.addAll(arrayLists[0]);
            } else {
                for (int i = 0; i < arrayLists[0].size(); i++) {
                    if (arrayLists[0].get(i).getName().toLowerCase().equals(searchFor.toLowerCase())) {
                        products1.add(arrayLists[0].get(i));
                    }
                }
            }
            return products1;
        }

        @Override
        protected void onPostExecute(ArrayList<Product> products1) {
            products.clear();
            products.addAll(products1);
            adabter.notifyDataSetChanged();
            super.onPostExecute(products);
        }
    }
}
