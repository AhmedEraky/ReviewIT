package com.example.eraky.reviewit.user;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mine on 08/02/18.
 */

public class Comment implements Parcelable {

    public static final String CNAME = "cname";
    public static final String EMAIL = "email";
    public static final String BODY = "body";
    public static final String CID = "cid";
    public static final String PID = "pid";
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
    private String pid;
    private String cid;

    public Comment() {
    }

    public Comment(String cname, String email, String body, String pid, String cid) {
        this.cname = cname;
        this.email = email;
        this.body = body;
        this.pid = pid;
        this.cid = cid;
    }

    protected Comment(Parcel in) {
        cname = in.readString();
        email = in.readString();
        body = in.readString();
        pid = in.readString();
        cid = in.readString();
    }

    public static Creator<Comment> getCREATOR() {
        return CREATOR;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result;
        result = new HashMap<>();
        result.put(CNAME, cname);
        result.put(EMAIL, email);
        result.put(BODY, body);
        result.put(PID, pid);
        result.put(CID, cid);

        return result;
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

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
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
        parcel.writeString(pid);
        parcel.writeString(cid);
    }
}
