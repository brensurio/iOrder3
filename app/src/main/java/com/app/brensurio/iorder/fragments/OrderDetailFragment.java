package com.app.brensurio.iorder.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.brensurio.iorder.R;
import com.app.brensurio.iorder.adapters.OrderDetailAdapter;
import com.app.brensurio.iorder.models.Order;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderDetailFragment extends Fragment {

    Order order;

    public OrderDetailFragment() { } // Required empty public constructor

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_order_detail, container, false);

        order = getArguments().getParcelable("order");

        TextView nameTextView = (TextView) rootView.findViewById(R.id.name_text_view);
        nameTextView.setText(order.getCustomerName());

        TextView statusTextView = (TextView) rootView.findViewById(R.id.status_text_view);
        statusTextView.setText(order.getStatus());

        TextView dateTimeTextView = (TextView) rootView.findViewById(R.id.datetime_text_view);
        dateTimeTextView.setText(order.getDatetime());

        TextView placeTextView = (TextView) rootView.findViewById(R.id.place_text_view);
        placeTextView.setText(order.getLocation());

        TextView orderIdTextView = (TextView) rootView.findViewById(R.id.order_id_text_view);
        orderIdTextView.setText(order.getRefNo().substring(6, 12).toUpperCase());

        TextView priceTextView = (TextView) rootView.findViewById(R.id.price_text_view);
        priceTextView.setText(Double.toString(order.getAmount()));

        String store = order.getStore();
        String storeName = "";
        if (store.equalsIgnoreCase("store1"))
            storeName = "Scoops";
        if (store.equalsIgnoreCase("store2"))
            storeName = "Nanay's Cuisine";
        if (store.equalsIgnoreCase("store3"))
            storeName = "Ovenmade";
        TextView storeTextView = (TextView) rootView.findViewById(R.id.store_text_view);
        storeTextView.setText(storeName);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        OrderDetailAdapter orderDetailAdapter = new OrderDetailAdapter(order.getItems());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setAdapter(orderDetailAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        return rootView;
    }
}