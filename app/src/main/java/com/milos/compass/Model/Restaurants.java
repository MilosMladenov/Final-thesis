package com.milos.compass.Model;

public class Restaurants {

    private String mName;
    private double mRating;
    private String mAddress;

    public Restaurants() {
    }

    public Restaurants(String mName, double mRating, String mAddress) {
        this.mName = mName;
        this.mRating = mRating;
        this.mAddress = mAddress;
    }



    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public double getmRating() {
        return mRating;
    }

    public void setmRating(double mRating) {
        this.mRating = mRating;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

}

