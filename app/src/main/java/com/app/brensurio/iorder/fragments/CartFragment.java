package com.app.brensurio.iorder.fragments;


import android.content.Context;
import android.content.Intent;
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
import com.app.brensurio.iorder.activities.CheckOutActivity;
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

        final TextView totalAmountTextView = (TextView) rootView.findViewById(R.id.amount_text_view);
        totalAmountTextView.setText(String.valueOf(getTotalAmount()));
        Button placeOrderButton = (Button) rootView.findViewById(R.id.place_order_button);
        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CheckOutActivity.class);
                startActivity(intent);
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
