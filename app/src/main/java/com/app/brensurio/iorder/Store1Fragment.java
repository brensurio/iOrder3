package com.app.brensurio.iorder;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
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
public class Store1Fragment extends Fragment {

    private DatabaseReference mDatabase;
    private String name;
    private String userName;
    private List<Food> foodList;
    private ArrayList<Food> orderList;
    private Store1FragmentListener store1FragmentCallback;

    interface Store1FragmentListener {
        ArrayList<Food> getOrderList();
        void addToOrderList(Food food);
    }

    public Store1Fragment() {
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
        store1FragmentCallback = (Store1FragmentListener) context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final RecyclerView foodRecycler = (RecyclerView)
                inflater.inflate(R.layout.fragment_seller_food_list, container, false);

        foodList = new ArrayList<>();
        orderList = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabase.child("store1foodlist");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
             userName = user.getDisplayName();
        }

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    Food food = singleSnapshot.getValue(Food.class);
                    foodList.add(food);
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        for (UserInfo profile : user.getProviderData()) {
                            name = profile.getDisplayName();
                        }
                    }
                    CustomerFoodListAdapter foodListAdapter =
                            new CustomerFoodListAdapter(foodList, 1, name);
                    LinearLayoutManager linearLayoutManager =
                            new LinearLayoutManager(getActivity());
                    foodRecycler.setLayoutManager(linearLayoutManager);
                    foodRecycler.setAdapter(foodListAdapter);

                    foodListAdapter.setListener(new CustomerFoodListAdapter.Listener() {
                        @Override
                        public void onClick(int position) {
                            Food food = foodList.get(position);
                            if (store1FragmentCallback.getOrderList().contains(food)) {
                                Toast.makeText(getActivity(), "Item is in cart already",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                store1FragmentCallback.addToOrderList(food);
                                Toast.makeText(getActivity(), "Added to cart",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        return foodRecycler;
    }

    public ArrayList<Food> getOrderList() {
        return this.orderList;
    }
}
