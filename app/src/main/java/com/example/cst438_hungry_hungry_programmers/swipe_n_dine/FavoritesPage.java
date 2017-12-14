package com.example.cst438_hungry_hungry_programmers.swipe_n_dine;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class FavoritesPage extends AppCompatActivity {

    Button backToMain;
    TextView favoriteChoice;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites_page);

        backToMain = (Button) findViewById(R.id.backButton);
        favoriteChoice = (TextView) findViewById(R.id.favoriteTexts);
        backToMain.setOnClickListener(optionsListener);
        favoriteChoice.setOnClickListener(optionsListener);
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUser = User.parseSnapshot(dataSnapshot);
                favoriteChoice.setText(currentUser.getFavorites().get(0).getName());
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
            else if (v.getId() == R.id.favoriteTexts) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);

                //pass the url to intent data
                intent.setData(Uri.parse("https://www.yelp.com/biz/el-pollo-rey-seaside-2"));

                startActivity(intent);
            }
        }
    };
}