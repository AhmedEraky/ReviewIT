package com.example.eraky.reviewit.user;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eraky.reviewit.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OneProductActivity extends AppCompatActivity implements CommentAdabter.SetOncLickListener {
    public static final String PRODUCT = "Product";
    public static final String RATE_IT = "rateIt";
    public static final String COMMENT = "comment";
    public static final String MANGER = "Manger";
    public static final String COMMENT1 = "Comment";
    public static final String RATE = "rate";
    public static final String USER = "user";
    public static final String RATED = "rated";
    public static final String FAVORITE = "favorite";
    public static final String FAV = "Fav";
    public static final String NAME = "Name";
    public static final String SIZE = "size";
    ArrayList<Product> products;
    RecyclerView commentRecyclerView;
    ImageView imageView;
    RatingBar ratingBar;
    FirebaseAuth mAuth;
    Button rateIt;
    Button add_comment;
    int fav;
    List<String> Mylist;
    EditText editText;
    int counter = 0;
    ImageView imageView2;
    private ArrayList<Comment> comments;
    private CommentAdabter adabter;
    private LinearLayoutManager layoutManager;
    private Product product;
    private DatabaseReference mDatabase, mDatabase1;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_product);
        mAuth = FirebaseAuth.getInstance();
        Mylist = new ArrayList<>();
        products = new ArrayList<>();
        imageView2 = findViewById(R.id.LoveProduct);
        rateIt = findViewById(R.id.rateIt);
        add_comment = findViewById(R.id.add_comment);
        product = getIntent().getParcelableExtra(PRODUCT);
        comments = new ArrayList<>();
        commentRecyclerView = (RecyclerView) findViewById(R.id.comment_list);
        layoutManager = new LinearLayoutManager(this);

        if (savedInstanceState != null) {
            rateIt.setVisibility(savedInstanceState.getInt(RATE_IT));
            comments = savedInstanceState.getParcelableArrayList(COMMENT);
            Parcelable state = savedInstanceState.getParcelable(MANGER);
            layoutManager.onRestoreInstanceState(state);
        } else {
            isRating();
            getCommentData();
        }
        adabter = new CommentAdabter(comments, this, this);
        commentRecyclerView.setLayoutManager(layoutManager);
        commentRecyclerView.setAdapter(adabter);

        editText = (EditText) findViewById(R.id.commentText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (editText.getText().toString().trim().isEmpty())
                    add_comment.setEnabled(false);
                else
                    add_comment.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        imageView = (ImageView) findViewById(R.id.product_details_image);

        mDatabase = FirebaseDatabase.getInstance().getReference().child(USER).child(mAuth.getUid()).child(FAVORITE).child(product.getCategory_id());
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                if (product.getId().equals(key)) {
                    fav = 1;
                    Picasso.with(OneProductActivity.this).load(R.drawable.like).into(imageView2);
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
        Picasso.with(this).load(product.getImage()).resize(200, 200).placeholder(R.drawable.no_image).into(imageView);
        TextView textView = (TextView) findViewById(R.id.product_details_name);
        textView.setText(product.getName());
        TextView textView1 = (TextView) findViewById(R.id.Description);
        textView1.setText(product.getDescription());
        ratingBar = (RatingBar) findViewById(R.id.product_rate);
        ratingBar.setRating((float) product.getRate());

    }



    private void getCommentData() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(COMMENT1);
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Comment comment = new Comment();
                comment = dataSnapshot.getValue(Comment.class);
                if (comment.getPid().equals(product.getId())) {
                    comments.add(comment);
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
    public void SetOnclick(Comment comment) {

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void AddComment(View view) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && !editText.getText().toString().equals("")) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            String s = editText.getText().toString();
            //todo: add New Comment

            mDatabase = FirebaseDatabase.getInstance().getReference().child(COMMENT1);
            String key = mDatabase.push().getKey();
            Comment comment = new Comment(name, email, s, product.getId(), key);
            Map<String, Object> CommentValues = comment.toMap();
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put(key, CommentValues);
            mDatabase.updateChildren(childUpdates);
        }
    }

    public void changeRate(View view) {
        Toast.makeText(this, R.string.change, Toast.LENGTH_LONG).show();
        rateIt.setVisibility(View.GONE);
        updateRating();
    }

    private void setWidget(Context context) {

        mDatabase = FirebaseDatabase.getInstance().getReference().child(USER).child(mAuth.getUid()).child(FAVORITE);
        counter = 0;
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
                counter = 0;
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(OneProductActivity.this);
                Intent intent = new Intent(OneProductActivity.this, NewAppWidget.class);
                intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(OneProductActivity.this, NewAppWidget.class));
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

    public void MakeFavourit(View view) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(USER).child(mAuth.getUid()).child(FAVORITE).child(product.getCategory_id());
        if (fav == 0) {
            Picasso.with(this).load(R.drawable.like).into(imageView2);
            Map<String, Object> childUpdates1 = new HashMap<>();
            Map<String, Object> childUpdates2 = new HashMap<>();
            childUpdates1.put(product.getId(), childUpdates2);
            childUpdates2.put(product.getName(), 1);
            childUpdates2.put(product.getDescription(), 1);

            mDatabase.updateChildren(childUpdates1);
            Toast.makeText(this, R.string.add_fav, Toast.LENGTH_LONG).show();
            fav = 1;
        } else if (fav == 1) {
            mDatabase.child(product.getId()).removeValue();
            Picasso.with(this).load(R.drawable.dislike).into(imageView2);
            Toast.makeText(this,R.string.toast_delete , Toast.LENGTH_LONG).show();
            fav = 0;
        }

        SharedPreferences preferences = getSharedPreferences(FAV, MODE_PRIVATE);
        preferences.edit().clear().commit();
        counter = 0;
        setWidget(this);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(OneProductActivity.this);
        Intent intent = new Intent(OneProductActivity.this, NewAppWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(OneProductActivity.this, NewAppWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.listwedg);
        sendBroadcast(intent);
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
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateRating() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(PRODUCT).child(product.getId()).child(RATE);
        int NewRate = (int) ((product.getRate() + ratingBar.getRating()) / 2);
        String key = mDatabase.push().getKey();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(key, NewRate);
        mDatabase.setValue(NewRate);
        ratingBar.setRating((float) NewRate);

        mDatabase = FirebaseDatabase.getInstance().getReference().child(USER).child(mAuth.getUid()).child(RATED).child(product.getCategory_id());

        Map<String, Object> childUpdates1 = new HashMap<>();
        childUpdates1.put(product.getId(), 1);
        mDatabase.updateChildren(childUpdates1);

    }

    private void isRating() {

        mDatabase = FirebaseDatabase.getInstance().getReference().child(USER).child(mAuth.getUid()).child(RATED).child(product.getCategory_id());
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().equals(product.getId())) {
                    rateIt.setVisibility(View.GONE);
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
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(RATE_IT, rateIt.getVisibility());
        outState.putParcelableArrayList(COMMENT, comments);
        outState.putParcelable(MANGER, layoutManager.onSaveInstanceState());
        super.onSaveInstanceState(outState);

    }
}
