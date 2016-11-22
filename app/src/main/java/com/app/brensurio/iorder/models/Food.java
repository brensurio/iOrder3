package com.app.brensurio.iorder.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by Mariz L. Maas on 10/24/2016.
 */

public class Food implements Parcelable {
    private String name;
    private double price;
    private String description;
    private String imageLink;
    private String store;
    private int amount;

    Food() { } // Default constructor required for calls to DataSnapshot.getValue(User.class)


    public Food(String name, double price, String description, String imageLink, String store) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageLink = imageLink;
        this.store = store;
        this.amount = 1;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Food food = (Food) o;

        if (Double.compare(food.getPrice(), getPrice()) != 0) return false;
        return getName().equals(food.getName());

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getName().hashCode();
        temp = Double.doubleToLongBits(getPrice());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeDouble(this.price);
        dest.writeString(this.description);
        dest.writeString(this.imageLink);
        dest.writeString(this.store);
        dest.writeInt(this.amount);
    }

    protected Food(Parcel in) {
        this.name = in.readString();
        this.price = in.readDouble();
        this.description = in.readString();
        this.imageLink = in.readString();
        this.store = in.readString();
        this.amount = in.readInt();
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel source) {
            return new Food(source);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };
}
