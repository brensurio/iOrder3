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

import com.app.brensurio.iorder.R;
import com.app.brensurio.iorder.adapters.CustomerCartItemAdapter;
import com.app.brensurio.iorder.interfaces.StoreFragmentListener;
import com.app.brensurio.iorder.models.Food;
import com.app.brensurio.iorder.models.Order;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {

    protected RecyclerView mRecyclerView;

    private StoreFragmentListener storeFragmentCallback;
    private EditText destinationEditText;
    private DatabaseReference mDatabase;

    public OrderFragment() { } // Required empty public constructor

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
        return inflater.inflate(R.layout.fragment_order, container, false);
    }


}