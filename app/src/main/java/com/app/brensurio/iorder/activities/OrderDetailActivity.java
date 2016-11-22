package com.app.brensurio.iorder.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.app.brensurio.iorder.R;
import com.app.brensurio.iorder.adapters.OrderDetailAdapter;
import com.app.brensurio.iorder.models.Order;

public class OrderDetailActivity extends AppCompatActivity {

    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        order = getIntent().getParcelableExtra("order");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        getSupportActionBar().setTitle("Transaction details");

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.order_detail_list);
        OrderDetailAdapter orderDetailAdapter = new OrderDetailAdapter(order.getItems());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(orderDetailAdapter);

        TextView orderIdTextView = (TextView) findViewById(R.id.order_id_text_view);
        orderIdTextView.setText("Order ID: " + order.getRefNo().substring(5, 11));

        TextView dateTextView = (TextView) findViewById(R.id.date_text_view);
        dateTextView.setText(order.getDatetime());

        TextView nameTextView = (TextView) findViewById(R.id.name_text_view);
        nameTextView.setText(order.getCustomerName());

        TextView totalAmountTextView = (TextView) findViewById(R.id.total_amount_text_view);
        totalAmountTextView.setText(Double.toString(order.getAmount()));

        TextView locationTextView = (TextView) findViewById(R.id.location_text_view);
        locationTextView.setText(order.getLocation());

        TextView statusTextView = (TextView) findViewById(R.id.status_text_view);
        statusTextView.setText(order.getStatus());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}
