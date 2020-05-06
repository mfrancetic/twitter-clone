package com.example.twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {

    private TextView switchToSignUpLoginTextView;
    private Button signUpLoginButton;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText password2EditText;
    private boolean isLogin = true;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

        if (isLogin) {
            setupLoginView();
        } else {
            setupSignUpView();
        }
        setupClickListeners();

        // checking how much the user has used the app
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    private void setupClickListeners() {
        switchToSignUpLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin) {
                    setupLoginView();
                } else {
                    setupSignUpView();
                }
            }
        });

        signUpLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = getUsername();
                String password = getPassword();

                if (signUpLoginButton.getText().equals(getString(R.string.sign_up))) {
                    if (areUsernamePassword1Password2FieldsValid(username, password, getPassword2())) {
                        signUpUser(username, password);
                    }
                } else if (usernamePasswordAreValid(username, password)) {
                    loginUser(username, password);
                }
            }
        });
    }

    private boolean usernamePasswordAreValid(String username, String password) {
        if (username.matches("") || password.matches("")) {
            ToastUtils.showToast(context, getString(R.string.username_password_required));
            return false;
        } else {
            return true;
        }
    }

    private boolean areUsernamePassword1Password2FieldsValid(String username, String password,
                                                             String password2) {
        if (username.matches("") || password.matches("") || password2.matches("")) {
            ToastUtils.showToast(context, getString(R.string.username_password_required));
            return false;
        } else if (!password.matches(password2)) {
            ToastUtils.showToast(context, getString(R.string.passwords_do_not_match));
            return false;
        } else if (username.length() < 4 || password.length() < 4) {
            ToastUtils.showToast(context, getString(R.string.username_or_password_too_short));
            return false;
        } else if (username.contains(" ") || password.contains(" ")) {
            ToastUtils.showToast(context, getString(R.string.no_spaces_allowed));
            return false;
        } else {
            return true;
        }
    }

    private void findViews() {
        switchToSignUpLoginTextView = findViewById(R.id.switch_to_sign_up_login_text_view);
        signUpLoginButton = findViewById(R.id.sign_up_login_button);
        usernameEditText = findViewById(R.id.username_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        password2EditText = findViewById(R.id.password2_edit_text);
        context = MainActivity.this;
    }

    private void setupLoginView() {
        signUpLoginButton.setText(getString(R.string.login));
        switchToSignUpLoginTextView.setText(getString(R.string.switch_to_sign_up));
        password2EditText.setVisibility(View.GONE);
        isLogin = false;
    }

    private void setupSignUpView() {
        signUpLoginButton.setText(getString(R.string.sign_up));
        switchToSignUpLoginTextView.setText(getString(R.string.switch_to_login));
        password2EditText.setVisibility(View.VISIBLE);
        isLogin = true;
    }

    private void loginUser(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null && user != null) {
                    ToastUtils.showToast(context, getString(R.string.login_successful));
//                    goToUserListActivity();
                } else if (e != null) {
                    ToastUtils.showToast(context, e.getMessage());
                }
            }
        });
    }

    private String getUsername() {
        return usernameEditText.getText().toString();
    }

    private String getPassword() {
        return passwordEditText.getText().toString();
    }

    private String getPassword2() {
        return password2EditText.getText().toString();
    }

    private void signUpUser(String username, String password) {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    ToastUtils.showToast(context, getString(R.string.sign_up_successful));
//                    goToUserListActivity();
                } else {
                    ToastUtils.showToast(context, e.getMessage());
                }
            }
        });
    }
}