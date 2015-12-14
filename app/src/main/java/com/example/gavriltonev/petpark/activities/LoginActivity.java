package com.example.gavriltonev.petpark.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.example.gavriltonev.petpark.R;
import com.example.gavriltonev.petpark.models.Token;
import com.example.gavriltonev.petpark.models.User;
import com.example.gavriltonev.petpark.models.services.TokenApiInterface;
import com.example.gavriltonev.petpark.utils.Preferences;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    // UI references.
    private EditText _emailText;
    private EditText _passwordText;
    private Button _loginButton;
    private TextView _signupLink;

    private View _loginFormView;
    private View _progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String token = Preferences.getInstance(this).getString(Preferences.Key.TOKEN_STR);
        if (token != null) {
            goToMainActivity();
        }

        _emailText = (EditText) findViewById( R.id.input_email);
        _passwordText = (EditText) findViewById( R.id.input_password);
        _loginButton = (Button) findViewById( R.id.btn_login);
        _signupLink = (TextView) findViewById( R.id.link_signup);

        _loginFormView = findViewById(R.id.login_form);
        _progressDialog = findViewById(R.id.login_progress);

        _loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to the Singup Activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    private void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed("Incorrect data.");
            return;
        }

        _loginButton.setEnabled(false);

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        loginOperation(email, password);
    }

    private void loginOperation(String email, String password) {
        showProgress(true);

        // Using Retrofit and Gson libraries
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.petparkAPI))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TokenApiInterface tokenApiInterface = retrofit.create(TokenApiInterface.class);
        Call<Token> call = tokenApiInterface.getToken("password", email, password);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Response<Token> response, Retrofit retrofit) {
                if (response.body() != null) {
                    onLoginSuccess(response.body());
                    showProgress(false);
                } else {
                    onLoginFailed("Unrecognized username or password");
                    showProgress(false);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                onLoginFailed("Network error, please try again.");
                showProgress(false);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    private void onLoginSuccess(Token token) {
        _loginButton.setEnabled(true);

        Preferences.getInstance(this).put(Preferences.Key.TOKEN_STR, token.getAccess_token());
        Preferences.getInstance(this).put(Preferences.Key.USER_NAME_STR, token.getUserName());
        goToMainActivity();
    }

    private void onLoginFailed(String message) {
        _loginButton.setEnabled(true);
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void goToMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
    private boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            _emailText.setError("enter a valid email address");
            valid = false;
        } else _emailText.setError(null);

        if (password.isEmpty() || password.length() < 4){
            _passwordText.setError("at least 6 alphanumeric characters");
            valid = false;
        } else _passwordText.setError(null);

        return valid;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {

        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            _loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            _loginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    _loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            _progressDialog.setVisibility(show ? View.VISIBLE : View.GONE);
            _progressDialog.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    _progressDialog.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            _progressDialog.setVisibility(show ? View.VISIBLE : View.GONE);
            _loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}

