package com.example.gavriltonev.petpark.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gavriltonev.petpark.R;
import com.example.gavriltonev.petpark.models.Token;
import com.example.gavriltonev.petpark.models.User;
import com.example.gavriltonev.petpark.models.services.TokenApiInterface;
import com.example.gavriltonev.petpark.models.services.UserApiInterface;
import com.example.gavriltonev.petpark.utils.Preferences;
import com.squareup.okhttp.ResponseBody;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    private EditText _emailText;
    private EditText _passwordText;
    private EditText _repeatPasswordText;

    private Button   _signupButton;
    private TextView _loginLink;

    private View _loginFormView;
    private View _progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        _emailText = (EditText) findViewById(R.id.input_email);
        _passwordText = (EditText) findViewById(R.id.input_password);
        _repeatPasswordText = (EditText) findViewById(R.id.input_repPassword);

        _signupButton = (Button) findViewById(R.id.btn_signup);
        _loginLink = (TextView) findViewById(R.id.link_login);

        _loginFormView = findViewById(R.id.login_form);
        _progressDialog = findViewById(R.id.login_progress);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to the Login Activity
                onSignupSuccess(new User("some", "some2", "some3"));
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed("Incorrect data.");
            return;
        }

        _signupButton.setEnabled(false);
        showProgress(true);

        final User user = new User(
                _emailText.getText().toString(),
                _passwordText.getText().toString(),
                _repeatPasswordText.getText().toString()
        );

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.petparkAPI))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserApiInterface userApiInterface = retrofit.create(UserApiInterface.class);
        Call<ResponseBody> call = userApiInterface.createUser(user);
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                if (response.raw().code() == 200) onSignupSuccess(user);
                else onSignupFailed("Failed to register.");
            }

            @Override
            public void onFailure(Throwable t) {
                onSignupFailed("Network error, please try again.");
            }
        });
    }

    private void onSignupSuccess(User user) {
        _signupButton.setEnabled(true);

        // Using Retrofit and Gson libraries
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.petparkAPI))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TokenApiInterface tokenApiInterface = retrofit.create(TokenApiInterface.class);
        Call<Token> call = tokenApiInterface.getToken("password", user.getEmail(), user.getPassword());
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Response<Token> response, Retrofit retrofit) {
                if (response.body() != null) {
                    onLoginSuccess(response.body().getAccess_token());
                    showProgress(false);
                } else {
                    onLoginFailed("Error. PLease try to go back and login");
                    showProgress(false);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                onLoginFailed("Network error, please try to go back and login.");
                showProgress(false);
            }
        });
    }

    private void onLoginSuccess(String access_token) {
        Preferences.getInstance(getApplicationContext()).put(Preferences.Key.TOKEN_STR, access_token);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

        finish();
    }

    private void onLoginFailed(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void onSignupFailed(String message) {
        _signupButton.setEnabled(true);
        showProgress(false);
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String repeatPass = _repeatPasswordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (!isValidPassword(password)) {
            _passwordText.setError("at least 6 letters, 1 lower and 1 upper case");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (!repeatPass.equals(password)) {
            _repeatPasswordText.setError("the passwords must match");
            valid = false;
        } else {
            _repeatPasswordText.setError(null);
        }

        return valid;
    }

    private boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

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
