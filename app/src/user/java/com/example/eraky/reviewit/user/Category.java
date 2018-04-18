package com.example.eraky.reviewit.user;

import android.os.Parcel;
import android.os.Parcelable;

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
    private String name;
    private String image;
    private String Category_id;

    public Category() {
    }

    private Category(Parcel in) {
        name = in.readString();
        image = in.readString();
        Category_id = in.readString();
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
