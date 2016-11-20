package com.app.brensurio.iorder.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.brensurio.iorder.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoreFragment extends Fragment {


    public StoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_store, container, false);
    }

    /**
     * Called when the Fragment is visible to the user.  This is generally
     * tied to {@link Activity#onStart() Activity.onStart} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onStart() {
        super.onStart();

        View view = getView();
        if (view != null) {
            SectionsPagerAdapter mSectionsPagerAdapter =
                    new SectionsPagerAdapter(getActivity().getSupportFragmentManager());
            ViewPager mViewPager = (ViewPager) view.findViewById(R.id.customer_container);
            mViewPager.setAdapter(mSectionsPagerAdapter);
            TabLayout tabLayout = (TabLayout) view.findViewById(R.id.customer_tab_layout);
            tabLayout.setupWithViewPager(mViewPager);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            if (position == 0) {
                Bundle args = new Bundle();
                args.putInt("store_id", 1);
                SpecificStoreFragment specificStoreFragment = new SpecificStoreFragment();
                specificStoreFragment.setArguments(args);
                return specificStoreFragment;
            } else if (position == 1) {
                Bundle args = new Bundle();
                args.putInt("store_id", 2);
                SpecificStoreFragment specificStoreFragment = new SpecificStoreFragment();
                specificStoreFragment.setArguments(args);
                return specificStoreFragment;
            } else if (position == 2) {
                Bundle args = new Bundle();
                args.putInt("store_id", 3);
                SpecificStoreFragment specificStoreFragment = new SpecificStoreFragment();
                specificStoreFragment.setArguments(args);
                return specificStoreFragment;
            }
            else {
                Bundle args = new Bundle();
                args.putInt("store_id", 0);
                SpecificStoreFragment specificStoreFragment = new SpecificStoreFragment();
                specificStoreFragment.setArguments(args);
                return specificStoreFragment;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SCOOPS";
                case 1:
                    return "NANAY'S CUISINE";
                case 2:
                    return "OVEN MAID";
            }
            return null;
        }
    }
}
