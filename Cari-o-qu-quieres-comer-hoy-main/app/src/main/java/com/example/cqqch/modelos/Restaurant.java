package com.example.cqqch.modelos;

import android.os.Parcel;
import android.os.Parcelable;

public class Restaurant implements Parcelable {
    private String id;
    private String name;
    private String address;
    private String category;
    private String price;
    private double rating;
    private String comment;
    private boolean isFavorite;
    private boolean canGo;
    private boolean canOrder;

    public Restaurant(String id, String name, String address, String category, String price, double rating, String comment, boolean isFavorite, boolean canGo, boolean canOrder) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.category = category;
        this.price = price;
        this.rating = rating;
        this.comment = comment;
        this.isFavorite = isFavorite;
        this.canGo = canGo;
        this.canOrder = canOrder;
    }

    public Restaurant() {
        // Constructor vacío requerido por Firebase
    }

    protected Restaurant(Parcel in) {
        id = in.readString();
        name = in.readString();
        address = in.readString();
        category = in.readString();
        price = in.readString();
        rating = in.readDouble();
        comment = in.readString();
        isFavorite = in.readByte() != 0;
        canGo = in.readByte() != 0;
        canOrder = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(category);
        dest.writeString(price);
        dest.writeDouble(rating);
        dest.writeString(comment);
        dest.writeByte((byte) (isFavorite ? 1 : 0));
        dest.writeByte((byte) (canGo ? 1 : 0));
        dest.writeByte((byte) (canOrder ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCategory() {
        return category;
    }

    public String getPrice() {
        return price;
    }

    public double getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public boolean isCanGo() {
        return canGo;
    }

    public boolean isCanOrder() {
        return canOrder;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public void setCanGo(boolean canGo) {
        this.canGo = canGo;
    }

    public void setCanOrder(boolean canOrder) {
        this.canOrder = canOrder;
    }
}
