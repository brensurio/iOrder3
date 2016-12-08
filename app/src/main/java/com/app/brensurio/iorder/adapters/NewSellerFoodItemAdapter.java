package com.app.brensurio.iorder.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
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
    private Context context;

    public interface Listener {
        void onDelete(int position);
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

    public NewSellerFoodItemAdapter(List<Food> foodList, Context context) {
        this.foodList = foodList;
        this.context = context;
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

        Button deletebutton = (Button) cardView.findViewById(R.id.button5);
        deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setMessage(context.getString(R.string.delete_message));
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                            context.getResources().getString(R.string.positive),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    listener.onDelete(holder.getAdapterPosition());
                                    notifyItemRemoved(holder.getAdapterPosition());
                                    notifyItemRangeChanged(holder.getAdapterPosition(), getItemCount());
                                }
                            });
                    alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                            context.getString(R.string.negative),
                            new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    alertDialog.show();
                }
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
