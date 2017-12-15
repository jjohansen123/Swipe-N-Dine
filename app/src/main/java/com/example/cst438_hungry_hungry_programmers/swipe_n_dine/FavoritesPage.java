package com.example.cst438_hungry_hungry_programmers.swipe_n_dine;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cst438_hungry_hungry_programmers.swipe_n_dine.adapters.FavoritesAdapter;
import com.example.cst438_hungry_hungry_programmers.swipe_n_dine.models.Restaurant;
import com.example.cst438_hungry_hungry_programmers.swipe_n_dine.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class FavoritesPage extends AppCompatActivity {

    Button backToMain;
    User currentUser;
    RecyclerView rvFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites_page);

        rvFavorites = (RecyclerView) findViewById(R.id.rvFavorites);
        backToMain = (Button) findViewById(R.id.backButton);
        backToMain.setOnClickListener(optionsListener);

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUser = User.parseSnapshot(dataSnapshot);
                ArrayList<Restaurant> favs = (ArrayList<Restaurant>) currentUser.getFavorites();
                Collections.reverse(favs);
                rvFavorites.setAdapter(new FavoritesAdapter(FavoritesPage.this, favs));
                rvFavorites.setLayoutManager(new LinearLayoutManager(FavoritesPage.this));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(FavoritesPage.this,"Error, fetching favorites.",Toast.LENGTH_LONG);
            }
        });
    }

    private View.OnClickListener optionsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.backButton) {
                Intent intent = new Intent(FavoritesPage.this, MainPage.class);
                startActivity(intent);
            }
        }
    };
}