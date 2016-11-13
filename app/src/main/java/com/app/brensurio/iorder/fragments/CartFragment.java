package com.app.brensurio.iorder.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.brensurio.iorder.R;
import com.app.brensurio.iorder.adapters.CustomerCartItemAdapter;
import com.app.brensurio.iorder.interfaces.StoreFragmentListener;
import com.app.brensurio.iorder.models.Food;
import com.app.brensurio.iorder.models.Order;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {

    protected RecyclerView mRecyclerView;

    private StoreFragmentListener storeFragmentCallback;
    private EditText destinationEditText;
    private DatabaseReference mDatabase;

    public CartFragment() { } // Required empty public constructor

    /**
     * Called when a fragment is first attached to its context.
     * {@link #onCreate(Bundle)} will be called after this.
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        storeFragmentCallback = (StoreFragmentListener) context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cart, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        destinationEditText = (EditText) rootView.findViewById(R.id.destination_editText);
        final TextView totalAmountTextView = (TextView) rootView.findViewById(R.id.amount_text_view);
        totalAmountTextView.setText(String.valueOf(getTotalAmount()));
        Button placeOrderButton = (Button) rootView.findViewById(R.id.place_order_button);
        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeOrder();
                getActivity().getFragmentManager().popBackStack();
            }
        });

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.customer_food_recycler);

        CustomerCartItemAdapter customerCartItemAdapter =
                new CustomerCartItemAdapter(storeFragmentCallback.getOrderList());
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getActivity());

        customerCartItemAdapter.setListener(new CustomerCartItemAdapter.Listener() {
            @Override
            public void onClickAddAmount(int position) {
                int i = storeFragmentCallback.getOrderList().get(position).getAmount() + 1;
                storeFragmentCallback.getOrderList().get(position).setAmount(i);
                totalAmountTextView.setText(String.valueOf(getTotalAmount()));
            }

            @Override
            public void onClickDeleteItem(int position) {
                storeFragmentCallback.getOrderList().remove(position);
                totalAmountTextView.setText(String.valueOf(getTotalAmount()));
            }

            @Override
            public void onClickSubtractAmount(int position) {
                int i = storeFragmentCallback.getOrderList().get(position).getAmount();
                if (i != 1) {
                    storeFragmentCallback.getOrderList().get(position).setAmount(i - 1);
                    totalAmountTextView.setText(String.valueOf(getTotalAmount()));
                }
            }
        });

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(customerCartItemAdapter);
        customerCartItemAdapter.notifyDataSetChanged();

        return rootView;
    }

    private void placeOrder() {
        if (!TextUtils.isEmpty(destinationEditText.getText().toString())) {
            List<Food> store1Order = new ArrayList<>();
            List<Food> store2Order = new ArrayList<>();
            List<Food> store3Order = new ArrayList<>();
            List<Food> orderList = storeFragmentCallback.getOrderList();

            for (Food food : orderList) {
                if (food.getStore().equalsIgnoreCase("store1"))
                    store1Order.add(food);
                else if (food.getStore().equalsIgnoreCase("store2"))
                    store2Order.add(food);
                else if (food.getStore().equalsIgnoreCase("store3"))
                    store3Order.add(food);
            }

            if (!store1Order.isEmpty()) {
                Order order = processOrder(store1Order);
                order.setStore("store1");
                String refno = mDatabase.child("storeorders").push().getKey();
                order.setRefNo(refno);
                mDatabase.child("storeorders").child(refno).setValue(order);
            }
            if (!store2Order.isEmpty()) {
                Order order = processOrder(store2Order);
                order.setStore("store2");
                String refno = mDatabase.child("storeorders").push().getKey();
                order.setRefNo(refno);
                mDatabase.child("storeorders").child(refno).setValue(order);
            }
            if (!store3Order.isEmpty()) {
                Order order = processOrder(store2Order);
                order.setStore("store3");
                String refno = mDatabase.child("storeorders").push().getKey();
                order.setRefNo(refno);
                mDatabase.child("storeorders").child(refno).setValue(order);
            }
        }
    }

    private Order processOrder(List<Food> foodList) {
        Order order;

        String customerName = storeFragmentCallback.getCustomerName();
        String refNo = "";
        List<String> items = new ArrayList<>();
        String amount = "";
        String location = destinationEditText.getText().toString();
        String status = "PROCESSING";

        double totalAmount = 0;
        for (Food food : foodList) {
            items.add(food.getName());
            totalAmount += (Double.parseDouble(food.getPrice()) * food.getAmount());
        }

        order = new Order(customerName, refNo, items,
                String.valueOf(totalAmount), location, status);

        return order;
    }

    private double getTotalAmount() {
        double totalAmount = 0;
        if (!storeFragmentCallback.getOrderList().isEmpty()) {
            for (Food food : storeFragmentCallback.getOrderList()) {
                totalAmount += (Double.parseDouble(food.getPrice()) * food.getAmount());
            }
        }

        return totalAmount;
    }

}
