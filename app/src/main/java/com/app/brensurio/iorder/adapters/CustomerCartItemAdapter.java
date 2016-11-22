package com.app.brensurio.iorder.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.brensurio.iorder.R;
import com.app.brensurio.iorder.models.Food;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomerCartItemAdapter extends
        RecyclerView.Adapter<CustomerCartItemAdapter.ViewHolder> {

    private List<Food> food;
    private Listener listener;
    private Context context;

    public interface Listener {
        void onClickAddAmount(int position);
        void onClickSubtractAmount(int position);
        void onClickDeleteItem(int position);
    }

    // Provide reference to the views used in the recycler view
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    public CustomerCartItemAdapter(List<Food> food, Context context) {
        this.food = food;
        this.context = context;
    }

    @Override
    public CustomerCartItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_customer_cart_item, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        CardView cardView = holder.cardView;

        TextView foodNameTextView = (TextView) cardView.findViewById(R.id.food_name_text_view);
        foodNameTextView.setText(food.get(position).getName());
        TextView foodPriceTextView = (TextView) cardView.findViewById(R.id.food_price_text_view);
        foodPriceTextView.setText("P " + food.get(position).getPrice());
        ImageView foodImageView = (ImageView) cardView.findViewById(R.id.food_image);
        Picasso.with(foodImageView.getContext()).load(food.get(position).getImageLink())
                .into(foodImageView);

        TextView amountTextView = (TextView) cardView.findViewById(R.id.amount_text_view);
        amountTextView.setText(String.valueOf(food.get(position).getAmount()));

        ImageButton deleteButton = (ImageButton) cardView.findViewById(R.id.delete_item_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
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
                                    listener.onClickDeleteItem(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, getItemCount());
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
        ImageButton decreaseAmountButton = (ImageButton) cardView.findViewById(R.id.decrease_amount_button);
        decreaseAmountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClickSubtractAmount(position);
                    notifyItemChanged(position);
                }
            }
        });
        ImageButton increaseAmountButton = (ImageButton) cardView.findViewById(R.id.increase_amount_button);
        increaseAmountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClickAddAmount(position);
                    notifyItemChanged(position);
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
