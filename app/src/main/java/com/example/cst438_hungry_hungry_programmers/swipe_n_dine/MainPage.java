package com.example.cst438_hungry_hungry_programmers.swipe_n_dine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.*;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainPage extends AppCompatActivity {

    Button aboutButton;
    Button logoutButton;
    Button startButton;
    Button friendsButton;
    Button favoritesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        friendsButton = (Button) findViewById(R.id.friendsButton);
        startButton = (Button) findViewById(R.id.startSwipingButton);
        favoritesButton = (Button) findViewById(R.id.favoritesButton);
        friendsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainPage.this,FriendsPage.class));
            }
        });
        startButton.setOnClickListener(optionsListener);
        aboutButton.setOnClickListener(optionsListener);
        logoutButton.setOnClickListener(optionsListener);
        favoritesButton.setOnClickListener(optionsListener);
    }



    private OnClickListener optionsListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.startSwipingButton) {
                Intent intent = new Intent(MainPage.this, SelectionPage.class);
                startActivity(intent);
            }
            else if (v.getId() == R.id.aboutUsButton) {
                Intent intent = new Intent(MainPage.this, AboutPage.class);
                startActivity(intent);
            }
            else if (v.getId() == R.id.logoutButton) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainPage.this, LoginPage.class);
                intent.putExtra("isLogout",true);
                startActivity(intent);
            }
            else if (v.getId() == R.id.favoritesButton) {
                Intent intent = new Intent(MainPage.this, FavoritesPage.class);
                startActivity(intent);
            }
        }
    };

}
