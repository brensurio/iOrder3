package com.app.brensurio.iorder.interfaces;

import com.app.brensurio.iorder.models.Food;

import java.util.ArrayList;

/**
 * Created by Mariz L. Maas on 11/11/2016.
 */

public interface StoreFragmentListener {
    ArrayList<Food> getOrderList();
    void addToOrderList(Food food);
    void removeFromOrderList(Food food);
}
