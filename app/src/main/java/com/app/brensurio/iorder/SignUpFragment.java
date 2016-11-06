package com.app.brensurio.iorder;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {

    private static final String TAG = "";
    // Firebase instance variables
    private DatabaseReference mDatabase;
    private MyFragmentCallback callback;
    private TabLayout tabLayout;
    private EditText eidEditText;
    private EditText emailEditText;
    private EditText nameEditText;
    private EditText passwordEditText;
    private EditText pwConfirmEditText;
    private EditText surnameEditText;
    private TextInputLayout eidTextInputLayout;
    private TextInputLayout emailTextInputLayout;
    private TextInputLayout nameTextInputLayout;
    private TextInputLayout passwordTextInputLayout;
    private TextInputLayout pwConfirmTextInputLayout;
    private TextInputLayout surnameTextInputLayout;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
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

    /**
     * Called when the Fragment is visible to the user.  This is generally
     * tied to {@link Activity#onStart() Activity.onStart} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onStart() {
        super.onStart();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        View view = getView();
        if (view != null) {

            Button signUpButton = (Button) view.findViewById(R.id.sign_up_button);
            signUpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    attemptSignUp();
                }
            });

            eidEditText = (EditText) view.findViewById(R.id.eid_edit_text);
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

            nameTextInputLayout = (TextInputLayout) view.findViewById(R.id.name_text_input_layout);
            nameEditText = (EditText) view.findViewById(R.id.name_edit_text);
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

            pwConfirmEditText = (EditText) view.findViewById(R.id.confirm_password_edit_text);
            pwConfirmTextInputLayout = (TextInputLayout)
                    view.findViewById(R.id.confirm_password_text_input_layout);
            pwConfirmEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        String password = pwConfirmEditText.getText().toString();
                        // Check for a valid email address.
                        if (!password.isEmpty()) {
                            if (!isPasswordValid(password))
                                pwConfirmTextInputLayout
                                        .setError(getString(R.string.error_invalid_password));
                            else if (isPasswordValid(password))
                                pwConfirmTextInputLayout.setError(null);
                        }
                    }
                }
            });

            surnameEditText = (EditText) view.findViewById(R.id.surname_edit_text);
            eidTextInputLayout = (TextInputLayout) view.findViewById(R.id.eid_text_input_layout);
            surnameTextInputLayout = (TextInputLayout)
                    view.findViewById(R.id.surname_text_input_layout);
        }
    }

    private void attemptSignUp() {

        boolean cancel = false;
        View focusView = null;

        eidTextInputLayout.setError(null);
        emailTextInputLayout.setError(null);
        nameTextInputLayout.setError(null);
        passwordTextInputLayout.setError(null);
        pwConfirmTextInputLayout.setError(null);
        surnameTextInputLayout.setError(null);

        if (isFieldEmpty(eidEditText)) {
            eidTextInputLayout.setError(getString(R.string.error_field_required));
            focusView = eidEditText;
            cancel = true;
        }
        if (isFieldEmpty(emailEditText)) {
            emailTextInputLayout.setError(getString(R.string.error_field_required));
            focusView = emailEditText;
            cancel = true;
        }
        if (isFieldEmpty(nameEditText)) {
            nameTextInputLayout.setError(getString(R.string.error_field_required));
            focusView = nameEditText;
            cancel = true;
        }
        if (isFieldEmpty(passwordEditText)) {
            passwordTextInputLayout.setError(getString(R.string.error_field_required));
            focusView = passwordEditText;
            cancel = true;
        }
        if (isFieldEmpty(pwConfirmEditText)) {
            pwConfirmTextInputLayout.setError(getString(R.string.error_field_required));
            focusView = pwConfirmEditText;
            cancel = true;
        }
        if (isFieldEmpty(surnameEditText)) {
            surnameTextInputLayout.setError(getString(R.string.error_field_required));
            focusView = nameEditText;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            if (doesPasswordMatch()) {
                Query eidQuery = mDatabase.child("eidlist").orderByChild("eid")
                        .equalTo(eidEditText.getText().toString());
                eidQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            callback.signUp(emailEditText.getText().toString(),
                                    passwordEditText.getText().toString(),
                                    nameEditText.getText().toString().concat(" " +
                                            surnameEditText.getText().toString()));
                            User user = new User(nameEditText.getText().toString(),
                                    surnameEditText.getText().toString(),
                                    eidEditText.getText().toString(),
                                    emailEditText.getText().toString());
                            mDatabase.child("users").push().setValue(user);
                            Toast.makeText(getActivity(), "Account created!",
                                    Toast.LENGTH_SHORT).show();
                            tabLayout.getTabAt(0).select();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) { }
                });
            } else {
                pwConfirmTextInputLayout.setError(getString(R.string.pw_do_not_match_text));
            }
        }
    }

    private boolean isFieldEmpty(EditText editText) {
        return TextUtils.isEmpty(editText.getText());
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    private boolean doesPasswordMatch() {
        String password = passwordEditText.getText().toString();
        String confirmPassword = pwConfirmEditText.getText().toString();

        return password.equals(confirmPassword);
    }
}