package com.example.cst438_hungry_hungry_programmers.swipe_n_dine;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.connection.YelpFusionApiFactory;
import com.yelp.fusion.client.models.Business;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectionPage extends AppCompatActivity {
    TextView mRestaurantTitle, mRateCat;
    ImageView mMainImage;

    YelpFusionApiFactory yelpFusionApiFactory;
    YelpFusionApi yelpFusionApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.selection_page);
        thread.start();

        mRestaurantTitle = (TextView) findViewById(R.id.titleLabel);
        mRateCat = (TextView) findViewById(R.id.rateCatLabel);
        mMainImage = (ImageView) findViewById(R.id.mainImage);

        Picasso
                .with(this)
                .load("http://i.imgur.com/DvpvklR.png")
                .into(mMainImage);
        mMainImage.setOnTouchListener(new OnSwipeTouchListener(this){
            public void onSwipeTop() {
                Toast.makeText(SelectionPage.this, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                Toast.makeText(SelectionPage.this, "right", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft() {
                Toast.makeText(SelectionPage.this, "left", Toast.LENGTH_SHORT).show();
                Call<Business> call = yelpFusionApi.getBusiness("gary-danko-san-francisco");
                call.enqueue(callback);
            }
            public void onSwipeBottom() {
                Toast.makeText(SelectionPage.this, "bottom", Toast.LENGTH_SHORT).show();
            }
        });

//        new FetchPictures().execute();
    }

    Thread thread = new Thread(new Runnable(){
        @Override
        public void run() {
            try {
                yelpFusionApiFactory = new YelpFusionApiFactory();
                yelpFusionApi = yelpFusionApiFactory.createAPI(getString(R.string.clientId), getString(R.string.clientSecret));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });

    Callback<Business> callback = new Callback<Business>() {
        @Override
        public void onResponse(Call<Business> call, Response<Business> response) {
            Business business = response.body();
            ArrayList<String> d = business.getLocation().getDisplayAddress();
            StringBuilder sbStr = new StringBuilder();
            for (int i = 0, il = d.size(); i < il; i++) {
                if (i > 0)
                    sbStr.append(',');
                sbStr.append(d.get(i));
            }
            mRestaurantTitle.setText(sbStr.toString());

            // Update UI text with the Business object.
        }

        @Override
        public void onFailure(Call<Business> call, Throwable t) {
            mRestaurantTitle.setText(t.getMessage());
            // HTTP error happened, do something to handle it.
        }
    };
    // Doing the API in the background
    // String initilise - string updates - string ignore
    class FetchPictures extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String ... params) {

            return null;
        }
    }

}
