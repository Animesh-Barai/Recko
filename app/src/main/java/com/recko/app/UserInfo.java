package com.recko.app;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.recko.app.Misc.Constants;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class UserInfo extends AppCompatActivity {
    private static String TAG = UserInfo.class.getSimpleName();
    EditText name, location;
    Button next;
    private String NAME, LOCATION;
    Chip chip;
    private int toggle = 0;
    private String idToken;

    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    private LocationRequest mLocationRequest;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private FirebaseAnalytics mFirebaseAnalytics;
    private LocationCallback locationCallback;

    private AtomicBoolean requestSent;
    private boolean gotLocation = false;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        setContentView(R.layout.activity_user_info);

        name = findViewById(R.id.name);
        location = findViewById(R.id.location);
        next = findViewById(R.id.Next);
        chip = findViewById(R.id.chip);
        progressBar = findViewById(R.id.spin_kit);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        requestSent = new AtomicBoolean(false);

        name.requestFocus();

        location.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if(b) {
                    maybeAskUserToTurnOnLocation();
                }

            }
        });


        final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            idToken = task.getResult().getToken();
                            // Send token to your backend via HTTPS
                            // ...
                        } else {
                            // Handle error -> task.getException();
                        }
                    }
                });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!requestSent.compareAndSet(false, true)) return;

                progressBar.setVisibility(View.VISIBLE);
                NAME = name.getText().toString().trim();
                LOCATION = location.getText().toString().trim();

                if (!NAME.equals("") && !LOCATION.equals("")) {
                    JSONObject json = new JSONObject();
                    try {
                        json.put("seller_uuid", mUser.getUid());
                        json.put("seller_name", NAME);
                        json.put("seller_mobile_no", mUser.getPhoneNumber());
                        json.put("seller_location", LOCATION);
                        json.put("firebase_token", idToken);
                        Log.d("zzz json", json.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, APIURL.url + "user/insert", json,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    Log.d("zzz", APIURL.url + "user/insert" + "\nonResponse: " + response);
                                    requestSent.compareAndSet(true, false);
                                    try {

                                        if (response.getString("code").equals("200")) {
                                            Bundle bundle = new Bundle();
                                            bundle.putString("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                            mFirebaseAnalytics.logEvent("user_info_saved", bundle);

                                            Constants.setSellerNameNo(NAME, mUser.getPhoneNumber());

                                            Intent intent = new Intent(UserInfo.this, Industries.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            startActivity(intent);
                                            progressBar.setVisibility(View.GONE);
                                        } else {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(UserInfo.this, "Oops! Please try again later", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        progressBar.setVisibility(View.GONE);
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
							Constants.logVolleyError(error);
                            progressBar.setVisibility(View.GONE);
							requestSent.compareAndSet(true, false);
                            error.printStackTrace();
                            Toast.makeText(UserInfo.this, "Oops! Please try again later", Toast.LENGTH_SHORT).show();
                        }
                    });

                    RequestQueue requestQueue = Volley.newRequestQueue(UserInfo.this);
                    requestQueue.add(jsonObjectRequest);
                }
                else
                    Toast.makeText(getApplicationContext(),"Please fill the details",Toast.LENGTH_SHORT).show();
            }
        });

        location.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    next.performClick();
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                startLocationUpdates();
            } else {
                Toast.makeText(this, "Until you grant the permission, we cannot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 102) {
            startLocationUpdates();
        }
    }

    protected void startLocationUpdates() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
                return;
            }
        }

        /*final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    }
                }

                    fusedLocationProviderClient.getLastLocation().addOnSuccessListener(UserInfo.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location l) {
                            if (l != null)
                                Log.d("zzz coordinates", "Latitude = " + l.getLatitude() + " Longitude = " + l.getLongitude());
                            try {
                                if (l != null)
                                    coordinatesToAddress(l.getLatitude(), l.getLongitude());
                                else
                                    Toast.makeText(getApplicationContext(), "Not able to automatically detect location", Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            location.setOnFocusChangeListener(null);
                        }
                    });

            }
        }, 100);*/

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(UserInfo.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location l) {
                if (l != null)
                    Log.d("zzz coordinates","Latitude = " + l.getLatitude() + " Longitude = " + l.getLongitude());
                try {
                    if (l!=null)
                        coordinatesToAddress(l.getLatitude(),l.getLongitude());
                    else
                        locationUpdates();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                location.setOnFocusChangeListener(null);
            }
        });
    }

    private void locationUpdates() {
        LocationRequest mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1 * 1000)
                .setFastestInterval(1 * 1000);

        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                if (!gotLocation)
                    Toast.makeText(getApplicationContext(), "Not able to automatically detect location", Toast.LENGTH_SHORT).show();
                return;
            }
        }, 3000);

        locationCallback = new LocationCallback() {
//            @Override
//            public void onLocationAvailability (LocationAvailability locationAvailability){
//                Log.d(TAG, "Location not available");
//                if (!locationAvailability.isLocationAvailable()) {
//                    Toast.makeText(getApplicationContext(), "Not able to automatically detect location", Toast.LENGTH_SHORT).show();
//                    fusedLocationProviderClient.removeLocationUpdates(locationCallback);
//                    return;
//                }
//            }

            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Toast.makeText(getApplicationContext(), "Not able to automatically detect location", Toast.LENGTH_SHORT).show();
                    fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    try {
                        coordinatesToAddress(location.getLatitude(), location.getLongitude());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    gotLocation = true;
                    fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                    return;
                }
            }
        };
        try {
            fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, locationCallback, null);
        } catch (SecurityException e) {
            Toast.makeText(getApplicationContext(), "Not able to automatically detect location", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    protected void coordinatesToAddress(Double latitude, Double longitude) throws IOException {


        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String premises = addresses.get(0).getPremises();
        String sublocality = addresses.get(0).getSubLocality();
        String subadmin = addresses.get(0).getSubAdminArea();

        location.setText(address);
        Log.d("zzz coordinates",address+"\n"+city+"\n"+state+"\n"+country+"\n"+postalCode+"\n"+premises+"\n"+sublocality+"\n"+subadmin);

    }

    private AppCompatActivity getActivity() {
        return this;
    }

    private void maybeAskUserToTurnOnLocation() {
        LocationRequest mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1 * 1000);
        LocationSettingsRequest.Builder settingsBuilder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        settingsBuilder.setAlwaysShow(true);
        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(this)
                .checkLocationSettings(settingsBuilder.build());
        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                boolean should_call_startLocationUpdates = true;
                try {
                    LocationSettingsResponse response =
                            task.getResult(ApiException.class);

                } catch (ApiException ex) {
                    switch (ex.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvableApiException =
                                        (ResolvableApiException) ex;
                                resolvableApiException
                                        .startResolutionForResult(getActivity(),
                                                102);
                                should_call_startLocationUpdates = false;
                            } catch (IntentSender.SendIntentException e) {

                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:

                            break;
                    }
                }
                if (should_call_startLocationUpdates) startLocationUpdates();
            }
        });
    }

}
