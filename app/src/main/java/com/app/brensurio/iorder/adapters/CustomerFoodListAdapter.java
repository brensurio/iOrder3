package com.app.brensurio.iorder.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.app.brensurio.iorder.models.Food;
import com.app.brensurio.iorder.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomerFoodListAdapter extends
        RecyclerView.Adapter<CustomerFoodListAdapter.ViewHolder> {

    private List<Food> food;
    private String user;
    private int storeNum;
    private Listener listener;

    public interface Listener {
        void onClick(int position);
    }

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
    public void onBindViewHolder(CustomerFoodListAdapter.ViewHolder holder, final int position) {

        CardView cardView = holder.cardView;

        TextView foodNameTextView = (TextView) cardView.findViewById(R.id.food_name_text_view);
        foodNameTextView.setText(food.get(position).getName());
        TextView foodPriceTextView = (TextView) cardView.findViewById(R.id.food_price_text_view);
        foodPriceTextView.setText("P " + food.get(position).getPrice());
        TextView foodDescTextView = (TextView) cardView.findViewById(R.id.food_desc_text_view);
        foodDescTextView.setText(food.get(position).getDescription());
        ImageView foodImageView = (ImageView) cardView.findViewById(R.id.food_image_view);
        Picasso.with(foodImageView.getContext()).load(food.get(position).getImageLink())
                .into(foodImageView);
        Button addToCartButton = (Button) cardView.findViewById(R.id.add_to_cart_button);

        addToCartButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return food.size();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }
}