package com.example.neelanshsethi.sello;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.ContactsContract;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class UserInfo extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        name = findViewById(R.id.name);
        location = findViewById(R.id.location);
        next = findViewById(R.id.Next);
        chip = findViewById(R.id.chip);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        name.requestFocus();

        location.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if(b) {
                    startLocationUpdates();
                }

            }
        });


        final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            idToken = task.getResult().getToken();
                            Log.d("tokennn", idToken);
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

                                    try {

                                        if (response.getString("code").equals("200")) {
                                            Intent intent = new Intent(UserInfo.this, Industries.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(UserInfo.this, "Oops! Please try again later", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

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

    protected void startLocationUpdates() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
                return;
            }
        }

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(UserInfo.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location l) {
                if (l != null)

                    Log.d("zzz coordinates","Latitude = " + l.getLatitude() + " Longitude = " + l.getLongitude());
                try {
                    coordinatesToAddress(l.getLatitude(),l.getLongitude());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
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

}
