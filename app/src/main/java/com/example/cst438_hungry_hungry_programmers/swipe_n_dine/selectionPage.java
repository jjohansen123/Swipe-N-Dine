package com.example.cst438_hungry_hungry_programmers.swipe_n_dine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


public class selectionPage extends AppCompatActivity {
    TextView mRestaurantTitle, mRateCat;
    ImageView mMainImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selection_page);

        mRestaurantTitle = (TextView) findViewById(R.id.titleLabel);
        mRateCat = (TextView) findViewById(R.id.rateCatLabel);
        mMainImage = (ImageView) findViewById(R.id.mainImage);
    }
}
