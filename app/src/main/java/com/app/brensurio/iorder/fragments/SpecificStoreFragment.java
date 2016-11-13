package com.app.brensurio.iorder.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.brensurio.iorder.adapters.CustomerFoodListAdapter;
import com.app.brensurio.iorder.interfaces.StoreFragmentListener;
import com.app.brensurio.iorder.models.Food;
import com.app.brensurio.iorder.R;
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
public class SpecificStoreFragment extends Fragment {

    private DatabaseReference mDatabase;
    private String name;
    private String userName;
    private List<Food> foodList;
    private ArrayList<Food> orderList;
    private StoreFragmentListener storeFragmentCallback;

    public SpecificStoreFragment() { }// Required empty public constructor

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
        final RecyclerView foodRecycler = (RecyclerView)
                inflater.inflate(R.layout.recycler_view, container, false);

        foodList = new ArrayList<>();
        orderList = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        int storeNumber = getArguments().getInt("store_id");
        Query query = mDatabase.child("store1foodlist");
        if (storeNumber == 1)
            query = mDatabase.child("store1foodlist");
        else if (storeNumber == 2)
            query = mDatabase.child("store2foodlist");
        else if (storeNumber == 3)
            query = mDatabase.child("store3foodlist");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
             userName = user.getDisplayName();
        }

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                foodList.clear();
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    Food food = singleSnapshot.getValue(Food.class);
                    foodList.add(food);
                }

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    for (UserInfo profile : user.getProviderData()) {
                        name = profile.getDisplayName();
                    }
                }
                CustomerFoodListAdapter foodListAdapter =
                        new CustomerFoodListAdapter(foodList,
                                getArguments().getInt("store_id"), name);
                LinearLayoutManager linearLayoutManager =
                        new LinearLayoutManager(getActivity());
                foodListAdapter.setListener(new CustomerFoodListAdapter.Listener() {
                    @Override
                    public void onClick(int position) {
                        Food food = foodList.get(position);
                        if (storeFragmentCallback.getOrderList().contains(food)) {
                            Toast.makeText(getActivity(), "Item is in cart already",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            storeFragmentCallback.addToOrderList(food);
                            Toast.makeText(getActivity(), "Added to cart",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                foodRecycler.setLayoutManager(linearLayoutManager);
                foodRecycler.setAdapter(foodListAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        return foodRecycler;
    }
}
