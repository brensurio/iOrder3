package com.app.brensurio.iorder;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
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
                    Toast.makeText(getActivity(), food.getName(),
                            Toast.LENGTH_SHORT).show();
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

       /* mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    Food food = singleSnapshot.getValue(Food.class);
                    foodList.add(food);
                    FoodListAdapter foodListAdapter = new FoodListAdapter(foodList);
                    foodRecycler.setAdapter(foodListAdapter);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    Food food = singleSnapshot.getValue(Food.class);
                    foodList.add(food);
                    FoodListAdapter foodListAdapter = new FoodListAdapter(foodList);
                    foodRecycler.setAdapter(foodListAdapter);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        return foodRecycler;
    }

    /*private void getFoodTask(DataSnapshot dataSnapshot) {
        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
            Food food = singleSnapshot.getValue(Food.class);
            Toast.makeText(getActivity(), food.getName(),
                    Toast.LENGTH_SHORT).show();
            foodList.add(food);
            FoodListAdapter foodListAdapter = new FoodListAdapter(foodList);
            LinearLayoutManager linearLayoutManager =
                    new LinearLayoutManager(getActivity());
            foodRecycler.setLayoutManager(linearLayoutManager);
            foodRecycler.setAdapter(foodListAdapter);
        }
    }*/

    /*private void taskDeletion(DataSnapshot dataSnapshot){
        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
            String taskTitle = singleSnapshot.getValue(String.class);
            for(int i = 0; i < allTask.size(); i++){
                if(allTask.get(i).getTask().equals(taskTitle)){
                    allTask.remove(i);
                }
            }
            Log.d(TAG, "Task tile " + taskTitle);
            recyclerViewAdapter.notifyDataSetChanged();
            recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, allTask);
            recyclerView.setAdapter(recyclerViewAdapter);
        }
    }*/

    public void setStoreName(String name) {
        this.storeName = name;
    }

}
