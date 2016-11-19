package com.app.brensurio.iorder.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.brensurio.iorder.interfaces.MyFragmentCallback;
import com.app.brensurio.iorder.R;
import com.app.brensurio.iorder.activities.MainActivity;

/**
 * Created by Mariz L. Maas on 10/18/2016.
 */

public class MainFragment extends Fragment {

    private MyFragmentCallback callback;
    private TabLayout tabLayout;
    private EditText emailEditText;
    private EditText passwordEditText;
    private TextInputLayout emailTextInputLayout;
    private TextInputLayout passwordTextInputLayout;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    /**
     * Called when a fragment is first attached to its context.
     * {@link #onCreate(Bundle)} will be called after this.
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        callback = (MyFragmentCallback) context;
        super.onAttach(context);
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
            Button signInButton = (Button) view.findViewById(R.id.sign_in_button);
            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptLogin();
                }
            });
            
            emailEditText = (EditText) view.findViewById(R.id.email_edit_text);
            emailTextInputLayout = (TextInputLayout)
                    view.findViewById(R.id.email_text_input_layout);
            emailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        String email = emailEditText.getText().toString();
                        // Check for a valid email address.
                        if (!email.isEmpty()) {
                            if (!isEmailValid(email))
                                emailTextInputLayout
                                        .setError(getString(R.string.error_invalid_email));
                            else if (isEmailValid(email))
                                emailTextInputLayout.setError(null);
                        }
                    }
                }
            });

            passwordEditText = (EditText) view.findViewById(R.id.password_edit_text);
            passwordTextInputLayout = (TextInputLayout)
                    view.findViewById(R.id.password_text_input_layout);
            passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        String password = passwordEditText.getText().toString();
                        // Check for a valid email address.
                        if (!password.isEmpty()) {
                            if (!isPasswordValid(password))
                                passwordTextInputLayout
                                        .setError(getString(R.string.error_invalid_password));
                            else if (isPasswordValid(password))
                                passwordTextInputLayout.setError(null);
                        }
                    }
                }
            });

            TextView signUpLinkTextView = (TextView) view.findViewById(R.id.sign_up_link_text_view);
            signUpLinkTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSignUp();
                }
            });
        }
    }

    /**
     * Called when the fragment's activity has been created and this
     * fragment's view hierarchy instantiated.  It can be used to do final
     * initialization once these pieces are in place, such as retrieving
     * views or restoring state.  It is also useful for fragments that use
     * {@link #setRetainInstance(boolean)} to retain their instance,
     * as this callback tells the fragment when it is fully associated with
     * the new activity instance.  This is called after {@link #onCreateView}
     * and before {@link #onViewStateRestored(Bundle)}.
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tabLayout = ((MainActivity) this.getActivity()).getTabLayout();
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        emailTextInputLayout.setError(null);
        passwordTextInputLayout.setError(null);
        // Store values at the time of the login attempt.
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!isPasswordValid(password)) {
            passwordTextInputLayout.setError(getString(R.string.error_invalid_password));
            focusView = passwordEditText;
            cancel = true;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailTextInputLayout.setError(getString(R.string.error_field_required));
            focusView = emailEditText;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailTextInputLayout.setError(getString(R.string.error_invalid_email));
            focusView = emailEditText;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            callback.signIn(emailEditText.getText().toString(),
                    passwordEditText.getText().toString());

        }
    }

    private void onSignUp() {
        tabLayout.getTabAt(1).select();
    }
}
