package com.example.eraky.reviewit.admin;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mine on 08/02/18.
 */

public class Comment implements Parcelable {

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
    private String cname;
    private String email;
    private String body;
    private int pid;
    private int cid;
    private int appear;

    protected Comment(Parcel in) {
        cname = in.readString();
        email = in.readString();
        body = in.readString();
        pid = in.readInt();
        cid = in.readInt();
        appear = in.readInt();
    }

    public static Creator<Comment> getCREATOR() {
        return CREATOR;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getAppear() {
        return appear;
    }

    public void setAppear(int appear) {
        this.appear = appear;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(cname);
        parcel.writeString(email);
        parcel.writeString(body);
        parcel.writeInt(pid);
        parcel.writeInt(cid);
        parcel.writeInt(appear);
    }
}
