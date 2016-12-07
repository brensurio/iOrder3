package com.app.brensurio.iorder.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.brensurio.iorder.models.Food;
import com.app.brensurio.iorder.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ViewHolder> {

    private List<Food> food;

    // Provide reference to the views used in the recycler view
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    public FoodListAdapter(List<Food> food) {
        this.food = food;
    }

    @Override
    public FoodListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_food_item, parent, false);

        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        CardView cardView = holder.cardView;

        TextView foodNameTextView = (TextView) cardView.findViewById(R.id.food_name_text_view);
        foodNameTextView.setText(food.get(position).getName());

        TextView foodPriceTextView = (TextView) cardView.findViewById(R.id.food_price_text_view);
        foodPriceTextView.setText(Double.toString(food.get(position).getPrice()));

        TextView foodDescTextView = (TextView) cardView.findViewById(R.id.food_desc_text_view);
        foodDescTextView.setText(food.get(position).getDescription());

        ImageView foodImageView = (ImageView) cardView.findViewById(R.id.food_image_view);
        Picasso.with(foodImageView.getContext()).load(food.get(position).getImageLink())
                .into(foodImageView);



    }

    @Override
    public int getItemCount() {
        return food.size();
    }
}