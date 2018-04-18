package com.example.eraky.reviewit.admin;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.eraky.reviewit.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddNewProductActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static final String PRODUCT = "Product";
    public static final String FLAG = "flag";
    public static final String USER_CHOICE_SPINNER = "userChoiceSpinner";
    ArrayList<Category> categories;
    ArrayList<String> categoriesItem;
    EditText name, price, description;
    String range;
    Uri uri;
    ArrayAdapter<String> dataAdapter;
    boolean flag;
    Product product;
    int userChoice;
    FirebaseStorage storage;
    StorageReference storageReference;
    Spinner spinner;
    private int PICK_IMAGE_REQUEST = 1;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categories = new ArrayList<>();
        categoriesItem = new ArrayList<>();
        setContentView(R.layout.activity_add_new_product);
        if (getIntent() != null) {
            product = getIntent().getParcelableExtra(PRODUCT);
            flag = product != null;
        }


        if (savedInstanceState != null) {
            product = savedInstanceState.getParcelable(PRODUCT);
            flag = savedInstanceState.getBoolean(FLAG, false);
            userChoice = savedInstanceState.getInt(USER_CHOICE_SPINNER, -1);
            uri=savedInstanceState.getParcelable("Selectedimage");
        }
        name = (EditText) findViewById(R.id.new_product_name);
        price = (EditText) findViewById(R.id.new_product_price);
        description = (EditText) findViewById(R.id.new_product_description);
        isStoragePermissionGranted();
        if (flag) {
            name.setText(product.getName());
            price.setText(product.getPrice());
            description.setText(product.getDescription());
        }
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(AddNewProductActivity.this);
        dataAdapter = new ArrayAdapter<String>(AddNewProductActivity.this, R.layout.spinner_layout, categoriesItem);
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spinner.setAdapter(dataAdapter);

        if (userChoice != -1) {
        }
        create();
    }

    void create() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Category");
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Category category = new Category();
                category = dataSnapshot.getValue(Category.class);
                categories.add(category);
                categoriesItem.add(category.getName());
                dataAdapter.notifyDataSetChanged();
                if (flag && category.getCategory_id().equals(product.getCategory_id())) {
                    userChoice = categories.size() - 1;
                    spinner.setSelection(userChoice);
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
                // progressDialog.dismiss();

            }

        });


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            uri = data.getData();


            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        userChoice = spinner.getSelectedItemPosition();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), getString(R.string.selected) + item, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {

    }

    public void AddNewProduct(View view) {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(PRODUCT);

        if (!name.getText().toString().isEmpty()
                && !price.getText().toString().isEmpty()
                && !description.getText().toString().isEmpty()
                && spinner.getSelectedItem() != null
                ) {
            if (uri == null && !flag) {

                Toast.makeText(this, R.string.add_image, Toast.LENGTH_LONG).show();
            } else if (flag) {
                //Todo: Update
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle(getString(R.string.uploading));
                progressDialog.show();

                StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
                if (uri != null) {
                    ref.putFile(uri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    progressDialog.dismiss();
                                    Toast.makeText(AddNewProductActivity.this, R.string.upload, Toast.LENGTH_SHORT).show();
                                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                    Product product1 = new Product(categories.get(spinner.getSelectedItemPosition()).getCategory_id(), product.getId(), name.getText().toString(), description.getText().toString(),
                                            price.getText().toString()
                                            , product.getRate(),
                                            downloadUrl.toString());
                                    Map<String, Object> ProductValues = product1.toMap();
                                    Map<String, Object> childUpdates = new HashMap<>();
                                    childUpdates.put(product.getId(), ProductValues);
                                    mDatabase.updateChildren(childUpdates);

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(AddNewProductActivity.this, R.string.failed + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                            .getTotalByteCount());
                                    progressDialog.setMessage(R.string.upload + (int) progress + "%");
                                }
                            });
                } else {
                    progressDialog.dismiss();
                    Product product1 = new Product(categories.get(spinner.getSelectedItemPosition()).getCategory_id(), product.getId(), name.getText().toString(), description.getText().toString(),
                            price.getText().toString()
                            , product.getRate(),
                            product.getImage());
                    Map<String, Object> ProductValues = product1.toMap();
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put(product.getId(), ProductValues);
                    mDatabase.updateChildren(childUpdates);
                }
                Toast.makeText(this, R.string.success_update, Toast.LENGTH_LONG).show();
            } else if (!flag) {
                StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
                ref.putFile(uri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(AddNewProductActivity.this, R.string.upload, Toast.LENGTH_SHORT).show();
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                String key = mDatabase.push().getKey();
                                Product product = new Product(categories.get(spinner.getSelectedItemPosition()).getCategory_id(), key, name.getText().toString(), description.getText().toString(),
                                        price.getText().toString()
                                        , 0,
                                        downloadUrl.toString());
                                Map<String, Object> ProductValues = product.toMap();
                                Map<String, Object> childUpdates = new HashMap<>();
                                childUpdates.put(key, ProductValues);
                                mDatabase.updateChildren(childUpdates);

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddNewProductActivity.this, R.string.upload + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            }
                        });
                Toast.makeText(this, R.string.new_product, Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(this, R.string.fill_data, Toast.LENGTH_LONG).show();
        }
    }

    public void bickImage(View view) {
        Intent intent = new Intent();
// Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_img))
                , PICK_IMAGE_REQUEST);
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                //Log.v(TAG,"Permission is granted");
                return true;
            } else {

                // Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            // Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            //Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
            Toast.makeText(this, R.string.permission1, Toast.LENGTH_LONG)
                    .show();
            finish();

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(USER_CHOICE_SPINNER, userChoice);
        outState.putBoolean(FLAG, flag);
        outState.putParcelable(PRODUCT, product);
        outState.putParcelable("Selectedimage",uri);

    }

}
