package com.app.brensurio.iorder.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.app.brensurio.iorder.R;
import com.app.brensurio.iorder.models.Food;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {

    List<Food> orderList;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout view;

        public ViewHolder(LinearLayout v) {
            super(v);
            view = v;
        }
    }

    public OrderDetailAdapter(List<Food> orderList) {
        this.orderList = orderList;
    }

    @Override
    public OrderDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout view = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_order_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LinearLayout view = holder.view;

        ColorGenerator generator = ColorGenerator.MATERIAL;
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(Character.toString(orderList.get(position).getName().charAt(0)),
                        generator.getRandomColor());
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView9);
        imageView.setImageDrawable(drawable);

        TextView nameTextView = (TextView) view.findViewById(R.id.name_text_view);
        nameTextView.setText(orderList.get(position).getName());

        TextView unitPriceTextView = (TextView) view.findViewById(R.id.unit_price_text_view);
        unitPriceTextView.setText(Double.toString(orderList.get(position).getPrice()));

        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity_text_view);
        quantityTextView.setText(Integer.toString(orderList.get(position).getAmount()));

        TextView totalPriceTextView = (TextView) view.findViewById(R.id.total_price_text_view);
        totalPriceTextView.setText(Double.toString(orderList.get(position).getPrice() *
                orderList.get(position).getAmount()));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
