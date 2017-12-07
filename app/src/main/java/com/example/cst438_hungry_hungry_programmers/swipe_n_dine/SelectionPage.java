package com.example.cst438_hungry_hungry_programmers.swipe_n_dine;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.connection.YelpFusionApiFactory;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.SearchResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectionPage extends AppCompatActivity {

    TextView mRestaurantTitle;
    ImageView mMainImage;

    YelpFusionApiFactory yelpFusionApiFactory;
    YelpFusionApi yelpFusionApi;

    ArrayList<Business> businesses;
    ArrayList<String> businessNames;
    ArrayList<String> businessImages;
    ArrayList<String> businessUrls;
    String currentUrl;
    boolean isAtStart = true;
    boolean updateFirst = true;
    final int numOfSearchResults = 20;
    int currentBusinessIndex = 0;

    LocationManager mLocationManager;
    double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.selection_page);
        thread.start();

        mRestaurantTitle = (TextView) findViewById(R.id.titleLabel);
        mMainImage = (ImageView) findViewById(R.id.mainImage);

        businessNames = new ArrayList<String>();
        businessImages = new ArrayList<String>();
        businessUrls = new ArrayList<String>();

        mMainImage.setOnTouchListener(new OnSwipeTouchListener(this){
            public void onSwipeTop() {

            }
            public void onSwipeRight() {
                //Toast.makeText(SelectionPage.this, "right", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft() {
                if (isAtStart) {
                    Toast.makeText(SelectionPage.this, "Loading . . .", Toast.LENGTH_SHORT).show();
                    Map<String, String> params = new HashMap<>();

                    params.put("term", "food");
                    params.put("location", "Seaside");

                    Call<SearchResponse> call = yelpFusionApi.getBusinessSearch(params);
                    call.enqueue(callback);
                } else {
                    if (numOfSearchResults < 20) {
                        mRestaurantTitle.setText(businessNames.get(currentBusinessIndex));
                        Picasso.with(getApplicationContext())
                                .load(businessImages.get(currentBusinessIndex))
                                .into(mMainImage);
                        currentUrl = businessUrls.get(currentBusinessIndex);
                        currentBusinessIndex++;
                    } else {

                    }
                }
            }
            public void onSwipeBottom() {

            }
        });
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

    Callback<SearchResponse> callback = new Callback<SearchResponse>() {
        @Override
        public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
            SearchResponse searchResponse = response.body();
            businesses = searchResponse.getBusinesses();
            for (int i = 0; i < numOfSearchResults; i++) {
                businessNames.add(businesses.get(i).getName());
                businessImages.add(businesses.get(i).getImageUrl());
                businessUrls.add(businesses.get(i).getUrl());
            }
            mRestaurantTitle.setText(businessNames.get(currentBusinessIndex));
            Picasso.with(getApplicationContext())
                    .load(businessImages.get(currentBusinessIndex))
                    .into(mMainImage);
            currentUrl = businessUrls.get(currentBusinessIndex);
            currentBusinessIndex++;

            isAtStart = false;
        }

        @Override
        public void onFailure(Call<SearchResponse> call, Throwable t) {
            Log.v("Error Message", t.getMessage());
            // HTTP error happened, do something to handle it.
        }
    };

    public void openBrowser(View view){
        //Get url from tag
        if (!isAtStart) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);

            //pass the url to intent data
            intent.setData(Uri.parse(currentUrl));

            startActivity(intent);
        }
    }
}
