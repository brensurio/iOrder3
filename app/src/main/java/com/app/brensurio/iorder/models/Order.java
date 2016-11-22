package com.app.brensurio.iorder.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Created by Mariz L. Maas on 11/7/2016.
 */

public class Order implements Parcelable {
    String refNo;
    String customerName;
    List<Food> items;
    double amount;
    String location;
    String status;
    String store;
    String datetime;

    public Order() { }

    public Order(String customerName, String refNo, List<Food> items, double amount,
                 String location, String status) {
        this.customerName = customerName;
        this.refNo = refNo;
        this.items = items;
        this.amount = amount;
        this.location = location;
        this.status = status;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<Food> getItems() {
        return items;
    }

    public void setItems(List<Food> items) {
        this.items = items;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return getRefNo().equals(order.getRefNo());

    }

    @Override
    public int hashCode() {
        return getRefNo().hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.refNo);
        dest.writeString(this.customerName);
        dest.writeTypedList(this.items);
        dest.writeDouble(this.amount);
        dest.writeString(this.location);
        dest.writeString(this.status);
        dest.writeString(this.store);
        dest.writeString(this.datetime);
    }

    protected Order(Parcel in) {
        this.refNo = in.readString();
        this.customerName = in.readString();
        this.items = in.createTypedArrayList(Food.CREATOR);
        this.amount = in.readDouble();
        this.location = in.readString();
        this.status = in.readString();
        this.store = in.readString();
        this.datetime = in.readString();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel source) {
            return new Order(source);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}
