package com.example.eraky.reviewit.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.eraky.reviewit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eraky on 2/28/2018.
 */

public class Factory implements RemoteViewsService.RemoteViewsFactory {
    public static final String USER = "user";
    public static final String FAVORITE = "favorite";
    public static final String FAV = "Fav";
    public static final String SIZE = "size";
    public static final String NAME = "Name";
    public static final String PRODUCT = "Product";
    ArrayList<Product> products;
    Context mContext;
    List<String> Mylist;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ArrayList<String> names, descrep;
    private DatabaseReference mDatabase;

    Factory(Context context) {
        this.mContext = context;
        names = new ArrayList<>();
        descrep = new ArrayList<>();
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

        names.clear();
        descrep.clear();
        SharedPreferences preferences = mContext.getSharedPreferences(FAV, mContext.MODE_PRIVATE);
        names = new ArrayList<>();
        descrep = new ArrayList<>();
        for (int i = 0; i < preferences.getInt(SIZE, 0); i++) {
            if (i % 2 == 1)
                names.add(preferences.getString(NAME + Integer.toString(i), mContext.getString(R.string.no_data)));
            else
                descrep.add(preferences.getString(NAME + Integer.toString(i), mContext.getString(R.string.no_data)));
        }

    }

    private void getData(final List<String> Mylist) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(PRODUCT);
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Product product = new Product();
                product = dataSnapshot.getValue(Product.class);
                if (Mylist.contains(product.getId()))
                    products.add(product);
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
        while (products.size() == 0) {
        }

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (names == null)
            return 0;
        return names.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        final RemoteViews remoteView = new RemoteViews(mContext.getPackageName(), R.layout.item);
        String name = names.get(i);
        String description = descrep.get(i);
        remoteView.setTextViewText(R.id.product_widget_name, name);
        remoteView.setTextViewText(R.id.product_widget_description, description);
        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

