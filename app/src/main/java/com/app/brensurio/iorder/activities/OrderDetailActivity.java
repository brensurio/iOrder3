package com.app.brensurio.iorder.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.app.brensurio.iorder.R;
import com.app.brensurio.iorder.adapters.OrderDetailAdapter;
import com.app.brensurio.iorder.fragments.OrderDetailFragment;
import com.app.brensurio.iorder.models.Order;

public class OrderDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        getSupportActionBar().setTitle("Transaction details");

        Bundle bundle = new Bundle();
        bundle.putParcelable("order", getIntent().getParcelableExtra("order"));
        Fragment fragment = new OrderDetailFragment();
        fragment.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.activity_order_detail, fragment, "visible_fragment");
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}
