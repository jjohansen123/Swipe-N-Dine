package com.example.cst438_hungry_hungry_programmers.swipe_n_dine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.*;
import android.widget.Button;

public class AboutPage extends AppCompatActivity {

    Button backToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_page);

        backToMain = (Button) findViewById(R.id.backButton);
        backToMain.setOnClickListener(backListener);
    }

    private OnClickListener backListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.backButton) {
                Intent intent = new Intent(AboutPage.this, MainPage.class);
                startActivity(intent);

            }
        }
    };
}
