package com.app.brensurio.iorder;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {

    private Button signUpButton;
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
     * Called when the Fragment is visible to the user.  This is generally
     * tied to {@link Activity#onStart() Activity.onStart} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onStart() {
        super.onStart();


    }
}
