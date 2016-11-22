package com.app.brensurio.iorder.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.brensurio.iorder.R;
import com.app.brensurio.iorder.adapters.OrderDetailAdapter;
import com.app.brensurio.iorder.models.Food;
import com.app.brensurio.iorder.models.Order;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CheckOutActivity extends AppCompatActivity {

    private EditText placeEditText;
    private TextInputLayout placeTextInputLayout;
    private ProgressBar progressBar;
    private LinearLayout linearLayout;

    private String customerName;
    private ArrayList<Food> orderList;
    private double total;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        getSupportActionBar().setTitle("Checkout");

        mDatabase = FirebaseDatabase.getInstance().getReference();

        customerName = getIntent().getStringExtra("customerName");
        orderList = getIntent().getParcelableArrayListExtra("orderList");

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.order_detail_list);
        OrderDetailAdapter orderDetailAdapter = new OrderDetailAdapter(orderList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(orderDetailAdapter);

        total = 0;
        for (Food food : orderList) { total += food.getAmount() * food.getPrice(); }
        TextView totalAmount = (TextView) findViewById(R.id.amount_text_view);
        totalAmount.setText(Double.toString(total));

        placeEditText = (EditText) findViewById(R.id.place_text_view);
        placeTextInputLayout = (TextInputLayout) findViewById(R.id.place_til);
        placeEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String email = placeEditText.getText().toString();
                    // Check for a valid email address.
                    if (!email.isEmpty()) {
                        placeTextInputLayout.setError(null);
                    } else {
                        placeTextInputLayout.setError("Required");
                    }
                }
            }
        });

        Button placeOrderButton = (Button) findViewById(R.id.button2);
        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptPlaceOrder();
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        linearLayout = (LinearLayout) findViewById(R.id.linear_layout);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    private void attemptPlaceOrder() {
        // Reset errors.
        placeTextInputLayout.setError(null);
        // Store values at the time of the login attempt.
        final String place = placeEditText.getText().toString();
        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(place)) {
            placeTextInputLayout.setError("Required");
            focusView = placeEditText;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle(getString(R.string.title));
            alertDialog.setMessage(getString(R.string.messsage));
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.popositive),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                           processOrders(place);
                        }
                    });
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.ponegative),
                    new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            alertDialog.show();
        }
    }

    private void processOrders(String place) {
        loading();

        List<Food> store1Orders = new ArrayList<>();
        List<Food> store2Orders = new ArrayList<>();
        List<Food> store3Orders = new ArrayList<>();
        DatabaseReference storeRef;

        Order order = new Order();
        order.setCustomerName(customerName);
        order.setRefNo("");
        order.setItems(null);
        order.setAmount(total);
        order.setLocation(place);
        order.setStatus("PROCESSING");
        order.setStore("");
        order.setDatetime(DateFormat.getDateTimeInstance().format(new Date()));

        for (Food food : orderList) {
            if (food.getStore().equalsIgnoreCase("store1")) {
                store1Orders.add(food);
            } else  if (food.getStore().equalsIgnoreCase("store2")) {
                store2Orders.add(food);
            } else  if (food.getStore().equalsIgnoreCase("store3")) {
                store3Orders.add(food);
            }
        }

        if (!store1Orders.isEmpty()) {
            double total = 0;
            for (Food food : store1Orders) { total += food.getAmount() * food.getPrice(); }
            storeRef = mDatabase.child("storeorders").push();
            order.setItems(store1Orders);
            order.setStore("store1");
            order.setRefNo(storeRef.getKey());
            order.setAmount(total);
            storeRef.setValue(order);
        }
        if (!store2Orders.isEmpty()) {
            double total = 0;
            for (Food food : store2Orders) { total += food.getAmount() * food.getPrice(); }
            storeRef = mDatabase.child("storeorders").push();
            order.setItems(store2Orders);
            order.setStore("store2");
            order.setRefNo(storeRef.getKey());
            order.setAmount(total);
            storeRef.setValue(order);
        }
        if (!store3Orders.isEmpty()) {
            double total = 0;
            for (Food food : store3Orders) { total += food.getAmount() * food.getPrice(); }
            storeRef = mDatabase.child("storeorders").push();
            order.setItems(store3Orders);
            order.setStore("store3");
            order.setRefNo(storeRef.getKey());
            order.setAmount(total);
            storeRef.setValue(order);
        }

        unload();
    }

    private void loading() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        linearLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void unload() {
        new CountDownTimer(1000, 100) {
            public void onTick(long millisUntilFinished) { }

            public void onFinish() {
                linearLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Intent resultIntent = new Intent();
                setResult(1, resultIntent);
                Toast.makeText(getApplicationContext(), "Order Placed", Toast.LENGTH_SHORT).show();
                finish();
            }
        }.start();
    }
}
