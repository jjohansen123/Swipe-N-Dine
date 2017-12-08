package com.example.cst438_hungry_hungry_programmers.swipe_n_dine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cst438_hungry_hungry_programmers.swipe_n_dine.models.Friend;
import com.example.cst438_hungry_hungry_programmers.swipe_n_dine.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FriendsPage extends AppCompatActivity {
    private DatabaseReference userRef;
    private Button btnAddFriend;
    private EditText etFriendUID;
    private TextView tvFriends;
    private User mUser;
    private FirebaseAuth mAuth;
    private String friendUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_page);
        btnAddFriend = (Button) findViewById(R.id.btnAddFriend);
        etFriendUID = (EditText) findViewById(R.id.etFriendUID);
        tvFriends = (TextView) findViewById(R.id.tvFriends);
        mAuth = FirebaseAuth.getInstance();

        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getUid());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUser = User.parseSnapshot(dataSnapshot);
                Toast.makeText(getApplicationContext(), mUser.getUid(), Toast.LENGTH_LONG).show();
                for(Friend f:mUser.friends){
                    tvFriends.append("\n" + f.name + "\n");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error logging in", Toast.LENGTH_LONG).show();
            }
        });

        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friendUID = etFriendUID.getText().toString();
                if(friendUID.equals(mAuth.getUid()) || mUser.getFriends().contains(friendUID)){
                    Toast.makeText(getApplicationContext(), "Friend has already been added", Toast.LENGTH_LONG).show();
                    return;
                }

                FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        if (snapshot.child(friendUID).exists()) {
                            String key = userRef.child("friends").push().getKey();
                            Friend f = new Friend();
                            f.setName((String) snapshot.child(friendUID).child("name").getValue());
                            f.setUid(friendUID);
                            userRef.child("friends").child(key).setValue(f);

                            DatabaseReference friendRef = FirebaseDatabase.getInstance().getReference().child("users").child(friendUID);
                            key = friendRef.child("friends").push().getKey();
                            f.setName(mUser.getName());
                            f.setUid(mUser.getUid());
                            friendRef.child("friends").child(key).setValue(f);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "User does not exist", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Error logging in", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }



}
