package com.app.brensurio.iorder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.brensurio.iorder.interfaces.MyFragmentCallback;
import com.app.brensurio.iorder.R;
import com.app.brensurio.iorder.fragments.MainFragment;
import com.app.brensurio.iorder.fragments.SignUpFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements MyFragmentCallback {

    private static final String TAG = "";
    private static final String STORE_NAME = "storeName";
    private static final String STORE_1 = "store1";
    private static final String STORE_2 = "store2";
    private static final String STORE_3 = "store3";
    private TabLayout tabLayout;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private ProgressBar progressBar;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        linearLayout = (LinearLayout) findViewById(R.id.linear_layout);
        loading();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter =
                new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onStart() {
        super.onStart();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    loading();
                    // If user is signed in
                    Query eidQuery = mDatabase.child("users").orderByChild("email")
                            .equalTo(user.getEmail());
                    eidQuery.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                    Map m = (Map) postSnapshot.getValue();
                                    String eid = m.get("eid").toString().toLowerCase();
                                    String email = m.get("email").toString();
                                    String name = m.get("firstName").toString() + " "
                                            + m.get("lastName").toString();
                                    if (eid.substring(0, 1).equalsIgnoreCase("s")) {
                                        Intent intent = new Intent(MainActivity.this,
                                                SellerMainActivity.class);
                                        if (eid.substring(1, 2).equalsIgnoreCase("a"))
                                            intent.putExtra(MainActivity.STORE_NAME,
                                                    MainActivity.STORE_1);
                                        else if (eid.substring(1, 2).equalsIgnoreCase("b"))
                                            intent.putExtra(MainActivity.STORE_NAME,
                                                    MainActivity.STORE_2);
                                        else if (eid.substring(1, 2).equalsIgnoreCase("c"))
                                            intent.putExtra(MainActivity.STORE_NAME,
                                                    MainActivity.STORE_3);
                                        //loading();
                                        startActivity(intent);
                                        finish();
                                    } else if (eid.substring(0, 1).equalsIgnoreCase("f")) {
                                        Intent intent = new Intent(MainActivity.this,
                                                CustomerMainActivity.class);
                                        intent.putExtra("NAME", name);
                                        intent.putExtra("EMAIL", email);
                                        //loading();
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) { }
                    });
                } else {
                    // User is signed out
                    unload();
                    Toast.makeText(MainActivity.this, "Please sign in",
                            Toast.LENGTH_SHORT).show();
                }
            }
        };

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
    public void signUp(String email, String password) {
        loading();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(MainActivity.this, "Account created!",
                                Toast.LENGTH_SHORT).show();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            unload();
                            Toast.makeText(MainActivity.this, "Sign up failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void signIn(String email, String password) {
        loading();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            unload();
                            Toast.makeText(MainActivity.this, "Failed!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void loading() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        linearLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void unload() {
        linearLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            if (position == 0) {
                return new MainFragment();
            }
            else if (position == 1)
                return new SignUpFragment();
            else
                return new MainFragment();
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "LOG IN";
                case 1:
                    return "SIGN UP";
            }
            return null;
        }
    }

    public TabLayout getTabLayout() {
        return  tabLayout;
    }
}