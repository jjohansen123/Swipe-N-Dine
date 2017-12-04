package com.example.cst438_hungry_hungry_programmers.swipe_n_dine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.*;
import android.widget.Button;
import android.widget.EditText;

public class LoginPage extends AppCompatActivity {

    EditText usernameInput;
    EditText passwordInput;
    Button submitLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        usernameInput = (EditText) findViewById(R.id.usernameInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);
        submitLogin = (Button) findViewById(R.id.loginSubmit);
        submitLogin.setOnClickListener(loginListener);
    }

    private OnClickListener loginListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.loginSubmit) {
                Intent intent = new Intent(LoginPage.this, MainPage.class);
                startActivity(intent);

            }
        }
    };
}
