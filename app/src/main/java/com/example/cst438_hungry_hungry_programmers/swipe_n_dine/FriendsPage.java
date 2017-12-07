package com.example.cst438_hungry_hungry_programmers.swipe_n_dine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

public class FriendsPage extends AppCompatActivity {
    FirebaseDatabase userRef;
    Button btnAddFriend;
    EditText etFriendUID;
    TextView tvFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_page);


    }
}
