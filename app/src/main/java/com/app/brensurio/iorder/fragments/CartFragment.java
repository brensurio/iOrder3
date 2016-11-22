package com.app.brensurio.iorder.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.app.brensurio.iorder.R;
import com.app.brensurio.iorder.activities.CheckOutActivity;
import com.app.brensurio.iorder.adapters.CustomerCartItemAdapter;
import com.app.brensurio.iorder.interfaces.StoreFragmentListener;
import com.app.brensurio.iorder.models.Food;
import com.app.brensurio.iorder.ui.CustomRecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {

    protected CustomRecyclerView mRecyclerView;
    private StoreFragmentListener storeFragmentCallback;
    Button placeOrderButton;
    TextView totalAmountTextView;

    CustomerCartItemAdapter customerCartItemAdapter;

    public CartFragment() { } // Required empty public constructor

    @Override
    public void onAttach(Context context) {
        storeFragmentCallback = (StoreFragmentListener) context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cart, container, false);

        totalAmountTextView = (TextView) rootView.findViewById(R.id.amount_text_view);
        totalAmountTextView.setText(String.valueOf(getTotalAmount()));

        placeOrderButton = (Button) rootView.findViewById(R.id.place_order_button);
        checkOrderList();
        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CheckOutActivity.class);
                intent.putExtra("customerName", storeFragmentCallback.getCustomerName());
                intent.putParcelableArrayListExtra("orderList",
                        storeFragmentCallback.getOrderList());
                startActivityForResult(intent, 1);
            }
        });

        mRecyclerView = (CustomRecyclerView) rootView.findViewById(R.id.customer_food_recycler);

        customerCartItemAdapter =
                new CustomerCartItemAdapter(storeFragmentCallback.getOrderList(), getActivity());
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
            public void onClickDeleteItem(final int position) {
                storeFragmentCallback.getOrderList().remove(position);
                totalAmountTextView.setText(String.valueOf(getTotalAmount()));
                customerCartItemAdapter.notifyDataSetChanged();
                checkOrderList();
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
        mRecyclerView.setEmptyView(rootView.findViewById(R.id.textView4));
        mRecyclerView.setAdapter(customerCartItemAdapter);

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 1) {
                storeFragmentCallback.getOrderList().clear();
                customerCartItemAdapter.notifyDataSetChanged();
                totalAmountTextView.setText(String.valueOf(getTotalAmount()));
            }
        }
    }

    private void checkOrderList() {
        if (storeFragmentCallback.getOrderList().isEmpty()){
            placeOrderButton.setEnabled(false);
        } else {
            placeOrderButton.setEnabled(true);
        }
    }


    private double getTotalAmount() {
        double totalAmount = 0;
        if (!storeFragmentCallback.getOrderList().isEmpty()) {
            for (Food food : storeFragmentCallback.getOrderList()) {
                totalAmount += (food.getPrice() * food.getAmount());
            }
        }

        return totalAmount;
    }
}
