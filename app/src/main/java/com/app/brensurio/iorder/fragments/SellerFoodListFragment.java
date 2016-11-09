package com.app.brensurio.iorder.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.brensurio.iorder.models.Food;
import com.app.brensurio.iorder.FoodListAdapter;
import com.app.brensurio.iorder.R;
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
public class SellerFoodListFragment extends Fragment {

    private DatabaseReference mDatabase;
    private List<Food> foodList;
    private String storeName;

    public SellerFoodListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final RecyclerView foodRecycler = (RecyclerView)
                inflater.inflate(R.layout.fragment_seller_food_list, container, false);

        foodList = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabase.child(storeName.concat("foodlist"));

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    Food food = singleSnapshot.getValue(Food.class);
                    foodList.add(food);
                    FoodListAdapter foodListAdapter = new FoodListAdapter(foodList);
                    LinearLayoutManager linearLayoutManager =
                            new LinearLayoutManager(getActivity());
                    foodRecycler.setLayoutManager(linearLayoutManager);
                    foodRecycler.setAdapter(foodListAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return foodRecycler;
    }

    public void setStoreName(String name) {
        this.storeName = name;
    }

}
