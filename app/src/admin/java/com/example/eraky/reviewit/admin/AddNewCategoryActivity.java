package com.example.eraky.reviewit.admin;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.eraky.reviewit.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddNewCategoryActivity extends AppCompatActivity {
    public static final String CATEGORY = "Category";
    private final int PICK_IMAGE_REQUEST = 71;
    EditText editText;
    FirebaseStorage storage;
    StorageReference storageReference;
    private Uri filePath;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_category);
        if(savedInstanceState!=null)
        {
            filePath=savedInstanceState.getParcelable("uri");
        }
        editText = (EditText) findViewById(R.id.new_category_name);
        isStoragePermissionGranted();

    }

    public void bickImage(View view) {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.pic_img)), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView imageView = (ImageView) findViewById(R.id.new_image);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addCategory(View view) {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(CATEGORY);

        if (filePath != null && !editText.getText().toString().equals("")) {


            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(AddNewCategoryActivity.this, R.string.upload, Toast.LENGTH_SHORT).show();
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            String key = mDatabase.push().getKey();
                            Category category = new Category(editText.getText().toString(), downloadUrl.toString(), key);
                            Map<String, Object> categoryValues = category.toMap();
                            Map<String, Object> childUpdates = new HashMap<>();
                            childUpdates.put(key, categoryValues);
                            mDatabase.updateChildren(childUpdates);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddNewCategoryActivity.this, getString(R.string.failed) + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        }
                    });
            Toast.makeText(AddNewCategoryActivity.this,R.string.uploading,Toast.LENGTH_LONG);

        }
        else
        {
            Toast.makeText(this, R.string.fill_data, Toast.LENGTH_LONG).show();

        }
    }


    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
             return true;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("uri",filePath);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, R.string.permission1, Toast.LENGTH_LONG)
                    .show();
            finish();

        }
    }

}
