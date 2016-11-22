package com.app.brensurio.iorder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.brensurio.iorder.fragments.CartFragment;
import com.app.brensurio.iorder.fragments.HistoryFragment;
import com.app.brensurio.iorder.fragments.OrderFragment;
import com.app.brensurio.iorder.fragments.StoreFragment;
import com.app.brensurio.iorder.interfaces.StoreFragmentListener;
import com.app.brensurio.iorder.models.Food;
import com.app.brensurio.iorder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class CustomerMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        StoreFragmentListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ArrayList<Food> orderList;
    private int currentPosition = 0;
    private String customerName;
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);

        mAuth = FirebaseAuth.getInstance();

        if (savedInstanceState != null) {
            orderList = savedInstanceState.getParcelableArrayList("foodlist");
        } else {
            orderList = new ArrayList<>();
        }


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Store menus");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().performIdentifierAction(R.id.nav_shop_now, 0);

        View headerLayout = navigationView.getHeaderView(0);
        TextView nameTextView = (TextView) headerLayout.findViewById(R.id.customer_name_text_view);
        nameTextView.setText(getIntent().getStringExtra("NAME"));
        TextView emailTextView = (TextView) headerLayout.findViewById(R.id.customer_email_text_view);
        emailTextView.setText(getIntent().getStringExtra("EMAIL"));

        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    @Override
                    public void onBackStackChanged() {
                        FragmentManager fragMan = getSupportFragmentManager();
                        Fragment fragment = fragMan.findFragmentByTag("visible_fragment");
                        if (fragment instanceof StoreFragment) {
                            currentPosition = 0;
                        }
                        if (fragment instanceof CartFragment) {
                            currentPosition = 1;
                        }
                        if (fragment instanceof OrderFragment) {
                            currentPosition = 2;
                        }
                    }
                }
        );
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                } else {
                    finish();
                    Intent intent = new Intent(CustomerMainActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };

        customerName = getIntent().getStringExtra("NAME");

        progressBar = (ProgressBar) findViewById(R.id.progressBar3);
        linearLayout = (LinearLayout) findViewById(R.id.linear_layout);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.customer_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("foodlist", orderList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cart) {
            toolbar.setTitle("Your food cart");
            Fragment fragment = new CartFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_customer_main, fragment, "visible_fragment");
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        currentPosition = id;
        Fragment fragment = new StoreFragment();

        if (id == R.id.nav_shop_now) {
            fragment = new StoreFragment();
            toolbar.setTitle("Store menus");
        } else if (id == R.id.nav_cart) {
            fragment = new CartFragment();
            toolbar.setTitle("Your food cart");
        } else if (id == R.id.nav_orders) {
            fragment = new OrderFragment();
            toolbar.setTitle("Present Orders");
        } else if (id == R.id.nav_history) {
            fragment = new HistoryFragment();
            toolbar.setTitle("Past Orders");
        } else if (id == R.id.nav_log_out) {
            FirebaseAuth.getInstance().signOut();
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_customer_main, fragment, "visible_fragment");
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public ArrayList<Food> getOrderList() {
        return this.orderList;
    }

    @Override
    public void addToOrderList(Food food) {
        this.orderList.add(food);
    }

    @Override
    public String getCustomerName() {
        return this.customerName;
    }

    @Override
    public void loading() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        linearLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        new CountDownTimer(1000, 100) {
            public void onTick(long millisUntilFinished) { }

            public void onFinish() {
                linearLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        }.start();
    }
}
