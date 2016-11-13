package com.app.brensurio.iorder.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.brensurio.iorder.R;
import com.app.brensurio.iorder.adapters.CustomerOrderAdapter;
import com.app.brensurio.iorder.interfaces.SellerFragmentListener;
import com.app.brensurio.iorder.models.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellerHistoryFragment extends Fragment {

    private List<Order> orderList;
    private DatabaseReference mDatabase;
    private SellerFragmentListener sellerFragmentCallback;

    public SellerHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        sellerFragmentCallback = (SellerFragmentListener) context;
        super.onAttach(context);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_seller_history, container, false);
        final RecyclerView mRecyclerView = (RecyclerView)
                rootView.findViewById(R.id.seller_history_recycler);

        orderList = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabase.child("storeorders").orderByChild("store")
                .equalTo(sellerFragmentCallback.getStoreName());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderList.clear();
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Order order = singleSnapshot.getValue(Order.class);
                    if (!order.getStatus().equalsIgnoreCase("PROCESSING"))
                        orderList.add(order);
                }

                CustomerOrderAdapter customerOrderAdapter =
                        new CustomerOrderAdapter(getActivity(), orderList);
                LinearLayoutManager linearLayoutManager =
                        new LinearLayoutManager(getActivity());

                mRecyclerView.setLayoutManager(linearLayoutManager);
                mRecyclerView.setAdapter(customerOrderAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;
    }

}
