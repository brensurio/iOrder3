package com.app.brensurio.iorder;

import android.net.Uri;

/**
 * Created by Mariz L. Maas on 10/24/2016.
 */

public class Food {
    private String name;
    private String price;
    private String description;
    private String imageLink;

    Food() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    Food(String name, String price, String description, String imageLink) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageLink = imageLink;
    }

    public String getDescription() {
        return description;
    }

    public String getImageLink() {
        return imageLink;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }
}
