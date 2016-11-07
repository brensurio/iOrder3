package com.app.brensurio.iorder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CartActivity extends AppCompatActivity {

    ArrayList<Food> orderList;
    String name;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        orderList = (ArrayList<Food>) getIntent().getSerializableExtra("foodList");
        name = getIntent().getStringExtra("name");

        Button placeOrder = (Button) findViewById(R.id.place_order_button);
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processOrders(orderList);
            }
        });
    }

    private void processOrders(ArrayList<Food> orderList) {
        List<Food> store1OrderList = new ArrayList<>();
        List<Food> store2OrderList = new ArrayList<>();
        List<Food> store3OrderList = new ArrayList<>();
        Order store1Order;
        Order store2Order;
        Order store3Order;

        for (Food food : orderList) {
            if (Objects.equals(food.getStore(), "store1")) {
                store1OrderList.add(food);
            } else if (Objects.equals(food.getStore(), "store2")) {
                store2OrderList.add(food);
            } else if (Objects.equals(food.getStore(), "store3")) {
                store3OrderList.add(food);
            }
            orderList.remove(food);
        }
        Toast.makeText(this, String.valueOf(store1OrderList.size()),
                Toast.LENGTH_SHORT).show();
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

            mDatabase.child("store1orders").push().setValue(store1Order);
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
