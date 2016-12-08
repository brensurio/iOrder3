package com.app.brensurio.iorder.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.app.brensurio.iorder.R;
import com.app.brensurio.iorder.models.Food;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewSellerFoodItemAdapter extends
        RecyclerView.Adapter<NewSellerFoodItemAdapter.ViewHolder> {

    private List<Food> foodList;
    private Listener listener;

    public interface Listener {
        void onUpdate(int position);
        void onAvailable(int position);
        void onUnAvailable(int position);
    }

    // Provide reference to the views used in the recycler view
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    public NewSellerFoodItemAdapter(List<Food> foodList) {
        this.foodList = foodList;
    }

    @Override
    public NewSellerFoodItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_new_seller_food_item, parent, false);

        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        CardView cardView = holder.cardView;

        TextView foodNameTextView = (TextView) cardView.findViewById(R.id.food_name_text_view);
        foodNameTextView.setText(foodList.get(position).getName());

        TextView foodPriceTextView = (TextView) cardView.findViewById(R.id.food_price_text_view);
        foodPriceTextView.setText(Double.toString(foodList.get(position).getPrice()));

        ImageView foodImageView = (ImageView) cardView.findViewById(R.id.food_image_view);
        Picasso.with(foodImageView.getContext()).load(foodList.get(position).getImageLink())
                .into(foodImageView);

        Button updateButton = (Button) cardView.findViewById(R.id.update_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onUpdate(holder.getAdapterPosition());
            }
        });

        CheckBox isAvailSwitch = (CheckBox) cardView.findViewById(R.id.checkBox);
        String storeName = foodList.get(position).getStore();
        if (storeName.charAt((storeName.length() - 1)) == 'u') {
            isAvailSwitch.setChecked(false);
        } else {
            isAvailSwitch.setChecked(true);
        }

        isAvailSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    listener.onAvailable(holder.getAdapterPosition());
                } else {
                    listener.onUnAvailable(holder.getAdapterPosition());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
