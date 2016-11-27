package com.app.brensurio.iorder.adapters;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.brensurio.iorder.R;
import com.app.brensurio.iorder.activities.SellerOrderDetailActivity;
import com.app.brensurio.iorder.models.Order;

import java.util.List;

public class SellerOrderAdapter extends
        RecyclerView.Adapter<SellerOrderAdapter.ViewHolder> {

    private List<Order> orders;
    private Context context;
    private Listener listener;

    public interface Listener {
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

    public SellerOrderAdapter(Context context, List<Order> orders) {
        this.orders = orders;
        this.context = context;
    }

    @Override
    public SellerOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_seller_order, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        CardView cardView = holder.cardView;

        TextView refTextView = (TextView) cardView.findViewById(R.id.ref_text_view);
        refTextView.setText("Order ID: " + orders.get(position).getRefNo().substring(6, 12));

        TextView dateTextview = (TextView) cardView.findViewById(R.id.date_text_view);
        dateTextview.setText(orders.get(position).getDatetime());

        TextView statusTextView = (TextView) cardView.findViewById(R.id.status_text_view);
        statusTextView.setText(orders.get(position).getStatus());

        ImageView statusIcon = (ImageView) cardView.findViewById(R.id.status_icon);
        if (orders.get(position).getStatus().equalsIgnoreCase("processing")) {
            Drawable myDrawable =
                    context.getResources().getDrawable(R.drawable.ic_directions_run_black_24dp);
            statusIcon.setImageDrawable(myDrawable);
        } else if (orders.get(position).getStatus().equalsIgnoreCase("confirmed")){
            Drawable myDrawable =
                    context.getResources().getDrawable(R.drawable.ic_done_all_black_24dp);
            statusIcon.setImageDrawable(myDrawable);
        } else {
            Drawable myDrawable =
                    context.getResources().getDrawable(R.drawable.ic_error_outline_black_24dp);
            statusIcon.setImageDrawable(myDrawable);
        }

        Button viewOrderButton = (Button) cardView.findViewById(R.id.view_order);
        viewOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, SellerOrderDetailActivity.class);
                intent.putExtra("order", orders.get(holder.getAdapterPosition()));
                intent.putExtra("STORE_NAME", orders.get(holder.getAdapterPosition()).getStore());
                context.startActivity(intent);
            }
        });

        Button cancelButton = (Button) cardView.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClickDeleteItem(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                    notifyItemRangeChanged(holder.getAdapterPosition(), getItemCount());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }
}