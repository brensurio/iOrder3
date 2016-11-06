package com.app.brensurio.iorder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Mariz L. Maas on 11/3/2016.
 */

class CustomerFoodListAdapter extends RecyclerView.Adapter<CustomerFoodListAdapter.ViewHolder> {

    private List<Food> food;
    private String user;
    private int storeNum;

    // Provide reference to the views used in the recycler view
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    public CustomerFoodListAdapter(List<Food> food, int storeNum, String user) {
        this.food = food;
        this.storeNum = storeNum;
        this.user = user;
    }

    @Override
    public CustomerFoodListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_food_item, parent, false);

        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(CustomerFoodListAdapter.ViewHolder holder, int position) {

        CardView cardView = holder.cardView;

        Button button = (Button) cardView.findViewById(R.id.delete_button);
        cardView.removeView(button);

        TextView foodNameTextView = (TextView) cardView.findViewById(R.id.food_name_text_view);
        foodNameTextView.setText(food.get(position).getName());

        TextView foodPriceTextView = (TextView) cardView.findViewById(R.id.food_price_text_view);
        foodPriceTextView.setText(food.get(position).getPrice());

        TextView foodDescTextView = (TextView) cardView.findViewById(R.id.food_desc_text_view);
        foodDescTextView.setText(food.get(position).getDescription());

        ImageView foodImageView = (ImageView) cardView.findViewById(R.id.food_image_view);
        Picasso.with(foodImageView.getContext()).load(food.get(position).getImageLink())
                .into(foodImageView);

        Button addToCartButton = (Button) cardView.findViewById(R.id.update_button);
        addToCartButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                
            }
        });
    }

    @Override
    public int getItemCount() {
        return food.size();
    }

    public Food getFood(int position) {
        return food.get(position);
    }
}