package com.example.eraky.reviewit.admin;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eraky on 2/10/2018.
 */

public class Category implements Parcelable {
    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
    public static final String NAME = "name";
    public static final String CATEGORY_ID = "Category_id";
    public static final String CIMAGE = "image";

    private String name;
    private String image;
    private String Category_id;

    public Category() {
    }

    public Category(String name, String image, String category_id) {
        this.name = name;
        this.image = image;
        Category_id = category_id;
    }

    private Category(Parcel in) {
        name = in.readString();
        image = in.readString();
        Category_id = in.readString();
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result;
        result = new HashMap<>();
        result.put(NAME, name);
        result.put(CIMAGE, image);
        result.put(CATEGORY_ID, getCategory_id());
        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory_id() {
        return Category_id;
    }

    public void setCategory_id(String category_id) {
        Category_id = category_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(image);
        parcel.writeString(Category_id);
    }
}
