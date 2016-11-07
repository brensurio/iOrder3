package com.app.brensurio.iorder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    ArrayList<Food> orderList;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        orderList = (ArrayList<Food>) getIntent().getSerializableExtra("foodList");
        name = getIntent().getStringExtra("name");
        Toast.makeText(this, "Cart contains " + orderList.size() + " items.",
                Toast.LENGTH_SHORT).show();
    }

    private void processOrders(ArrayList<Food> orderList) {
        ArrayList<Food> store1OrderList = new ArrayList<>();
        ArrayList<Food> store2OrderList = new ArrayList<>();
        ArrayList<Food> store3OrderList = new ArrayList<>();
        Order store1Order;
        Order store2Order;
        Order store3Order;

        for (Food food : orderList) {
            if (food.getStore() == "store1") {
                store1OrderList.add(food);
            } else if (food.getStore() == "store2") {
                store2OrderList.add(food);
            } else if (food.getStore() == "store3") {
                store3OrderList.add(food);
            }
            orderList.remove(food);
        }

        if (!store1OrderList.isEmpty()) {
            String name = this.name;
            String refNo = "";
            ArrayList<String> items = new ArrayList<>();
            for (Food food : store1OrderList) {
                items.add(food.getName());
            }
            double amount = 0.0;
            for (Food food : store1OrderList) {
                amount += Double.parseDouble(food.getPrice());
            }
            String location = "";
            String status = "processing";

            store1Order = new Order(name, refNo, items, String.valueOf(amount), location, status);
        }
        if (!store2OrderList.isEmpty()) {
            String name = this.name;
            String refNo = "";
            ArrayList<String> items = new ArrayList<>();
            for (Food food : store2OrderList) {
                items.add(food.getName());
            }
            double amount = 0.0;
            for (Food food : store2OrderList) {
                amount += Double.parseDouble(food.getPrice());
            }
            String location = "";
            String status = "processing";

            store2Order = new Order(name, refNo, items, String.valueOf(amount), location, status);
        }
        if (!store3OrderList.isEmpty()) {
            String name = this.name;
            String refNo = "";
            ArrayList<String> items = new ArrayList<>();
            for (Food food : store3OrderList) {
                items.add(food.getName());
            }
            double amount = 0.0;
            for (Food food : store3OrderList) {
                amount += Double.parseDouble(food.getPrice());
            }
            String location = "";
            String status = "processing";

            store3Order = new Order(name, refNo, items, String.valueOf(amount), location, status);
        }
    }
}
