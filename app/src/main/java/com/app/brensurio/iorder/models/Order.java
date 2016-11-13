package com.app.brensurio.iorder.models;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Created by Mariz L. Maas on 11/7/2016.
 */

public class Order implements Serializable{
    String refNo;
    String customerName;
    List<String> items;
    String amount;
    String location;
    String status;
    String store;

    public Order() { }

    public Order(String customerName, String refNo, List<String> items, String amount,
                 String location, String status) {
        this.customerName = customerName;
        this.refNo = refNo;
        this.items = items;
        this.amount = amount;
        this.location = location;
        this.status = status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
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
}
