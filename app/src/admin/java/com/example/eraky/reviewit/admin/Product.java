package com.example.eraky.reviewit.admin;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class Product implements Parcelable {

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
    public static final String NAME = "name";
    public static final String PIMage = "image";
    public static final String CATEGORY_ID = "Category_id";
    public static final String ID = "id";
    public static final String DESCRIPTION = "description";
    public static final String PRICE = "price";
    public static final String RATE = "rate";
    private String Category_id;
    private String id;
    private String name;
    private String description;
    private String price;
    private double rate;
    private String image;

    protected Product(Parcel in) {
        Category_id = in.readString();
        id = in.readString();
        name = in.readString();
        description = in.readString();
        price = in.readString();
        rate = in.readDouble();
        image = in.readString();
    }

    public Product(String category_id, String id, String name, String description, String price, double rate, String image) {
        Category_id = category_id;
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.rate = rate;
        this.image = image;
    }

    public Product() {
    }

    public static Creator<Product> getCREATOR() {
        return CREATOR;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result;
        result = new HashMap<>();
        result.put(NAME, name);
        result.put(PIMage, image);
        result.put(CATEGORY_ID, Category_id);
        result.put(ID, id);
        result.put(DESCRIPTION, description);
        result.put(PRICE, price);
        result.put(RATE, rate);

        return result;
    }

    public String getCategory_id() {

        return Category_id;
    }

    public void setCategory_id(String category_id) {
        Category_id = category_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Category_id);
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(price);
        parcel.writeDouble(rate);
        parcel.writeString(image);
    }
}
