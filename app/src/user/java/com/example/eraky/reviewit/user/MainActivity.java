package com.example.eraky.reviewit.user;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.eraky.reviewit.R;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    public static final String FAVORITE = "favorite";
    public static final String USER = "user";
    static final int GOOGLE_SIGN_IN = 1;
    public static final String FAV = "Fav";
    public static final String NAME = "Name";
    public static final String SIZE = "size";
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String status;
    int fav;
    List<String> Mylist;
    int counter = 0;
    private DatabaseReference mDatabase, mDatabase1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Mylist = new ArrayList<>();
        findViewById(R.id.google_sign_in).setOnClickListener(this);
        if (isNetworkAvailable()) {

            if (mAuth.getCurrentUser() != null) {
                setWidget(this);
                startActivity(new Intent(this, AllCategoryActivity.class));
                finish();

            }
        }
    }

    private void setWidget(Context context) {
        SharedPreferences preferences = getSharedPreferences(FAV, MODE_PRIVATE);
        preferences.edit().clear().commit();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(USER).child(mAuth.getUid()).child(FAVORITE);
        mDatabase.addChildEventListener(new ChildEventListener() {
            SharedPreferences preferences = getSharedPreferences(FAV, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot category : dataSnapshot.getChildren()) {
                    for (DataSnapshot product : category.getChildren()) {
                        editor.putString(NAME + Integer.toString(counter), product.getKey());
                        counter++;
                        editor.putInt(SIZE, counter);
                    }
                    editor.commit();
                }
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(MainActivity.this);
                Intent intent = new Intent(MainActivity.this, NewAppWidget.class);
                intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(MainActivity.this, NewAppWidget.class));
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.listwedg);
                sendBroadcast(intent);
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

    public void setGoogleSignIn() {
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.GoogleBuilder().build()))
                        .setIsSmartLockEnabled(false)
                        .build(),
                GOOGLE_SIGN_IN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN && resultCode == RESULT_OK) {

            startActivity(new Intent(this, AllCategoryActivity.class));
            finish();
        } else {
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.main_layout), status, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.google_sign_in:
                updateStatus();
                setGoogleSignIn();
                break;
        }

    }

    private void updateStatus() {

        if (isNetworkAvailable()) {

            status = String.valueOf(getResources().getText(R.string.sign_in_request_connected));
        } else {
            status = String.valueOf(getResources().getText(R.string.sign_in_request_not_connected));
        }
    }
}
