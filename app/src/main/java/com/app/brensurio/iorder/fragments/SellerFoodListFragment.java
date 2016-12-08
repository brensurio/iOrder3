package com.app.brensurio.iorder.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.brensurio.iorder.adapters.NewSellerFoodItemAdapter;
import com.app.brensurio.iorder.interfaces.SellerFragmentListener;
import com.app.brensurio.iorder.models.Food;
import com.app.brensurio.iorder.adapters.FoodListAdapter;
import com.app.brensurio.iorder.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SellerFoodListFragment extends Fragment {

    private SellerFragmentListener sellerFragmentCallback;
    private DatabaseReference mDatabase;
    private List<Food> foodList;
    private String storeName;

    public SellerFoodListFragment() {
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
        final RecyclerView foodRecycler = (RecyclerView)
                inflater.inflate(R.layout.recycler_view, container, false);

        foodList = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabase.child(sellerFragmentCallback.getStoreName().concat("foodlist"));

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                foodList.clear();
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    Food food = singleSnapshot.getValue(Food.class);
                    foodList.add(food);
                }
                final NewSellerFoodItemAdapter foodListAdapter = new NewSellerFoodItemAdapter(foodList, getActivity());
                LinearLayoutManager linearLayoutManager =
                        new LinearLayoutManager(getActivity());
                foodListAdapter.setListener(new NewSellerFoodItemAdapter.Listener() {

                    @Override
                    public void onDelete(final int position) {
                        Query query1 = mDatabase.child(sellerFragmentCallback.getStoreName().concat("foodlist"));
                        query1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (final DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                    Food foodItem = singleSnapshot.getValue(Food.class);
                                    if (foodItem.getName().equalsIgnoreCase(foodList.get(position).getName())) {
                                        FirebaseStorage storage = FirebaseStorage.getInstance();
                                        StorageReference storageRef = storage.getReferenceFromUrl("gs://iorder-72aca.appspot.com");
                                        StorageReference imageRef = storageRef.child(cutLastLetter(foodItem.getStore()).concat("/") +
                                                foodItem.getName().toLowerCase().concat(".jpg"));
                                        // Delete the file
                                        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                foodListAdapter.notifyDataSetChanged();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                                // Uh-oh, an error occurred!
                                            }
                                        });

                                        singleSnapshot.getRef().removeValue();
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) { }
                        });
                        foodListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onAvailable(final int position) {
                        Query query1 = mDatabase.child(sellerFragmentCallback.getStoreName().concat("foodlist"));
                        query1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                    Food foodItem = singleSnapshot.getValue(Food.class);
                                    if (foodItem.getName().equalsIgnoreCase(foodList.get(position).getName())) {
                                        singleSnapshot.getRef().child("store").setValue(cutLastLetter(foodItem.getStore()));
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) { }
                        });
                    }

                    @Override
                    public void onUnAvailable(final int position) {
                        Query query1 = mDatabase.child(sellerFragmentCallback.getStoreName().concat("foodlist"));
                        query1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                    Food foodItem = singleSnapshot.getValue(Food.class);
                                    if (foodItem.getName().equalsIgnoreCase(foodList.get(position).getName())) {
                                        singleSnapshot.getRef().child("store").setValue(foodItem.getStore() + "u");
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) { }
                        });
                    }
                });
                foodRecycler.setLayoutManager(linearLayoutManager);
                foodRecycler.setAdapter(foodListAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        return foodRecycler;
    }

    private String cutLastLetter(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == 'u') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }
}
