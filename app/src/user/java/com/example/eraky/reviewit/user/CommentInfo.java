package com.example.eraky.reviewit.user;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mine on 20/02/18.
 */

public class CommentInfo implements Parcelable {
    public static final Creator<CommentInfo> CREATOR = new Creator<CommentInfo>() {
        @Override
        public CommentInfo createFromParcel(Parcel in) {
            return new CommentInfo(in);
        }

        @Override
        public CommentInfo[] newArray(int size) {
            return new CommentInfo[size];
        }
    };
    private String productName;
    private String categoryName;

    protected CommentInfo(Parcel in) {
        productName = in.readString();
        categoryName = in.readString();
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(productName);
        parcel.writeString(categoryName);
    }
}
