package com.example.eraky.reviewit.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.eraky.reviewit.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;

public class UserHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
    }

    public void AddProductActivity(View view) {
        Intent intent = new Intent(this, AddNewProductActivity.class);
        startActivity(intent);
    }

    public void addCategoryActivity(View view) {
        Intent intent = new Intent(this, AddNewCategoryActivity.class);
        startActivity(intent);
    }


    public void mangeAplicationActivity(View view) {
        Intent intent = new Intent(this, CategoryActivity.class);
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
        }
        return super.onOptionsItemSelected(item);
    }
}
