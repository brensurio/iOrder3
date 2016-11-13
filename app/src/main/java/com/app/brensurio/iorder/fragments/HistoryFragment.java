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
import com.app.brensurio.iorder.interfaces.StoreFragmentListener;
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
public class HistoryFragment extends Fragment {

    private List<Order> orderList;
    private DatabaseReference mDatabase;
    private StoreFragmentListener storeFragmentCallback;

    public HistoryFragment() {
        // Required empty public constructor
    }

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
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        final RecyclerView mRecyclerView = (RecyclerView)
                rootView.findViewById(R.id.customer_history_recycler);

        orderList = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabase.child("storeorders").orderByChild("customerName")
                .equalTo(storeFragmentCallback.getCustomerName());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderList.clear();
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Order order = singleSnapshot.getValue(Order.class);
                    if (order.getStatus().equalsIgnoreCase("CONFIRMED"))
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
