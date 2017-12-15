package com.example.cst438_hungry_hungry_programmers.swipe_n_dine;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.Manifest;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cst438_hungry_hungry_programmers.swipe_n_dine.models.Restaurant;
import com.example.cst438_hungry_hungry_programmers.swipe_n_dine.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.connection.YelpFusionApiFactory;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.SearchResponse;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectionPage extends AppCompatActivity implements LocationListener {

    TextView mRestaurantTitle;
    ImageView mMainImage;
    ImageView mYelpLogo;

    YelpFusionApiFactory yelpFusionApiFactory;
    YelpFusionApi yelpFusionApi;

    ArrayList<Business> businesses;
    ArrayList<String> businessNames;
    ArrayList<String> businessImages;
    ArrayList<String> businessUrls;
    String currentUrl;
    boolean isAtStart = true;
    boolean updateFirst = true;
    int numOfSearchResults = 20;
    int currentBusinessIndex = 0;


    final String TAG = "GPS";
    private final static int ALL_PERMISSIONS_RESULT = 101;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    LocationManager locationManager;
    Location loc;
    ArrayList<String> permissions = new ArrayList<>();
    ArrayList<String> permissionsToRequest;
    ArrayList<String> permissionsRejected = new ArrayList<>();
    boolean isGPS = false;
    boolean isNetwork = false;
    boolean canGetLocation = true;

    String latitude;
    String longitude;

    User currentUser;

    LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.selection_page);
        thread.start();

        locationManager = (LocationManager) getSystemService(Service.LOCATION_SERVICE);
        isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);

        if (!isGPS && !isNetwork) {
            Log.d(TAG, "Connection off");
            showSettingsAlert();
            getLastLocation();
        } else {
            Log.d(TAG, "Connection on");
            // check permissions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (permissionsToRequest.size() > 0) {
                    requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]),
                            ALL_PERMISSIONS_RESULT);
                    Log.d(TAG, "Permission requests");
                    canGetLocation = false;
                }
            }

            // get location
            getLocation();
        }

        mRestaurantTitle = (TextView) findViewById(R.id.titleLabel);
        mMainImage = (ImageView) findViewById(R.id.mainImage);
        mYelpLogo = (ImageView) findViewById(R.id.yelpLogo);

        businessNames = new ArrayList<String>();
        businessImages = new ArrayList<String>();
        businessUrls = new ArrayList<String>();

        mMainImage.setOnTouchListener(new OnSwipeTouchListener(this){
            public void onSwipeTop() {

            }
            public void onSwipeRight() {
                if (!isAtStart) {
                    Toast.makeText(SelectionPage.this, "Favorited!", Toast.LENGTH_SHORT).show();
                    Restaurant newFav = new Restaurant(businessNames.get(currentBusinessIndex - 1),currentUrl);
                    if(!currentUser.getFavorites().contains(newFav)){
                        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("favorites").push().setValue(newFav);
                    }
                }
            }
            public void onSwipeLeft() {
                if (isAtStart) {
                    Toast.makeText(SelectionPage.this, "Loading . . .", Toast.LENGTH_SHORT).show();
                    Map<String, String> params = new HashMap<>();

                    params.put("term", "food");
                    params.put("latitude", latitude);
                    params.put("longitude", longitude);

                    Call<SearchResponse> call = yelpFusionApi.getBusinessSearch(params);
                    call.enqueue(callback);
                } else {
                    if (currentBusinessIndex < 20) {
                        mRestaurantTitle.setText(businessNames.get(currentBusinessIndex));
                        Picasso.with(getApplicationContext())
                                .load(businessImages.get(currentBusinessIndex))
                                .into(mMainImage);
                        currentUrl = businessUrls.get(currentBusinessIndex);
                        currentBusinessIndex++;
                    } else {
                        Toast.makeText(SelectionPage.this, "End of Results Reached", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SelectionPage.this, MainPage.class);
                        startActivity(intent);
                    }
                }
            }
            public void onSwipeBottom() {

            }
        });

        FirebaseDatabase.getInstance().getReference("/users/" + FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUser = User.parseSnapshot(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SelectionPage.this,"Error fetching user data",Toast.LENGTH_LONG);
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
            if (searchResponse.getTotal() < 20) {
                numOfSearchResults = searchResponse.getTotal();
            } else {
                numOfSearchResults = 20;
            }
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
            mYelpLogo.setVisibility(View.VISIBLE);
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

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged");
        updateUI(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}

    @Override
    public void onProviderEnabled(String s) {
        getLocation();
    }

    @Override
    public void onProviderDisabled(String s) {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    private void getLocation() {
        try {
            if (canGetLocation) {
                Log.d(TAG, "Can get location");
                if (isGPS) {
                    // from GPS
                    Log.d(TAG, "GPS on");
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (loc != null)
                            updateUI(loc);
                    }
                } else if (isNetwork) {
                    // from Network Provider
                    Log.d(TAG, "NETWORK_PROVIDER on");
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (loc != null)
                            updateUI(loc);
                    }
                } else {
                    loc.setLatitude(0);
                    loc.setLongitude(0);
                    updateUI(loc);
                }
            } else {
                Log.d(TAG, "Can't get location");
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void getLastLocation() {
        try {
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, false);
            Location location = locationManager.getLastKnownLocation(provider);
            Log.d(TAG, provider);
            Log.d(TAG, location == null ? "NO LastLocation" : location.toString());
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private ArrayList findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList result = new ArrayList();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canAskPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canAskPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                Log.d(TAG, "onRequestPermissionsResult");
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(
                                                        new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }
                } else {
                    Log.d(TAG, "No rejected permissions.");
                    canGetLocation = true;
                    getLocation();
                }
                break;
        }
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("GPS is not Enabled!");
        alertDialog.setMessage("Do you want to turn on GPS?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(SelectionPage.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void updateUI(Location loc) {
        Log.d(TAG, "updateUI");
        latitude = (Double.toString(loc.getLatitude()));
        longitude = (Double.toString(loc.getLongitude()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }
}
