package com.app.brensurio.iorder.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

public class CustomRecyclerView extends RecyclerView {

    private View mEmptyView;

    private AdapterDataObserver adapterDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            updateEmptyView();
        }
    };

    public CustomRecyclerView(Context context) {
        super(context);
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Designate a view as the empty view. When the backing adapter has no
     * data this view will be made visible and the recycler view hidden.
     *
     */
    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        if (getAdapter() != null) {
            getAdapter().unregisterAdapterDataObserver(adapterDataObserver);
        }
        if (adapter != null) {
            adapter.registerAdapterDataObserver(adapterDataObserver);
        }
        super.setAdapter(adapter);
        updateEmptyView();
    }



    private void updateEmptyView() {

        Adapter<?> adapter =  getAdapter();
        if(adapter != null && mEmptyView != null) {
            if(adapter.getItemCount() == 0) {
                mEmptyView.setVisibility(View.VISIBLE);
                CustomRecyclerView.this.setVisibility(View.GONE);
            }
            else {
                mEmptyView.setVisibility(View.GONE);
                CustomRecyclerView.this.setVisibility(View.VISIBLE);
            }
        }


        /*if (mEmptyView != null && getAdapter() != null) {
            boolean showEmptyView = getAdapter().getItemCount() == 0;
            mEmptyView.setVisibility(showEmptyView ? VISIBLE : GONE);
            setVisibility(showEmptyView ? GONE : VISIBLE);
        }*/
    }
}
