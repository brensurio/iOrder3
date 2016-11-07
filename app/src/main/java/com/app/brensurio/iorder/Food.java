package com.app.brensurio.iorder;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by Mariz L. Maas on 10/24/2016.
 */

public class Food implements Serializable{
    private String name;
    private String price;
    private String description;
    private String imageLink;
    private String store;

    Food() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    Food(String name, String price, String description, String imageLink, String store) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageLink = imageLink;
        this.store = store;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    @Override
    public boolean equals(Object o) {
        if ((o instanceof Order) && (Objects.equals(((Food) o).getName(),
                this.getName()) && (Objects.equals(((Food) o).getPrice(),
                this.getPrice())))) {
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
