package com.example.cst438_hungry_hungry_programmers.swipe_n_dine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.*;
import android.widget.Button;

public class MainPage extends AppCompatActivity {

    Button aboutButton;
    Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        aboutButton = (Button) findViewById(R.id.aboutUsButton);
        logoutButton = (Button) findViewById(R.id.logoutButton);

        aboutButton.setOnClickListener(optionsListener);
        logoutButton.setOnClickListener(optionsListener);
    }

    private OnClickListener optionsListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.aboutUsButton) {
                Intent intent = new Intent(MainPage.this, AboutPage.class);
                startActivity(intent);
            }
            else if (v.getId() == R.id.logoutButton) {
                Intent intent = new Intent(MainPage.this, LoginPage.class);
                startActivity(intent);
            }
        }
    };

}
