package com.app.brensurio.iorder.activities;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.brensurio.iorder.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Map;

public class AccountDetailActivity extends AppCompatActivity {

    private EditText emailEditText;
    private TextInputLayout emailTIL;
    private EditText oldpwEditText;
    private TextInputLayout oldpwTIL;
    private EditText npEditText;
    private TextInputLayout npTIL;
    private EditText cnpEditText;
    private TextInputLayout cnpTIL;
    private Button updateButton;

    private ProgressBar progressBar;
    private LinearLayout linearLayout;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        getSupportActionBar().setTitle("Change Password");

        mDatabase = FirebaseDatabase.getInstance().getReference();

        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        linearLayout = (LinearLayout) findViewById(R.id.linear_layout);

        emailEditText = (EditText) findViewById(R.id.email_edit_text);
        emailTIL = (TextInputLayout) findViewById(R.id.email_til);

        oldpwEditText = (EditText) findViewById(R.id.oldpw_edit_text);
        oldpwTIL = (TextInputLayout) findViewById(R.id.oldpw_til);
        npEditText = (EditText) findViewById(R.id.np_et);
        npTIL = (TextInputLayout) findViewById(R.id.np_til);
        cnpEditText = (EditText) findViewById(R.id.cnp_et);
        cnpTIL = (TextInputLayout) findViewById(R.id.cnp_til);

        emailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String email = emailEditText.getText().toString();
                    // Check for a valid email address.
                    if (!email.isEmpty()) {
                        if (!isEmailValid(email))
                            emailTIL.setError(getString(R.string.error_invalid_email));
                        else if (isEmailValid(email))
                            emailTIL.setError(null);
                    }
                }
            }
        });

        oldpwEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String password = npEditText.getText().toString();
                    // Check for a valid email address.
                    if (!password.isEmpty()) {
                        if (!isPasswordValid(password)) {
                            oldpwTIL.setError(getString(R.string.error_invalid_password));
                        }
                        else if (isPasswordValid(password))
                            oldpwTIL.setError(null);
                    }
                }
            }
        });

        npEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String password = npEditText.getText().toString();
                    // Check for a valid email address.
                    if (!password.isEmpty()) {
                        if (!isPasswordValid(password)) {
                            npTIL.setError(getString(R.string.error_invalid_password));
                        }
                        else if (isPasswordValid(password))
                            npTIL.setError(null);
                    }
                }
            }
        });

        cnpEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String password = cnpEditText.getText().toString();
                    // Check for a valid email address.
                    if (!password.isEmpty()) {
                        if (!isPasswordValid(password)) {
                            cnpTIL.setError(getString(R.string.error_invalid_password));
                        }
                        else if (isPasswordValid(password))
                            cnpTIL.setError(null);
                    }
                }
            }
        });

        updateButton = (Button) findViewById(R.id.update_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading();
                updateAccount();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    private void updateAccount() {

        boolean cancel = false;
        emailTIL.setError(null);
        oldpwTIL.setError(null);
        npTIL.setError(null);
        cnpTIL.setError(null);
        // Store values when button is clicked
        String email = emailEditText.getText().toString();
        String oldpw = oldpwEditText.getText().toString();
        String np = npEditText.getText().toString();
        String cnp = cnpEditText.getText().toString();

        if (isFieldEmpty(emailEditText)
                && isFieldEmpty(oldpwEditText)
                && isFieldEmpty(npEditText)
                && isFieldEmpty(cnpEditText)) {
            emailTIL.setError(getString(R.string.error_field_required));
            oldpwTIL.setError(getString(R.string.error_field_required));
            npTIL.setError(getString(R.string.error_field_required));
            cnpTIL.setError(getString(R.string.error_field_required));
            cancel = true;
        }
        if (!isPasswordValid(np) && !isFieldEmpty(npEditText)) {
            npTIL.setError(getString(R.string.error_invalid_password));
            cancel = true;
        }
        if (!isPasswordValid(cnp) && !isFieldEmpty(cnpEditText)) {
            cnpTIL.setError(getString(R.string.error_invalid_password));
            cancel = true;
        }
        if (cancel) {

        } else {
            //
            if (doesPasswordMatch()
                    && !isFieldEmpty(emailEditText)
                    && !isFieldEmpty(oldpwEditText)) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String newPassword = cnp;
                final String TAG = "";

                // Get auth credentials from the user for re-authentication. The example below shows
                // email and password credentials but there are multiple possible providers,
                // such as GoogleAuthProvider or FacebookAuthProvider.
                AuthCredential credential = EmailAuthProvider
                        .getCredential(email, oldpw);
                // Prompt the user to re-provide their sign-in credentials
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d(TAG, "User re-authenticated.");
                            }
                        });
                user.updatePassword(newPassword)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User password updated.");
                                    unload();
                                    FirebaseAuth.getInstance().signOut();
                                    finish();
                                }
                            }
                        });
            } else {
                cnpTIL.setError(getString(R.string.pw_do_not_match_text));
            }
        }
    }

    private boolean isFieldEmpty(EditText editText) {
        return TextUtils.isEmpty(editText.getText());
    }

    private boolean doesPasswordMatch() {
        String password = npEditText.getText().toString();
        String confirmPassword = cnpEditText.getText().toString();

        return password.equals(confirmPassword);
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
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
}
