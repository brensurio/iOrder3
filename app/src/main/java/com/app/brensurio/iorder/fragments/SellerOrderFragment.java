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
import com.app.brensurio.iorder.adapters.SellerOrderAdapter;
import com.app.brensurio.iorder.interfaces.SellerFragmentListener;
import com.app.brensurio.iorder.models.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellerOrderFragment extends Fragment {

    private List<Order> orderList;
    private DatabaseReference mDatabase;
    private SellerFragmentListener sellerFragmentListener;


    public SellerOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        sellerFragmentListener = (SellerFragmentListener) context;
        super.onAttach(context);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_seller_order, container, false);
        final RecyclerView mRecyclerView = (RecyclerView)
                rootView.findViewById(R.id.seller_order_recycler);

        orderList = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabase.child("storeorders").orderByChild("store")
                .equalTo(sellerFragmentListener.getStoreName());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderList.clear();
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Order order = singleSnapshot.getValue(Order.class);
                    if (order.getStatus().equalsIgnoreCase("PROCESSING"))
                        orderList.add(order);
                }

                SellerOrderAdapter sellerOrderAdapter =
                        new SellerOrderAdapter(getActivity(), orderList);
                LinearLayoutManager linearLayoutManager =
                        new LinearLayoutManager(getActivity());
                sellerOrderAdapter.setListener(new SellerOrderAdapter.Listener() {
                    @Override
                    public void onViewOrder(int position) {

                        // location | datetoday | orderid | payment | change | qty1 | food1 | subtotal1 | qty2 | food2 | subtotal2 | qtyN | foodN | subtotalN

                        /*String data;

                        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy hh:mm:ss");
                        Date date = new Date();
                        String dateToday = dateFormat.format(date);
                        String refno = orderList.get(position).getRefNo().substring(5, 11);
                        String location = orderList.get(position).getLocation();

                        data = location + "|" + dateToday + "|" + refno + "|" + "1"+ "|" + "1" + "|";

                        for (String string : orderList.get(position).getItems()) {
                            data += "1" + "|" + string + "1";
                        }

                        sellerFragmentListener.setString(data);
                        sellerFragmentListener.transmitData();

                        DatabaseReference databaseReference =
                                mDatabase.child("storeorders")
                                        .child(orderList.get(position).getRefNo());
                        Map<String, Object> statusUpdate = new HashMap<>();
                        statusUpdate.put("status", "CONFIRMED");
                        databaseReference.updateChildren(statusUpdate);*/

                    }

                    @Override
                    public void onClickDeleteItem(int position) {
                        DatabaseReference databaseReference =
                                mDatabase.child("storeorders")
                                        .child(orderList.get(position).getRefNo());
                        Map<String, Object> statusUpdate = new HashMap<>();
                        statusUpdate.put("status", "CANCELLED");
                        databaseReference.updateChildren(statusUpdate);
                    }
                });

                mRecyclerView.setLayoutManager(linearLayoutManager);
                mRecyclerView.setAdapter(sellerOrderAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;
    }

}