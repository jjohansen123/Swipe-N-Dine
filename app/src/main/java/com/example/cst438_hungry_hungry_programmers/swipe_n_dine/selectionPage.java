package com.example.cst438_hungry_hungry_programmers.swipe_n_dine;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


public class selectionPage extends AppCompatActivity {
    TextView mRestaurantTitle, mRateCat;
    ImageView mMainImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.selection_page);

        mRestaurantTitle = (TextView) findViewById(R.id.titleLabel);
        mRateCat = (TextView) findViewById(R.id.rateCatLabel);
        mMainImage = (ImageView) findViewById(R.id.mainImage);

        Picasso
                .with(this)
                .load("http://i.imgur.com/DvpvklR.png")
                .into(mMainImage);
        mMainImage.setOnTouchListener(new OnSwipeTouchListener(this){
            public void onSwipeTop() {
                Toast.makeText(selectionPage.this, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                Toast.makeText(selectionPage.this, "right", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft() {
                Toast.makeText(selectionPage.this, "left", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeBottom() {
                Toast.makeText(selectionPage.this, "bottom", Toast.LENGTH_SHORT).show();
            }
        });

        new FetchPictures().execute();
    }
    // Doing the API in the background
    // String initilise - string updates - string ignore
    class FetchPictures extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String ... params) {

            return null;
        }
    }

}
