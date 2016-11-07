package com.app.brensurio.iorder;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
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

    @Override
    public boolean equals(Object o) {
        if ((o instanceof Order) && (Objects.equals(((Order) o).getRefNo(),
                this.getRefNo()))) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int value = 7;
        int result;
        result = (value / 11);
        return result;
    }
}
