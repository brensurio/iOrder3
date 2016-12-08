package com.app.brensurio.iorder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.app.brensurio.iorder.R;
import com.app.brensurio.iorder.SellerService;
import com.app.brensurio.iorder.fragments.SellerFoodListFragment;
import com.app.brensurio.iorder.fragments.SellerHistoryFragment;
import com.app.brensurio.iorder.fragments.SellerOrderFragment;
import com.app.brensurio.iorder.interfaces.SellerFragmentListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SellerMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SellerFragmentListener {

    private static final String STORE_NAME = "storeName";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String storeName;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        storeName = getIntent().getStringExtra(STORE_NAME);
        Intent intent = new Intent(getBaseContext(), SellerService.class);
        intent.putExtra("storeName", storeName);
        startService(intent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().performIdentifierAction(R.id.nav_orders, 0);

        View header = navigationView.getHeaderView(0);
        TextView usernameTextView = (TextView) header.findViewById(R.id.username_text_view);
        usernameTextView.setText(getIntent().getStringExtra("NAME"));
        TextView emailTextView = (TextView) header.findViewById(R.id.user_email_textview);
        emailTextView.setText(getIntent().getStringExtra("EMAIL"));

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) { }
                else {
                    Intent intent = new Intent(SellerMainActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
        getMenuInflater().inflate(R.menu.seller_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_food) {
            Intent intent = new Intent(SellerMainActivity.this, UploadFoodActivity.class);
            intent.putExtra(STORE_NAME, storeName);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = new SellerFoodListFragment();

        if (id == R.id.nav_orders) {
            fragment = new SellerOrderFragment();
            toolbar.setTitle("Present Orders");
        } else if (id == R.id.nav_gallery) {
            fragment = new SellerFoodListFragment();
            toolbar.setTitle("Food menu");
        } else if (id == R.id.nav_history) {
            fragment = new SellerHistoryFragment();
            toolbar.setTitle("History");
        } else if (id == R.id.nav_log_out) {
            FirebaseAuth.getInstance().signOut();
        } else if (id == R.id.nav_details) {
            Intent intent = new Intent(this, AccountDetailActivity.class);
            startActivity(intent);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_seller_main, fragment, "visible_fragment");
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public String getStoreName() {
        return storeName;
    }
}
